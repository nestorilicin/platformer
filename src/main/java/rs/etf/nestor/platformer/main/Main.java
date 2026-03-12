package rs.etf.nestor.platformer.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rs.etf.nestor.platformer.cameras.Camera;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rs.etf.nestor.platformer.sprites.Background;
import rs.etf.nestor.platformer.sprites.Enemy;
import rs.etf.nestor.platformer.sprites.Floor;
import rs.etf.nestor.platformer.sprites.Player;
import rs.etf.nestor.platformer.sprites.newsprites.Cloud;
import rs.etf.nestor.platformer.sprites.newsprites.Heart;

/**
 *
 * @author mile
 */
public class Main extends Application {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 450;

    public static final int FLOOR_WIDTH = WINDOW_WIDTH;
    public static final int FLOOR_HEIGHT = 50;

    public static final int ENEMY_WIDTH = 100;
    public static final int ENEMY_HEIGHT = 80;

    public static final String TITLE = "Platformer";

    public static final double ENEMY_SPEED = 2.5f;

    private Player player;
    private List<Enemy> enemies;

    private static Main instance;

    private double time = 0; // game time in seconds
    private double secondPassed = 0; // trigger

    private Camera camera; // camera used for applying transformations

    private final Random rand = new Random();
    // 3. OBLAK
    private List<Cloud> clouds;
    private Group cloudGroup;
    // 4. VREME
    private Label timeLabel;
    private int remainingTime = 90; // startno vreme 90 sekundi
    private long lastUpdateTime = 0;
    
    private boolean GAME_OVER = false;

    // 12. ZIVOTI
    private Group heartGroup;
    // returns the instance of the game (Main class)
    public static Main getInstance() {
        return instance;
    }

    public Camera getCamera() {
        return camera;
    }

    public double getTime() {
        return time;
    }


    // called once per frame to update game state
    private void update(double deltaTime) {
        if(player.isNotDead()){
            player.update();
            // checking collision between player and enemies
            enemies.forEach(e -> {
                if (player.getBoundsInParent().intersects(e.getBoundsInParent())) {
                    player.takeHit();
                    heartGroup.getChildren().remove(player.getLives() - 1);
                    if(player.getLives() == 1){
                        GAME_OVER = true;
                    }
                }
            });
        }
        
        enemies.forEach(e -> e.update());

        
        // 3. OBLAK
        clouds.forEach(Cloud::update);
        clouds.removeIf(c -> c.getTranslateX() + WINDOW_WIDTH + 100 < 0);
        time += 1. / 60;
        secondPassed += deltaTime;
        if (secondPassed > 1.0) {
            remainingTime--;
            secondPassed = 0;
            // 3. OBLAK
            if (remainingTime % 4 == 0) {
                Cloud cloud = new Cloud(WINDOW_WIDTH, 100 + rand.nextInt(250), 1.75);
                clouds.add(cloud);
                cloudGroup.getChildren().add(cloud);
            }
            // 4. VREME
            int minutes = remainingTime / 60;
            int seconds = remainingTime % 60;
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));


            // 4. VREME
            if (remainingTime <= 0) {
                timeLabel.setText("00:00");
                GAME_OVER = true;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;

        enemies = new LinkedList<>();
        Background bg = new Background(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        Floor floor = new Floor(FLOOR_WIDTH, FLOOR_HEIGHT);
        floor.setTranslateY(WINDOW_HEIGHT);

        camera = new Camera();
        camera.getChildren().add(floor);

        Group sprites = new Group();
        camera.getChildren().add(sprites);

        // making 100 enemies and adding them to the scene
        for (int i = 0; i < 100; i++) {
            Enemy enemy = new Enemy(ENEMY_SPEED);
            enemy.setTranslateX((1 + i) * 800);
            enemy.setTranslateY(WINDOW_HEIGHT - FLOOR_HEIGHT - ENEMY_HEIGHT / 2.);
            sprites.getChildren().add(enemy);
            enemies.add(enemy);
        }

        player = new Player();
        player.setTranslateX(100);
        player.setTranslateY(WINDOW_HEIGHT - FLOOR_HEIGHT - ENEMY_HEIGHT / 2.);
        sprites.getChildren().add(player);

        // 3. OBLAK
        clouds = new LinkedList<>();
        cloudGroup = new Group();
        // 4. VREME
        timeLabel = new Label(String.format("%02d:%02d", remainingTime / 60, remainingTime % 60));
        timeLabel.setTextFill(Color.RED);
        timeLabel.setFont(new Font(24));
        timeLabel.setTranslateX(WINDOW_WIDTH / 2. - timeLabel.getWidth() / 2);
        timeLabel.setTranslateY(20);

        // 12. ZIVOTI
        heartGroup = new Group();
        for (int i = 0; i < player.getLives(); i++) {
            double x = WINDOW_WIDTH - (player.getLives() - i) * 50;
            double y = 20;
            Heart heart = new Heart(x, y);
            heartGroup.getChildren().add(heart);

        }
        Group root = new Group();
        root.getChildren().add(bg);
        root.getChildren().add(camera);
        root.getChildren().add(cloudGroup);
        root.getChildren().add(timeLabel);
        root.getChildren().add(heartGroup);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(player);
        scene.setOnKeyReleased(player);
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

        // calling update once every frame
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (lastUpdateTime > 0 && !GAME_OVER) {
                    double deltaTime = (currentNanoTime - lastUpdateTime) / 1000000000.0; // u sekundama
                    update(deltaTime);
                }
                lastUpdateTime = currentNanoTime;
            }
        }.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
