/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.nestor.platformer.cameras;

import javafx.scene.Group;
import javafx.scene.Node;
import rs.etf.nestor.platformer.main.Main;

/**
 *
 * @author mile
 */
public class Camera extends Group {

    public Camera() {
        getTransforms().clear();
    }
    
    public void centerOn(Node target){
        double targetX = target.getTranslateX();
        setTranslateX(Main.WINDOW_WIDTH / 2. - targetX);
    }
    public void resetPosition(Node target){
        //setTranslateX(0);
        setTranslateX(100 - target.getTranslateX());
    }

}
