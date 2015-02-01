/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.net.Connected_User;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import com.division.ticketer.permissions.PermissionsManager;
import java.net.Socket;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

/**
 *
 * @author Evan
 */
public class CMDInterpreter extends NetInterpreter {
    
    public CMDInterpreter() {
        super("CMD");
    }
    
    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        String cleandata = data.replace(NetCase.CMD.getNetCase(), "");
        String[] delimit = cleandata.split(" ");
        boolean success = false;
        String message = "";
        
        Connected_User cUser = netFrame.getConnectedUser(sock);
        PermissionsManager perMan = new PermissionsManager();
        PluginCommand cmd = Bukkit.getServer().getPluginCommand(delimit[0]);
        if (cmd != null) {
            String plugin = cmd.getPlugin().getDescription().getName();
            String permission = cmd.getPermission();
            if (plugin.equalsIgnoreCase("essentials")) {
                permission = "essentials." + delimit[0];
            }
            if (perMan.hasPermission(cUser.getUsername(), permission)) {
                success = Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cleandata);
                System.out.print("[Ticketer] User: " + cUser.getUsername() + " ran the command " + cleandata);
            } else{
                message = "You do not have the require permissions.";
            }
        } else{
            message = "Unknown command.";
        }
        netFrame.sendToClient(sock, NetCase.CMD, "" + success+"%"+message);
    }
}
