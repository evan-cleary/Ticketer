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
public class Commandtcancel extends TicketerCommand {

    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {
        DataSource DB = Ticketer.getDatasource();
        ActiveTicket tick = TI.getTicket(sender);
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatformat = TicketerConfig.getChatColor();
        if (tick != null) {
            TI.removeTicket(tick);
            sender.sendMessage(rankformat + ": " + chatformat + "Your ticket has been canceled.");
        } else {
            if (DB.hasOpenTicket(sender)) {
                int ticketid = DB.getTicketId(sender);
                if (!DB.getStatus(ticketid).equals("ACTIVE") && !DB.getStatus(ticketid).equals("PENDING")) {
                    DB.setState(ticketid, "CLOSED");
                    sender.sendMessage(rankformat + ": " + chatformat + "Your ticket has been canceled.");
                    TI.getNetFrame().massRefresh();
                } else {
                    sender.sendMessage(rankformat + ": " + chatformat + "You cannot cancel an active/pending ticket.");
                }
            } else {
                sender.sendMessage(rankformat + ": " + chatformat + "You do not have a pending ticket. Use /tcreate to start a ticket.");
            }
        }
    }
}
