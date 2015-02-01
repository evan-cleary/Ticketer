/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class Permissions {
    
    public static String METHOD;
    public static Ticketer TI;
    
    public enum Methods {
        
        VAULT, BASIC;
    }
    
    public Permissions(String method, Ticketer instance) {
        Permissions.METHOD = method;
        this.TI = instance;
    }
    
    public boolean isAuthorized(Player player, String perm) {
        
        switch (Methods.valueOf(METHOD)) {
            case VAULT:
                return TI.getPermissions().has(player, perm);
            case BASIC:
                player.hasPermission(perm);
                break;
        }
        return false;
    }
}
