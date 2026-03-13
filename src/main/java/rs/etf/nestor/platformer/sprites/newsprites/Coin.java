package rs.etf.nestor.platformer.sprites.newsprites;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rs.etf.nestor.platformer.sprites.Sprite;

public class Coin extends Sprite {
    // 15. NOVCIC
    private final double speed;
    private static final long radius = 25;
    public Coin(double x, double y, double speed) {
        this.speed = speed;
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.GOLD);
        Timeline coinAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(circle.centerYProperty(), y)),
                new KeyFrame(Duration.seconds(1), new KeyValue(circle.centerYProperty(), y - 15))
        );
        coinAnimation.setAutoReverse(true);
        coinAnimation.setCycleCount(Animation.INDEFINITE);
        coinAnimation.play();
        getChildren().add(circle);
    }

    public static long getRadius() {
        return radius;
    }

    @Override
    public void update() {
        setTranslateX(getTranslateX() - speed);
    }
    
}
