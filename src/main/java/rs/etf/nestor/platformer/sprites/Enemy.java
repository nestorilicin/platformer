/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.nestor.platformer.sprites;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 *
 * @author mile
 */
public class Enemy extends Sprite {

    private final double velocity;

    public Enemy(double velocity) {
        this.velocity = velocity;
        // 5. OBLIK NEPRIJATELJA
        /* STARA GLAVA
        Arc head = new Arc();
        head.setCenterX(0);
        head.setCenterY(0.3);
        head.setRadiusX(0.5f);
        head.setRadiusY(0.8f);
        head.setStartAngle(0);
        head.setLength(180.0f);
        head.setType(ArcType.CHORD);
        head.setFill(Color.SADDLEBROWN);
        */
        // GLAVA
        Polygon head = new Polygon(
                -0.4, 0.0,
                -0.4, 0.3,
                0.4, 0.3,
                0.4, 0.0,
                0.5, 0.0,
                0.35, -0.5,
                -0.35, -0.5,
                -0.5, 0.0
        );
        head.setFill(Color.SADDLEBROWN);
        
        // OBRVE
        Rectangle leftEyebrow = new Rectangle(
                -.5, -.25, .5, .1
        );
        leftEyebrow.setFill(Color.BLACK);
        Rectangle rightEyebrow = new Rectangle(
                .0, -.25, .5, .1
        );
        rightEyebrow.setFill(Color.BLACK);
        
        // ROTACIJA OBRVA DA BUDU LJUTI NEPRIJATELJI
        Rotate leftRotation = new Rotate(25, 0, -0.25);
        leftEyebrow.getTransforms().add(leftRotation);
        Rotate rightRotation = new Rotate(-25, 0, -0.25);
        rightEyebrow.getTransforms().add(rightRotation);

        // USTA
        Line mouth = new Line(-0.35, 0.1, 0.35, 0.1);
        mouth.setFill(Color.BLACK);
        mouth.setStrokeWidth(0.03);

        // ZUBI
        Polygon leftTooth = new Polygon(
                -0.35, 0.1,
                -0.2, 0.1,
                -0.275, 0.0
        );
        leftTooth.setFill(Color.WHITE);
        Polygon rightTooth = new Polygon(
                0.35, 0.1,
                0.2, 0.1,
                0.275, 0.0
        );
        rightTooth.setFill(Color.WHITE);
        
        // NOGE
        Rectangle leftLeg = new Rectangle(-0.4, 0.3, 0.2, 0.2);
        leftLeg.setFill(Color.BLACK);
        Rectangle rightLeg = new Rectangle(0.2, 0.3, 0.2, 0.2);
        rightLeg.setFill(Color.BLACK);
        
        // OCI
        Circle leftEye = new Circle(-0.2f, -0.15, 0.15);
        leftEye.setFill(Color.WHITE);
        Circle rightEye = new Circle(0.2f, -0.15, 0.15);
        rightEye.setFill(Color.WHITE);
        
        // ZENICE
        Circle leftPupil = new Circle(-0.2f, -0.15, 0.05);
        leftPupil.setFill(Color.BLACK);
        Circle rightPupil = new Circle(0.2f, -0.15, 0.05);
        rightPupil.setFill(Color.BLACK);
        
        // 6. ZENICE OSCILUJU
        Timeline leftPupilAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(leftPupil.centerXProperty(), -0.3)),
                new KeyFrame(Duration.seconds(1), new KeyValue(leftPupil.centerXProperty(), -0.1)),
                new KeyFrame(Duration.seconds(2), new KeyValue(leftPupil.centerXProperty(), -0.3))
        );
        leftPupilAnimation.setCycleCount(Animation.INDEFINITE);
        leftPupilAnimation.play();
        Timeline rightPupilAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(rightPupil.centerXProperty(), 0.1)),
                new KeyFrame(Duration.seconds(1), new KeyValue(rightPupil.centerXProperty(), 0.3)),
                new KeyFrame(Duration.seconds(2), new KeyValue(rightPupil.centerXProperty(), 0.1))
        );
        rightPupilAnimation.setCycleCount(Animation.INDEFINITE);
        rightPupilAnimation.play();

        getChildren().add(head);
        getChildren().add(mouth);
        getChildren().add(leftTooth);
        getChildren().add(rightTooth);
        getChildren().add(leftLeg);
        getChildren().add(rightLeg);
        getChildren().add(leftEye);
        getChildren().add(rightEye);
        getChildren().add(leftPupil);
        getChildren().add(rightPupil);
        getChildren().add(leftEyebrow);
        getChildren().add(rightEyebrow);

        Scale scale = new Scale(100, 80);
        getTransforms().add(scale);
    }

    @Override
    public void update() {
        setTranslateX(getTranslateX() - velocity);
    }

}
