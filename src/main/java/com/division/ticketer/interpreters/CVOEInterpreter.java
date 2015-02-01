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
public class CVOEInterpreter extends NetInterpreter {

    public CVOEInterpreter() {
        super("CVOE");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {

        //Network declares
        String cleandata = data.replace(NetCase.CVOE.getNetCase(), "");
        ConvoManager cManager = new ConvoManager();
        Player player = netFrame.getInstance().getServer().getPlayer(cleandata);
        Connected_User cUser = netFrame.getConnectedUser(sock);

        //ChatFormat information
        String cUsername = cUser.getUsername();
        Rank cRank = Accounts.getRank(cUsername);
        String chatformat = TicketerConfig.getChatFormat();
        String rankformat = TicketerConfig.getRankFormat(cRank);
        ChatColor chatcolor = TicketerConfig.getChatColor();
        String formated = chatformat.replace("{RANK}", rankformat).replace("{NAME}", ChatColor.WHITE + cUsername);


        if (player != null) {
            if (cManager.hasActiveConvo(player) && cManager.getActiveConvo(player).getTUser() == cUser) {
                cManager.endConvo(cManager.getActiveConvo(player));
                player.sendMessage(formated + " " + chatcolor + " Thank you for your time " + player.getName() + " I hope I have been of service.");
                netFrame.sendToClient(sock, NetCase.CVOE, "Thank you for your time " + player.getName() + " I hope I have been of service.");
            }
        }
    }
}
