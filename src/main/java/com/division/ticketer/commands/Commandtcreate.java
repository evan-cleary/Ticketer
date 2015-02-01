/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.commands;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.mysql.DataSource;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Commandtcreate extends TicketerCommand {

    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {
        DataSource DB = Ticketer.getDatasource();
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatformat = TicketerConfig.getChatColor();
        if (!DB.hasOpenTicket(sender)) {
            TI.addTicket(sender.getName());
            sender.sendMessage(rankformat + ": " + chatformat + "You have started a ticket. Follow these steps to submit.");
            sender.sendMessage(rankformat + ": " + chatformat + "1. Use /tsubject [subject] to set your subject.");
            sender.sendMessage(rankformat + ": " + chatformat + "2. Use /tmessage [message] to set your message.");
            sender.sendMessage(rankformat + ": " + chatformat + "3. Use /tappend [message] to add to your message.");
            sender.sendMessage(rankformat + ": " + chatformat + "4. (If needed) Use /treset to reset your message.");
            sender.sendMessage(rankformat + ": " + chatformat + "5. Use /tsubmit to submit your ticket.");
            sender.sendMessage(rankformat + ": " + chatformat + "6. Use /tcancel to cancel your ticket at any time.");
        } else {
            sender.sendMessage(rankformat + ": " + chatformat + "You already have an open ticket.");
        }
    }
}
