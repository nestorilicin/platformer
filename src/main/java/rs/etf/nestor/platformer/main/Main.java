package rs.etf.nestor.platformer.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import rs.etf.nestor.platformer.cameras.Camera;
import rs.etf.nestor.platformer.sprites.*;
import rs.etf.nestor.platformer.sprites.newsprites.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mile
 */
public class Main extends Application {

    public static final int WINDOW_WIDTH = 1366;
    public static final int WINDOW_HEIGHT = 768;

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

    private Group sprites;
    private final Random rand = new Random();
    // 3. OBLAK
    private List<Cloud> clouds;
    private Group cloudGroup;
    // 4. VREME
    private Label timeLabel;
    private int remainingTime = 90; // startno vreme 90 sekundi
    private long lastUpdateTime = 0;

    private boolean GAME_OVER = false;

    // 9. PLATFORME
    private List<Platform> platforms;
    private Group platformGroup;
    // 10. RUPE
    private List<Hole> holes;
    private Group holeGroup;
    // 11. POENI
    private long points = 0;
    private Label pointsLabel;

    // 12. ZIVOTI
    private Group heartGroup;

    // 14. PTICE
    private Group birdGroup;
    private List<Bird> birds;
    // 15. NOVCICI
    private Group coinGroup;
    private List<Coin> coins;
    // 16. IMUNITET
    private Group immunityGroup;
    private List<ImmunityCapsule> immunityCapsules;
    private long immunityTimer = 0;
    private Label immunityLabel;
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

    private <T extends Sprite> void updateAndClean(List<T> list, Group group) {
        list.forEach(Sprite::update);
        list.removeIf(e -> e.getTranslateX() < -5 * WINDOW_WIDTH + WINDOW_HEIGHT);
        group.getChildren().removeIf(e -> e.getTranslateX() < -5 * WINDOW_WIDTH + WINDOW_HEIGHT);
    }

    private void increasePoints(int value) {
        points += value;
        pointsLabel.setText(String.format("%02d", points));
    }

    private void loseLife() {
        heartGroup.getChildren().remove(player.getLives() - 1);
        if (player.getLives() == 1) {
            game_over();
        }
    }
    private void game_over(){
        GAME_OVER = true;
        timeLabel.setText("GAME OVER");
        timeLabel.setTranslateX(WINDOW_WIDTH / 2. - timeLabel.getWidth() / 2.);
    }
    
