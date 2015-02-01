/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.commands;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.ActiveConvo;
import com.division.ticketer.core.ConvoManager;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.net.NetCase;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Commandtreply extends TicketerCommand {
    
    public String stringBuilder(String[] bits) {
        StringBuilder output = new StringBuilder();
        for (String s : bits) {
            output.append(s);
            output.append(" ");
        }
        return output.toString();
    }
    
    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        
        ConvoManager cManager = new ConvoManager();
        ChatColor chatcolor = TicketerConfig.getChatColor();
        if (cManager.hasActiveConvo(sender)) {
            ActiveConvo ac = cManager.getActiveConvo(sender);
            if (ac.getTUser().isConnected()) {
                TI.getNetFrame().sendToClient(ac.getTUser().getSocket(), NetCase.CVO, sender.getName() + "%" + stringBuilder(args));
                sender.sendMessage(ChatColor.GOLD + "{Player} " + sender.getName() + ": " + chatcolor + stringBuilder(args));
            } else {
                cManager.endConvo(ac);
                sender.sendMessage(rankformat + ": " + chatcolor + "Your GameMaster has disconnected suddenly.");
            }
        } else{
            sender.sendMessage(rankformat + ": " + chatcolor + "You do not have an active GM conversation.");
        }
    }
}
