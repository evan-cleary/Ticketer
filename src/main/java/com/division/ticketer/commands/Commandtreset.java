/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.commands;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.ActiveTicket;
import com.division.ticketer.core.Ticketer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Commandtreset extends TicketerCommand {

    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatformat = TicketerConfig.getChatColor();
        ActiveTicket tick = TI.getTicket(sender);
        if (tick != null) {
            tick.setMessage("");
            sender.sendMessage(rankformat + ": " + chatformat + "Your tickets message has been reset.");
        } else {
            sender.sendMessage(rankformat + ": " + chatformat + "You do not have a pending ticket. Use /create to start a ticket.");
        }
    }
}
