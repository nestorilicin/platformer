package rs.etf.nestor.platformer.sprites.newsprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import rs.etf.nestor.platformer.sprites.Sprite;

public class Cloud extends Sprite {
    // 3. OBLACI
    private final double speed;

    public Cloud(double x, double y, double speed) {
        this.speed = speed;
        Circle c1 = new Circle(x, y, 30, Color.WHITE);
        Circle c2 = new Circle(x, y + 15, 20, Color.WHITE);
        Circle c3 = new Circle(x + 20, y + 15, 20, Color.WHITE);
        Circle c4 = new Circle(x - 20, y + 15, 20, Color.WHITE);
        getChildren().addAll(c1, c2, c3, c4);
    }

    @Override
    public void update() {
        setTranslateX(getTranslateX() - speed);
    }
}
