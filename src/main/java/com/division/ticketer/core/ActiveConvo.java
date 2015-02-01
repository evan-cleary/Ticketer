/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.core;

import com.division.ticketer.net.Connected_User;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class ActiveConvo {

    Player target;
    Connected_User tUser;
    boolean confirmed = false;

    public ActiveConvo(Player target, Connected_User ticketerUser) {
        this.target = target;
        this.tUser = ticketerUser;
    }

    public Connected_User getTUser() {
        return tUser;
    }

    public Player getTarget() {
        return target;
    }

    public void setConfirmed(boolean confirm) {
        confirmed = true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
