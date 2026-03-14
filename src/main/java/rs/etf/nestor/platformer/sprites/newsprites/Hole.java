package rs.etf.nestor.platformer.sprites.newsprites;


import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import rs.etf.nestor.platformer.main.Main;
import rs.etf.nestor.platformer.sprites.Sprite;

public class Hole extends Sprite {
    // 10. RUPA
    private final double speed;
    public Hole(double x, double speed) {
        this.speed = speed;
        Rectangle block = new Rectangle(150, 10 * Main.FLOOR_HEIGHT);
        Stop[] stops = new Stop[]{
                new Stop(0, Color.STEELBLUE), new Stop(1, Color.MIDNIGHTBLUE)};
        LinearGradient skyGradient = new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE, stops);
        block.setFill(skyGradient);
        getChildren().add(block);
        setTranslateX(x);
        setTranslateY(Main.WINDOW_HEIGHT - Main.FLOOR_HEIGHT);
    }
    

    @Override
    public void update() {
        setTranslateX(getTranslateX() - speed);
    }
}
