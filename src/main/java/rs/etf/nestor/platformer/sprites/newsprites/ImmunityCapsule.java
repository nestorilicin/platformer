package rs.etf.nestor.platformer.sprites.newsprites;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import rs.etf.nestor.platformer.sprites.Sprite;

public class ImmunityCapsule extends Sprite {
    private final double speed;

    public ImmunityCapsule(double speed) {
        this.speed = speed;
        Ellipse capsule = new Ellipse(
        );
        capsule.setFill(Color.CYAN);
        capsule.setRadiusX(1);
        capsule.setRadiusY(1.5);
        DropShadow glowEffect= new DropShadow();
        glowEffect.setOffsetY(0f);
        glowEffect.setOffsetX(0f);
        glowEffect.setColor(Color.RED);
        glowEffect.setWidth(5);
        glowEffect.setHeight(5);
        capsule.setEffect(glowEffect);
        getChildren().add(capsule);
    }

    @Override
    public void update() {
        setTranslateX(getTranslateX() - speed);
    }
}
