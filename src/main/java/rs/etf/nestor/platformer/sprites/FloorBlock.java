/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.nestor.platformer.sprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author mile
 */
public class FloorBlock extends Sprite {

    private Rectangle block;

    public FloorBlock() {
        block = new Rectangle(-0.5, -0.5, 1, 1);
        block.setFill(Color.SIENNA);
        // 2. TRAVA
        Polygon grass = new Polygon(
                -0.5, -0.5,
                -0.5, 0.,
                -0.25, -0.25,
                0., 0.,
                0.25, -0.25,
                0.5, 0.,
                0.5, -0.5
        );
        grass.setFill(Color.LAWNGREEN);
        getChildren().addAll(block, grass);

    }

}