    private void updateImmuneTimer(){
        immunityTimer--;
        if(immunityTimer == 0){
            immunityLabel.setText("");
            player.setImmune(false);
        }
        else immunityLabel.setText("Imunitet " + immunityTimer + "s");
    }
    private void startImmuneTimer(){
        immunityTimer = 10;
        immunityLabel.setText("Imunitet " + immunityTimer + "s");
    }
    // called once per frame to update game state
    private void update(double deltaTime) {
        Bounds playerBounds = player.getBoundsInParent();
        double playerBottom = playerBounds.getMaxY();
        if (player.isNotDead()) {
            player.update();
            // 9. kolizija za platforme
            // sletanje na platformu
            if (!player.isOnPlatform()) {
                for (Platform platform : platforms) {
                    Bounds platformBounds = platform.getBoundsInParent();
                    boolean isAbovePlatform = playerBottom <= platformBounds.getMinY() + 10;
                    boolean overlapsHorizontally = playerBounds.getMaxX() > platformBounds.getMinX() && playerBounds.getMinX() < platformBounds.getMaxX();
                    if (isAbovePlatform && overlapsHorizontally) {
                        double targetY = platformBounds.getMinY() - ENEMY_HEIGHT / 2.; // stane na platformu
                        if (player.getTranslateY() + 5 >= targetY) {
                            player.setTranslateY(targetY);
                            player.stop();
                            player.setCurrentPlatform(platform);
                            player.setOnPlatform(true);
                        } else if (player.isNotJumping()) {
                            player.setTranslateY(player.getTranslateY() + 5); // nastavlja da pada
                        }
                        break;
                    }
                }
            }
            // ukidanje platforme ako je spao
            if (player.isOnPlatform()) {
                Platform currentPlatform = player.getCurrentPlatform();
                if (currentPlatform != null) {
                    Bounds platformBounds = currentPlatform.getBoundsInParent();
                    boolean outsideHorizontally = playerBounds.getMaxX() < platformBounds.getMinX() || playerBounds.getMinX() > platformBounds.getMaxX();
                    if (outsideHorizontally) {
                        player.setCurrentPlatform(null);
                        player.setOnPlatform(false);
                    }
                }
            }
            // pad sa platforme
            if (!player.isOnPlatform() && player.isNotJumping()) {
                double floorY = WINDOW_HEIGHT - FLOOR_HEIGHT;
                if (playerBottom >= floorY) {
                    player.setTranslateY(floorY - 40); // na floor
                } else {
                    player.setTranslateY(player.getTranslateY() + 5); // pada na dole
                }
            }
            // 10. kolizija za rupe
            for (Hole hole : holes) {
                Bounds holeBounds = hole.getBoundsInParent();
                boolean insideHorizontally = playerBounds.getMinX() >= holeBounds.getMinX() && playerBounds.getMaxX() <= holeBounds.getMaxX();
                boolean insideVertically = playerBottom >= holeBounds.getMinY() && playerBottom <= holeBounds.getMaxY();
                if (insideHorizontally && insideVertically) {
                    player.fallInHole();
                    loseLife();
                    break; 
                }
            }

            // 13. napredna kolizija za neprijatelje
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                Bounds enemyBounds = enemy.getBoundsInParent();
                double enemyTop = enemyBounds.getMinY();
                if (playerBounds.intersects(enemyBounds)) {
                    if (playerBottom <= enemyTop + 10) {
                        sprites.getChildren().remove(enemy);
                        enemyIterator.remove();
                        increasePoints(20);
                    } else {
                        if(player.isImmune()) continue;
                        player.takeHit();
                        loseLife();
                    }
                }
            }
            // 15. kolizija za novcice
            Iterator<Coin> coinIterator = coins.iterator();
            while (coinIterator.hasNext()) {
                Coin coin = coinIterator.next();
                if (playerBounds.intersects(coin.getBoundsInParent())) {
                    coinIterator.remove();
                    coinGroup.getChildren().remove(coin);
                    increasePoints(10);
                    break;
                }
            }
            // 16. kolizija za imunitet
            Iterator<ImmunityCapsule> immunityCapsuleIterator = immunityCapsules.iterator();
            while(immunityCapsuleIterator.hasNext()) {
                ImmunityCapsule immunityCapsule = immunityCapsuleIterator.next();
                if(playerBounds.intersects(immunityCapsule.getBoundsInParent())) {
                    player.setImmune(true);
                    startImmuneTimer();
                    immunityCapsuleIterator.remove();
                    immunityGroup.getChildren().remove(immunityCapsule);
                    break;
                }
            }
        }

        updateAndClean(enemies, sprites);

        // 3. OBLAK
        updateAndClean(clouds, cloudGroup);
        // 9. PLATFORME
        updateAndClean(platforms, platformGroup);
        // 10. RUPE
        updateAndClean(holes, holeGroup);
        // 14. PTICE 
        updateAndClean(birds, birdGroup);
        // 15. NOVCICI
        updateAndClean(coins, coinGroup);
        // 16. IMUNITET
        updateAndClean(immunityCapsules, immunityGroup);

