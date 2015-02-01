/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.commands;

import com.division.ticketer.core.Ticketer;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public interface Command {

    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args);
}
