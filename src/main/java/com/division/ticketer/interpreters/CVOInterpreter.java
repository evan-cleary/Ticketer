/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.ConvoManager;
import com.division.ticketer.core.Rank;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class CVOInterpreter extends NetInterpreter {

    public CVOInterpreter() {
        super("CVO");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        String cleandata = data.replace(NetCase.CVO.getNetCase(), "");
        String[] delimit = cleandata.split("%");
        String chatformat = TicketerConfig.getChatFormat();
        String username = netFrame.getConnectedUser(sock).getUsername();
        Rank rank = Accounts.getRank(username);
        Player player = netFrame.getInstance().getServer().getPlayer(delimit[0]);
        ConvoManager cManager = new ConvoManager();

        if (player != null) {
            if (cManager.hasActiveConvo(player)) {
                if (cManager.getActiveConvo(player).isConfirmed()) {
                    String rankformat = TicketerConfig.getRankFormat(rank);
                    ChatColor chatcolor = TicketerConfig.getChatColor();
                    String formated = chatformat.replace("{RANK}", rankformat).replace("{NAME}", ChatColor.WHITE + Accounts.getInGame(username));
                    player.sendMessage(formated + " " + chatcolor + delimit[1]);
                } else {
                    netFrame.sendToClient(sock, NetCase.CVO, "SYSTEM%This user has not confirmed your conversation request.");
                }
            } else {
                netFrame.sendToClient(sock, NetCase.CVO, "SYSTEM%You do not have an Active Converstation with this person.");
            }
        } else{
            netFrame.sendToClient(sock, NetCase.CVO, "SYSTEM%Player is not online.");
        }
    }
}
