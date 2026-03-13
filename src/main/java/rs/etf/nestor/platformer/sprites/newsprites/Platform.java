package rs.etf.nestor.platformer.sprites.newsprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rs.etf.nestor.platformer.sprites.Sprite;

public class Platform extends Sprite {
    private final double speed;
    public Platform(double width, double height, double speed) {
        this.speed = speed;
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.GREEN);
        getChildren().add(rectangle);
    }

    @Override
    public void update() {
        setTranslateX(getTranslateX() - speed);
    }
}