        time += 1. / 60;
        secondPassed += deltaTime;
        if (secondPassed > 1.0) {
            remainingTime--;
            secondPassed = 0;
            // 3. OBLAK
            if (remainingTime % 4 == 0) {
                Cloud cloud = new Cloud(player.getTranslateX() + WINDOW_WIDTH, 100 + rand.nextInt(250), 1.75);
                clouds.add(cloud);
                cloudGroup.getChildren().add(cloud);
            }
            // 4. VREME
            int minutes = remainingTime / 60;
            int seconds = remainingTime % 60;
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
            if (remainingTime <= 0) {
                timeLabel.setText("00:00");
                game_over();
            }
            // 10. RUPE
            if (remainingTime % 7 == 2) {
                Hole hole = new Hole(player.getTranslateX() + WINDOW_WIDTH, ENEMY_SPEED);
                holes.add(hole);
                holeGroup.getChildren().add(hole);
            }
            // 11. POENI
            increasePoints(1);
            // 14. PTICE
            if (remainingTime % 4 == 1) {
                Bird bird = new Bird(player.getTranslateX() + WINDOW_WIDTH, 100 + rand.nextInt(250), 1.75);
                birds.add(bird);
                birdGroup.getChildren().add(bird);
            }
            // 15. NOVCICI
            if (remainingTime % 5 == 1) {
                Coin coin = new Coin(player.getTranslateX() + 3 * WINDOW_WIDTH / 4. + rand.nextInt(WINDOW_WIDTH / 4), WINDOW_HEIGHT - FLOOR_HEIGHT - Coin.getRadius(), ENEMY_SPEED);
                coins.add(coin);
                coinGroup.getChildren().add(coin);
            }
            // 16. IMUNITET
            if (remainingTime % 10 == 1){
                ImmunityCapsule capsule = new ImmunityCapsule(ENEMY_SPEED);
                capsule.getTransforms().add(new Scale(15, 15));
                capsule.setTranslateX(player.getTranslateX() + WINDOW_WIDTH + 50);
                capsule.setTranslateY(WINDOW_HEIGHT - FLOOR_HEIGHT - 30);
                immunityCapsules.add(capsule);
                immunityGroup.getChildren().add(capsule);
            }
            if(player.isImmune() && immunityTimer > 0){
                updateImmuneTimer();
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

        sprites = new Group();
        camera.getChildren().add(sprites);

        player = new Player();
        player.setTranslateX(100);
        player.setTranslateY(WINDOW_HEIGHT - FLOOR_HEIGHT - ENEMY_HEIGHT / 2.);
        
        // 3. OBLAK
        clouds = new LinkedList<>();
        cloudGroup = new Group();
        // 4. VREME
        timeLabel = new Label(String.format("%02d:%02d", remainingTime / 60, remainingTime % 60));
        timeLabel.setTextFill(Color.RED);
        timeLabel.setFont(new Font(24));
        timeLabel.setTranslateX(WINDOW_WIDTH / 2. - timeLabel.getWidth() / 2);
        timeLabel.setTranslateY(5);
        // 9. PLATFORME 
        platforms = new LinkedList<>();
        platformGroup = new Group();
        // 10. RUPE
        holes = new LinkedList<>();
        holeGroup = new Group();
        // 11. POENI
        pointsLabel = new Label("00");
        pointsLabel.setTextFill(Color.YELLOW);
        pointsLabel.setFont(new Font(24));
        pointsLabel.setTranslateX(20);
        pointsLabel.setTranslateY(5);
        // 12. ZIVOTI
        heartGroup = new Group();
        for (int i = 0; i < player.getLives(); i++) {
            double x = WINDOW_WIDTH - (player.getLives() - i) * 50;
            double y = 20;
            Heart heart = new Heart(x, y);
            heartGroup.getChildren().add(heart);

        }
        // 14. PTICE
        birds = new LinkedList<>();
        birdGroup = new Group();

        // 15. NOVCICI
        coins = new LinkedList<>();
        coinGroup = new Group();

        // 16. KAPSULE
        immunityGroup = new Group();
        immunityCapsules = new LinkedList<>();
        immunityLabel = new Label("");
        immunityLabel.setTextFill(Color.CYAN);
        immunityLabel.setFont(new Font(24));
        immunityLabel.setTranslateX(WINDOW_WIDTH - 150);
        immunityLabel.setTranslateY(50);
        // making 100 enemies and adding them to the scene
        for (int i = 0; i < 100; i++) {
            Enemy enemy = new Enemy(ENEMY_SPEED);
            enemy.setTranslateX((1 + i) * 800);
            enemy.setTranslateY(WINDOW_HEIGHT - FLOOR_HEIGHT - ENEMY_HEIGHT / 2.);
            sprites.getChildren().add(enemy);
            enemies.add(enemy);
        }
        for (int i = 0; i < 100; i++) {
            double width = 100 + rand.nextInt(WINDOW_WIDTH / 4);
            double height = 20;
            double y = WINDOW_HEIGHT - FLOOR_HEIGHT - 100 - rand.nextInt(WINDOW_WIDTH / 8);

            Platform p = new Platform(width, height, ENEMY_SPEED);
            p.setTranslateX((1 + i) * 800);
            p.setTranslateY(y);
            platforms.add(p);
            platformGroup.getChildren().add(p);
            if (i % 2 == 1) { // na svakoj drugoj neprijatelj
                Enemy enemy = new Enemy(ENEMY_SPEED);
                enemy.setTranslateX(p.getTranslateX() + width / 2.);
                enemy.setTranslateY(y - ENEMY_HEIGHT / 2.);
                sprites.getChildren().add(enemy);
                enemies.add(enemy);
            }

        }
        Group root = new Group();
        root.getChildren().add(bg);
        root.getChildren().add(camera);
        root.getChildren().add(timeLabel);
        root.getChildren().add(heartGroup);
        root.getChildren().add(pointsLabel);
        root.getChildren().add(immunityLabel);

        sprites.getChildren().add(birdGroup);
        sprites.getChildren().add(cloudGroup);
        sprites.getChildren().add(coinGroup);
        sprites.getChildren().add(platformGroup);
        sprites.getChildren().add(holeGroup);
        sprites.getChildren().add(immunityGroup);
        sprites.getChildren().add(player);

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
