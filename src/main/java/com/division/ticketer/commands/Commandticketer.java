/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.commands;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Commandticketer extends TicketerCommand {

    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatformat = TicketerConfig.getChatColor();
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length >= 4) {
                    if (args.length == 4) {
                        Ticketer.getAccounts().createUser(args[1], args[2], args[3], "");
                    } else {
                        Ticketer.getAccounts().createUser(args[1], args[2], args[3], args[4]);
                    }
                    sender.sendMessage(rankformat + ": " + chatformat + "Account: " + args[1] + " has been created.");
                    return;
                } else {
                    sender.sendMessage(rankformat + ": " + chatformat + "Incorrect number of arguements.");
                    sender.sendMessage(rankformat + ": " + chatformat + "/ticketer create [username] [password] [rank] <in-game>");
                    return;
                }
            }
            if (args[0].equalsIgnoreCase("ranks")) {
                ArrayList<String> ranks = Ticketer.getDatasource().getRanks();
                String s = "";
                for(String r:ranks){
                    if(s.length() > 0){
                        s+=", "+r;
                    } else {
                        s+=r;
                    }
                }
                sender.sendMessage(rankformat + ": " + chatformat + "Ranks: "+s);
            }
        } else {
            sender.sendMessage(rankformat + ": " + chatformat + "Incorrect number of arguements.");
        }
    }
}
