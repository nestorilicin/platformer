package rs.etf.nestor.platformer.sprites.newsprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Polygon;
import rs.etf.nestor.platformer.sprites.Sprite;

public class Heart extends Sprite {
    public Heart (double x, double y) {
        Arc left = new Arc(x - 10,y,10,10,0,180);
        Arc right = new Arc(x + 10,y,10,10,0,180);
        Polygon triangle = new Polygon(
                x-20, y,
                x, y + 20,
                x+20, y
        );
        left.setFill(Color.RED);
        right.setFill(Color.RED);
        triangle.setFill(Color.RED);
        getChildren().addAll(left,triangle,right);
    }
}
