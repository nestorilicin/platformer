package rs.etf.nestor.platformer.sprites.newsprites;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import rs.etf.nestor.platformer.sprites.Sprite;

public class Bird extends Sprite {
    private final double speed;

    public Bird(double x, double y, double speed) {
        this.speed = speed;
        /*Circle c1 = new Circle(x, y, 30, Color.WHITE);
        Circle c2 = new Circle(x, y + 15, 20, Color.WHITE);
        Circle c3 = new Circle(x + 20, y + 15, 20, Color.WHITE);
        Circle c4 = new Circle(x - 20, y + 15, 20, Color.WHITE);
        getChildren().addAll(c1, c2, c3, c4);*/
        Arc leftArc = new Arc();
        leftArc.setCenterX(x - 30);
        leftArc.setCenterY(y);
        leftArc.setRadiusX(30);
        leftArc.setRadiusY(12);
        leftArc.setStartAngle(0);
        leftArc.setLength(90);
        leftArc.setFill(null);
        leftArc.setStroke(Color.BLACK);
        leftArc.setStrokeWidth(5);

        Arc rightArc = new Arc();
        rightArc.setCenterX(x + 30);
        rightArc.setCenterY(y);
        rightArc.setRadiusX(30);
        rightArc.setRadiusY(12);
        rightArc.setStartAngle(90);
        rightArc.setLength(90);
        rightArc.setFill(null);
        rightArc.setStroke(Color.BLACK);
        rightArc.setStrokeWidth(5);
        

        Timeline leftWingAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(leftArc.radiusYProperty(), 12), new KeyValue(leftArc.centerYProperty(), y)),
                new KeyFrame(Duration.seconds(1), new KeyValue(leftArc.radiusYProperty(), 18), new KeyValue(leftArc.centerYProperty(), y - 30)),
                new KeyFrame(Duration.seconds(2), new KeyValue(leftArc.radiusYProperty(), 12), new KeyValue(leftArc.centerYProperty(), y))
        );
        leftWingAnimation.setCycleCount(Animation.INDEFINITE);
        
        leftWingAnimation.play();
        Timeline rightWingAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(rightArc.radiusYProperty(), 12), new KeyValue(rightArc.centerYProperty(), y)),
                new KeyFrame(Duration.seconds(1), new KeyValue(rightArc.radiusYProperty(), 18), new KeyValue(rightArc.centerYProperty(), y - 30)),
                new KeyFrame(Duration.seconds(2), new KeyValue(rightArc.radiusYProperty(), 12), new KeyValue(rightArc.centerYProperty(), y))
        );
        rightWingAnimation.setCycleCount(Animation.INDEFINITE);
        rightWingAnimation.setAutoReverse(true);
        rightWingAnimation.play();
        
        
        getChildren().addAll(leftArc, rightArc);


    }

    @Override
    public void update() {
        setTranslateX(getTranslateX() - speed);
    }
}
