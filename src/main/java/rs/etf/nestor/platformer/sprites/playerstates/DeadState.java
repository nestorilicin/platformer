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
public class DeadState extends State {

    @Override
    public char indState() {
        return 'D';
    }

    public DeadState(Player player) {
        super(player);
    }
    
}
