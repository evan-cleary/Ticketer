/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.permissions;

import com.division.ticketer.core.Ticketer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Evan
 */
public class PermissionsManager {

    private YamlConfiguration acc = null;

    public PermissionsManager() {
        acc = Ticketer.getAccounts().getAccountsFile();
    }

    public boolean hasPermission(String account, String node) {
        List<String> permList = null;
        permList = acc.getStringList("accounts." + account + ".permissions");
        for (String absNode : permList) {
            if (node.equals(absNode)) {
                return true;
            } else if (containsPerm(node, absNode)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPerm(String node, String absNode) {
        String[] absNodeBreak = absNode.split(".");
        if(absNodeBreak[0].equals("*")){
            return true;
        }
        if (absNodeBreak[absNodeBreak.length].equals("*")) {
            return absNode.contains(node);
        }
        return false;
    }

    private String rebuildNode(ArrayList<String> nodeBreak) {
        StringBuilder sb = new StringBuilder();
        for (String s : nodeBreak) {
            if (!s.equals("")) {
                if (sb.length() == 0) {
                    sb.append(s);
                } else {
                    sb.append(".");
                    sb.append(s);
                }
            }
        }
        return sb.toString();
    }
}
