/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.ConvoManager;
import com.division.ticketer.core.Rank;
import com.division.ticketer.net.Connected_User;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class CVOSInterpreter extends NetInterpreter {

    public CVOSInterpreter() {
        super("CVOS");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        String cleandata = data.replace(NetCase.CVOS.getNetCase(), "");
        ConvoManager cManager = new ConvoManager();
        Connected_User cUser = netFrame.getConnectedUser(sock);
        String cUsername = cUser.getUsername();
        Rank cRank = Accounts.getRank(cUsername);
        Player player = netFrame.getInstance().getServer().getPlayer(cleandata);
        String chatformat = TicketerConfig.getChatFormat();
        String rankformat = TicketerConfig.getRankFormat(cRank);
        String systemformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatcolor = TicketerConfig.getChatColor();
        String formated = chatformat.replace("{RANK}", rankformat).replace("{NAME}", ChatColor.WHITE + cUsername);
        if (player != null) {
            if (!cManager.hasActiveConvo(cUser)) {
                cManager.initiateConvo(player, cUser);
                player.sendMessage(formated + " " + chatcolor + "Greetings " + player.getName() + ", I am GameMaster " + cUsername + " are you free to talk regarding your ticket?");
                player.sendMessage(systemformat + ": " + chatcolor + " use /tconfirm to accept invitation to talk.");
                netFrame.sendToClient(sock, NetCase.CVO, cUsername + "%: Greetings " + player.getName() + ", I am GameMaster " + cUsername + " are you free to talk regarding your ticket?");
            }
        } else {
            netFrame.sendToClient(sock, NetCase.CVO, "SYSTEM%Player is not online.");
        }
    }
}
