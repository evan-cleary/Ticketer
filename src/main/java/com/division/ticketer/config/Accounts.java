/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.config;

import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.crypto.SHA1;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.division.ticketer.net.TicketUser;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Evan
 */
public class Accounts {

    private static YamlConfiguration acc = new YamlConfiguration();
    File aconfig;
    Ticketer TI;
    private final static int iterations = 1000;

    public Accounts(Ticketer instance) {
        aconfig = new File(instance.getDataFolder() + "/accounts.yml");
        this.TI = instance;
    }

    public void load() throws IOException {
        acc = YamlConfiguration.loadConfiguration(aconfig);
        if (!aconfig.exists()) {
            SHA1 sha1 = new SHA1();
            System.out.println("Creating Accounts database...");
            aconfig.createNewFile();
            acc.set("accounts", null);
            acc.set("accounts.admin.password", sha1.getHash(iterations, sha1.getHash(iterations, "admin" + "admin")));
            acc.set("accounts.admin.rank", "superuser");
            acc.set("accounts.admin.in-game", "admin");
            acc.save(aconfig);
        }
    }

    public static TicketUser verifiedUser(String username, String password) {
        SHA1 sha1 = new SHA1();
        return Ticketer.getDatasource().loginCheck(username, sha1.getHash(iterations, password));
        /*if (acc.contains("accounts." + username)) {
            if (acc.contains("accounts." + username + ".password")) {
                String sha1pass = sha1.getHash(iterations, password);
                if (sha1pass.equals(acc.getString("accounts." + username + ".password"))) {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }*/
    }

    public boolean createUser(String username, String password, String rank, String inGame) {
        SHA1 sha1 = new SHA1();
        return Ticketer.getDatasource().createAccount(username, sha1.getHash(iterations, sha1.getHash(iterations, password + username)), inGame,rank);
       /* if (!acc.contains("accounts." + username)) {
            acc.set("accounts." + username + ".password", sha1.getHash(iterations, sha1.getHash(iterations, password + username)));
            acc.set("accounts." + username + ".rank", rank);
            if (!inGame.equals("")) {
                acc.set("accounts." + username + ".in-game", inGame);
            } else {
                acc.set("accounts." + username + ".in-game", username);
            }
            try {
                acc.save(aconfig);
            } catch (IOException ex) {
                Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

    public static Rank getRank(String username) {
        for (Rank rank : Rank.values()) {
            if (rank.getRank().equalsIgnoreCase(acc.getString("accounts." + username + ".rank"))) {
                return rank;
            }
        }
        return null;
    }

    public static String getInGame(String username) {
        return acc.getString("accounts." + username + ".in-game", username);
    }

    public String getAccounts() {
        Set<String> accountList;
        accountList = acc.getConfigurationSection("accounts").getKeys(false);
        String output = "";
        for (String s : accountList) {
            output += s;
            output += "%";
        }
        return output;
    }
    
    public YamlConfiguration getAccountsFile(){
        return acc;
    }
}
