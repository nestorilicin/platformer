/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.nestor.platformer.sprites;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rs.etf.nestor.platformer.cameras.Camera;
import rs.etf.nestor.platformer.main.Main;
import rs.etf.nestor.platformer.sprites.newsprites.Platform;
import rs.etf.nestor.platformer.sprites.playerstates.DeadState;
import rs.etf.nestor.platformer.sprites.playerstates.IdleState;
import rs.etf.nestor.platformer.sprites.playerstates.JumpState;
import rs.etf.nestor.platformer.sprites.playerstates.RunState;
import rs.etf.nestor.platformer.sprites.playerstates.State;

/**
 *
 * @author mile
 */
public class Player extends Sprite implements EventHandler<KeyEvent> {

    private static final Paint IDLE_COLOR = Color.YELLOW;
    private static final Paint RUN_COLOR = Color.GREEN;
    private static final Paint JUMP_COLOR = Color.RED;
    private static final Paint DEAD_COLOR = Color.PURPLE;

    private static final Paint EYE_COLOR = Color.BLACK;

    private static final double PLAYER_VELOCITY = 7.5f;

    private State state;
    private boolean right = true;
    private double velocity = PLAYER_VELOCITY;

    private final Circle body;

    private int lives = 3;
    private final Camera camera;
    private boolean onPlatform;
    private Platform currentPlatform;
    private boolean immune = false;
    private final Circle capsule;
    
    public Player() {
        state = new IdleState(this);
        getChildren().clear();
        body = new Circle(0, 0, 40);
        body.setFill(IDLE_COLOR);
        Circle eye = new Circle(15, 0, 10);
        eye.setFill(EYE_COLOR);
        getChildren().add(body);
        getChildren().add(eye);
        camera = Main.getInstance().getCamera();
        /*glowEffect= new DropShadow();
        glowEffect.setOffsetY(0f);
        glowEffect.setOffsetX(0f);
        glowEffect.setColor(Color.WHITE);
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        */
        capsule = new Circle(0,0,45);
        capsule.setFill(Color.rgb(255,255,255,0.4));
        capsule.setStroke(Color.CYAN);
        capsule.setStrokeWidth(2);
    }

    public double getVelocity() {
        if (isFaceRight()) {
            return velocity;
        } else {
            return -velocity;
        }
    }

    public void run() {
        state = new RunState(this);
        body.setFill(RUN_COLOR);
    }

    public void faceRight() {
        if (isFaceLeft()) {
            setScaleX(-getScaleX());
        }
        right = true;
    }

    public void faceLeft() {
        if (isFaceRight()) {
            setScaleX(-getScaleX());
        }
        right = false;
    }

    public void jump() {
        state = new JumpState(this);
        body.setFill(JUMP_COLOR);
    }

    public void stop() {
        state = new IdleState(this);
        body.setFill(IDLE_COLOR);
    }

    public void die() {
        state = new DeadState(this);
        body.setFill(DEAD_COLOR);
        // 7. ANIMACIJA za izgubljeni zivot
        KeyValue rotateStart = new KeyValue(rotateProperty(), 0);
        KeyValue rotateEnd = new KeyValue(rotateProperty(), 720);
        
        double currentY = getTranslateY();
        double groundY = Main.WINDOW_HEIGHT - Main.FLOOR_HEIGHT - Main.ENEMY_HEIGHT / 2.;
        KeyValue current = new KeyValue(translateYProperty(), currentY);
        KeyValue jump = new KeyValue(translateYProperty(), currentY - 150);
        KeyValue fall = new KeyValue(translateYProperty(), Main.WINDOW_HEIGHT - Main.FLOOR_HEIGHT + 75);
        KeyValue ground = new KeyValue(translateYProperty(), groundY);
        Timeline deathAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), rotateStart),
                new KeyFrame(Duration.seconds(0.5), jump),
                new KeyFrame(Duration.seconds(1), current),
                new KeyFrame(Duration.seconds(1.5), rotateEnd, fall),
                new KeyFrame(Duration.seconds(2), ground)
        );
        deathAnimation.setCycleCount(1);
        deathAnimation.play();
        deathAnimation.setOnFinished((e) -> {
            lives--;
            state = new IdleState(this);
            body.setFill(IDLE_COLOR);
            setTranslateY(groundY);
        });
        
    }

    public boolean isFaceRight() {
        return right;
    }

    public boolean isFaceLeft() {
        return !right;
    }

    public int getLives() {
        return lives;
    }
    public boolean isNotDead(){
        return state.indState() != 'D';
    }
    public boolean isNotJumping(){
        return state.indState() != 'J';
    }
    public boolean isOnPlatform() {
        return onPlatform;
    }

    public void setOnPlatform(boolean onPlatform) {
        this.onPlatform = onPlatform;
    }

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(Platform currentPlatform) {
        this.currentPlatform = currentPlatform;
    }

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
        if(immune){
            if(!getChildren().contains(capsule)) getChildren().add(capsule);
        }
        else 
            getChildren().remove(capsule);
    }

    public Circle getBody() {
        return body;
    }

    @Override
    public void update() {
        state.move();
    }

    // executed on keyboard input to perform particular actions
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT && event.getEventType() == KeyEvent.KEY_PRESSED) {
            state.rightPressed();
        } else if (event.getCode() == KeyCode.RIGHT && event.getEventType() == KeyEvent.KEY_RELEASED) {
            state.rightReleased();
        } else if (event.getCode() == KeyCode.LEFT && event.getEventType() == KeyEvent.KEY_PRESSED) {
            state.leftPressed();
        } else if (event.getCode() == KeyCode.LEFT && event.getEventType() == KeyEvent.KEY_RELEASED) {
            state.leftReleased();
        } else if (event.getCode() == KeyCode.SPACE && event.getEventType() == KeyEvent.KEY_PRESSED) {
            state.jumpPressed();
        } else if (event.getCode() == KeyCode.SPACE && event.getEventType() == KeyEvent.KEY_RELEASED) {
            state.jumpReleased();
        } else if (event.getCode() == KeyCode.DIGIT1 && event.getEventType() == KeyEvent.KEY_PRESSED) {
            camera.resetPosition(this);
        } else if (event.getCode() == KeyCode.DIGIT2 && event.getEventType() == KeyEvent.KEY_PRESSED) {
            camera.centerOn(this);
        } else if (event.getCode() == KeyCode.DIGIT3 && event.getEventType() == KeyEvent.KEY_PRESSED) {
            velocity++;
        }
    }

    public void takeHit() {
        die();
    }
    
    public void fallInHole(){
        state = new DeadState(this);
        body.setFill(DEAD_COLOR);
        
        double currentY = getTranslateY();
        double groundY = Main.WINDOW_HEIGHT - Main.FLOOR_HEIGHT - Main.ENEMY_HEIGHT / 2.;
        KeyValue current = new KeyValue(translateYProperty(), currentY);
        KeyValue fall = new KeyValue(translateYProperty(), Main.WINDOW_HEIGHT - Main.FLOOR_HEIGHT + 75);
        KeyValue ground = new KeyValue(translateYProperty(), groundY);
        Timeline deathAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), current),
                new KeyFrame(Duration.seconds(0.5), fall),
                new KeyFrame(Duration.seconds(1), ground)
        );
        deathAnimation.setCycleCount(1);
        deathAnimation.play();
        deathAnimation.setOnFinished((e) -> {
            lives--;
            state = new IdleState(this);
            body.setFill(IDLE_COLOR);
            setTranslateY(groundY);
        });
    }
}
