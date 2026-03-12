/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.nestor.platformer.sprites.playerstates;

import rs.etf.nestor.platformer.sprites.Player;

/**
 *
 * @author mile
 */
public class IdleState extends State {

    @Override
    public char indState() {
        return 'I';
    }

    public IdleState(Player player) {
        super(player);
    }

    @Override
    public void jumpPressed() {
        super.jumpPressed();
        player.jump();
    }

    @Override
    public void leftPressed() {
        super.leftPressed();
        player.faceLeft();
        player.run();
    }

    @Override
    public void rightPressed() {
        super.rightPressed();
        player.faceRight();
        player.run();
    }
    
}
