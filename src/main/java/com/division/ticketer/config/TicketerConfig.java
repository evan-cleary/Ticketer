/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.config;

import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Evan
 */
public class TicketerConfig {

    YamlConfiguration tcfg = new YamlConfiguration();
    File tconfig;
    Ticketer TI;
    boolean changed = false;
    private static String host;
    private static int port;
    private static String mysqlhost;
    private static String mysqlport;
    private static String mysqluser;
    private static String mysqlpass;
    private static String mysqldata;
    private static String chatformat;
    private static ChatColor chatcolor;
    private static String sutag;
    private static ChatColor sucolor;
    private static String sytag;
    private static ChatColor sycolor;
    private static String ustag;
    private static ChatColor uscolor;
    private static String gutag;
    private static ChatColor gucolor;

    public TicketerConfig(Ticketer instance) {
        tconfig = new File(instance.getDataFolder() + "/config.yml");
        this.TI = instance;
    }

    public void load() {
        changed = false;
        System.out.println("[Ticketer] Loading config...");
        try {
            tcfg.load(tconfig);
        } catch (Exception ex) {
            System.out.println("[Ticketer] Generating config...");
        }
        if (!tcfg.contains("general.chat.format")) {
            tcfg.set("general.chat.format", "{RANK} {NAME}:");
            changed = true;
        } else {
            chatformat = tcfg.getString("general.chat.format");
        }
        if (!tcfg.contains("general.chat.color")) {
            tcfg.set("general.chat.color", "LIGHT_PURPLE");
            changed = true;
        } else {
            chatcolor = ChatColor.valueOf(tcfg.getString("general.chat.color"));
        }
        if (!tcfg.contains("general.superuser.tag")) {
            tcfg.set("general.superuser.tag", "{Admin}");
            changed = true;
        } else {
            sutag = tcfg.getString("general.superuser.tag");
        }
        if (!tcfg.contains("general.superuser.color")) {
            tcfg.set("general.superuser.color", "DARK_RED");
            changed = true;
        } else {
            sucolor = ChatColor.valueOf(tcfg.getString("general.superuser.color", "DARK_RED"));
        }
        if (!tcfg.contains("general.system.tag")) {
            tcfg.set("general.system.tag", "{Ticketer}");
            changed = true;
        } else {
            sytag = tcfg.getString("general.system.tag");
        }
        if (!tcfg.contains("general.system.color")) {
            tcfg.set("general.system.color", "DARK_GRAY");
            changed = true;
        } else {
            sycolor = ChatColor.valueOf(tcfg.getString("general.system.color", "DARK_GRAY"));
        }
        if (!tcfg.contains("general.user.tag")) {
            tcfg.set("general.user.tag", "{GM}");
            changed = true;
        } else {
            ustag = tcfg.getString("general.user.tag");
        }
        if (!tcfg.contains("general.user.color")) {
            tcfg.set("general.user.color", "GREEN");
            changed = true;
        } else {
            uscolor = ChatColor.valueOf(tcfg.getString("general.user.color", "GREEN"));
        }
        if (!tcfg.contains("general.guest.tag")) {
            tcfg.set("general.guest.tag", "{Trainee}");
            changed = true;
        } else {
            gutag = tcfg.getString("general.guest.tag");
        }
        if (!tcfg.contains("general.guest.color")) {
            tcfg.set("general.guest.color", "AQUA");
            changed = true;
        } else {
            gucolor = ChatColor.valueOf(tcfg.getString("general.guest.color", "AQUA"));
        }
        if (!tcfg.contains("server.host")) {
            tcfg.set("server.host", "0.0.0.0");
            changed = true;
        } else {
            host = tcfg.getString("server.host");
        }
        if (!tcfg.contains("server.port")) {
            tcfg.set("server.port", 9184);
            changed = true;
        } else {
            port = tcfg.getInt("server.port");
        }
        if (!tcfg.contains("mysql.host")) {
            tcfg.set("mysql.host", "localhost");
            changed = true;
        } else {
            mysqlhost = tcfg.getString("mysql.host");
        }
        if (!tcfg.contains("mysql.port")) {
            tcfg.set("mysql.port", "3306");
            changed = true;
        } else {
            mysqlport = tcfg.getString("mysql.port");
        }
        if (!tcfg.contains("mysql.username")) {
            tcfg.set("mysql.username", "root");
            changed = true;
        } else {
            mysqluser = tcfg.getString("mysql.username");
        }
        if (!tcfg.contains("mysql.password")) {
            tcfg.set("mysql.password", "password");
            changed = true;
        } else {
            mysqlpass = tcfg.getString("mysql.password");
        }
        if (!tcfg.contains("mysql.database")) {
            tcfg.set("mysql.database", "ticketer");
            changed = true;
        } else {
            mysqldata = tcfg.getString("mysql.database");
        }
        if (!tconfig.exists() || changed == true) {
            try {
                tcfg.save(tconfig);
                load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("[Ticketer] Done loading config!");
        }
    }

    public String getMySQLDatabase() {
        return mysqldata;
    }

    public String getMySQLPassword() {
        return mysqlpass;
    }

    public String getMySQLUsername() {
        return mysqluser;
    }

    public String getMySQLHost() {
        return mysqlhost;
    }

    public String getMySQLPort() {
        return mysqlport;
    }

    public String getServerHost() {
        return host;
    }

    public int getServerPort() {
        return port;
    }

    public static String getChatFormat() {
        return chatformat;
    }

    public static String getRankFormat(Rank rank) {
        try {
            switch (rank) {
                case SUPERUSER:
                    return sucolor + sutag;
                case SYSTEM:
                    return sycolor + sytag;
                case USER:
                    return uscolor + ustag;
                case GUEST:
                    return gucolor + gutag;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ChatColor getChatColor() {
        return chatcolor;
    }
}