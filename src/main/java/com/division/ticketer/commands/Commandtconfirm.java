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
import java.net.Socket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Commandtconfirm extends TicketerCommand {

    @Override
    public void run(Ticketer TI, Player sender, String commandLabel, Command command, String[] args) {

        //ChatFormat stuff
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatformat = TicketerConfig.getChatColor();



        ConvoManager cManager = new ConvoManager();
        if (cManager.hasActiveConvo(sender)) {
            ActiveConvo ac = cManager.getActiveConvo(sender);
            if (!ac.isConfirmed()) {
                ac.setConfirmed(true);
                Socket sock = ac.getTUser().getSocket();
                TI.getNetFrame().sendToClient(sock, NetCase.CVOS, "");
                sender.sendMessage(rankformat + ": " + chatformat + " The GameMaster " + ac.getTUser().getUsername() + " has been notified of your confirmation.");
                sender.sendMessage(rankformat + ": " + chatformat + "To reply to ticket messages use /treply [message].");

            }
        }
    }
}
