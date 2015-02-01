/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.commands;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.ActiveTicket;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.mysql.DataSource;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Commandtsubmit extends TicketerCommand {

    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {
        ActiveTicket tick = TI.getTicket(sender);
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatformat = TicketerConfig.getChatColor();
        if (tick.getMessage().equals("")) {
            sender.sendMessage(rankformat + ": " + chatformat + "You need to set a message with /message.");
            return;
        }
        if (tick.getSubject().equals("")) {
            sender.sendMessage(rankformat + ": " + chatformat + "You need to set a subject with /subject.");
            return;
        }
        DataSource DB = Ticketer.getDatasource();
        DB.createTicket(tick);
        TI.getNetFrame().sendNotifications("New Ticket", "A new ticket has arrived.");
    }
}
