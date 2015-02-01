/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.core;

import com.division.ticketer.net.Connected_User;
import java.util.ArrayList;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class ConvoManager {

    private static ArrayList<ActiveConvo> aclist = new ArrayList<ActiveConvo>();

    public ConvoManager() {
    }

    public void initiateConvo(Player player, Connected_User cUser) {
        aclist.add(new ActiveConvo(player, cUser));
    }

    public void endConvo(ActiveConvo ac) {
        aclist.remove(ac);
    }

    public boolean hasActiveConvo(Player player) {
        if (getActiveConvo(player) != null) {
            return true;
        }
        return false;
    }

    public boolean hasActiveConvo(Connected_User cUser) {
        if (getActiveConvo(cUser) != null) {
            return true;
        }
        return false;
    }

    public ActiveConvo getActiveConvo(Connected_User cUser) {
        for (ActiveConvo ac : aclist) {
            if (ac.getTUser() == cUser) {
                return ac;
            }
        }

        return null;
    }

    public ActiveConvo getActiveConvo(Player player) {
        for (ActiveConvo ac : aclist) {
            if (ac.getTarget() == player) {
                return ac;
            }
        }
        return null;
    }
}
