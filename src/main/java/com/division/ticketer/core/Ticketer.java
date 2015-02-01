/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.core;

import com.division.ticketer.commands.TicketerCommand;
import com.division.ticketer.config.Accounts;
import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.mysql.DataSource;
import com.division.ticketer.mysql.MySQLc;
import com.division.ticketer.net.Net_Framework;

import java.io.IOException;
import java.util.ArrayList;

import com.division.ticketer.net.TicketServer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * @author Evan
 */
public class Ticketer extends JavaPlugin {

    public static Permission permission = null;
    private static boolean usingVault = false;
    private static TicketerConfig tcfg;
    private static Accounts acc;
    private static DataSource database;
    private static TicketServer ticketServer;
    private static Net_Framework netFrame;
    private ArrayList<ActiveTicket> newtickets = new ArrayList<ActiveTicket>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        tcfg = new TicketerConfig(this);
        tcfg.load();
        acc = new Accounts(this);
        try {
            acc.load();
        } catch (IOException ex) {
            System.out.println("[Ticketer] failed to load files...");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        ticketServer = new TicketServer();
        ticketServer.startServer(tcfg.getServerPort());

        try {
            database = new MySQLc(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Can't load database");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        if (!runningVault()) {
            System.out.println("[Ticketer] no vault detected using basic permissions.");
        } else {
            usingVault = true;
            setupPermissions();
        }
    }

    @Override
    public void onDisable() {
        ticketServer.shutdown();
        System.out.println("[Ticketer] has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return runTicketerCommand(sender, cmd, label, args, this);
    }

    public boolean runTicketerCommand(CommandSender sender, Command command, String label, String[] args, Ticketer tick) {
        ClassLoader CL = Ticketer.class.getClassLoader();
        String commandPath = "com.division.ticketer.commands.Command";
        String permPrefix = "ticketer.";
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player != null && !player.hasPermission(permPrefix + command.getName())) {
            player.sendMessage(ChatColor.RED + "You do not have the required permissions.");
            return true;
        }
        try {
            TicketerCommand cmd;
            cmd = (TicketerCommand) CL.loadClass(commandPath + command.getName()).newInstance();
            if (player == null) {
                sender.sendMessage("This command requires a player.");
            } else {
                cmd.run(tick, player, commandPath, cmd, args);
            }
            return true;
        } catch (Exception ex) {
            player.sendMessage(ChatColor.RED + "No such command.");
            return true;
        }
    }

    public static TicketerConfig getTConfig() {
        return tcfg;
    }

    public static Accounts getAccounts() {
        return acc;
    }

    public static DataSource getDatasource() {
        return database;
    }

    public void addTicket(String p) {

        newtickets.add(new ActiveTicket(p));

    }

    public void removeTicket(ActiveTicket tick) {
        newtickets.remove(tick);
    }

    public Net_Framework getNetFrame() {
        return netFrame;
    }

    public ActiveTicket getTicket(Player p) {
        for (ActiveTicket tick : newtickets) {
            if (tick.getPlayer() == p) {
                return tick;
            }
        }
        return null;
    }

    private boolean runningVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public Permission getPermissions() {
        return permission;
    }

    public boolean isUsingVault() {
        return usingVault;
    }
}
