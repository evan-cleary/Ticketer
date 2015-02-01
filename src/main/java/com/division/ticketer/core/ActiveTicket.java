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
public class ActiveTicket {

    private String subject;
    private String message;
    private String player;

    public ActiveTicket(String p) {
        this.player = p;
    }

    public Player getPlayer() {
        return Bukkit.getPlayerExact(player);
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void setPlayer(String p) {
        player = p;
    }

    public boolean appendMessage(String append) {
        if (!message.equals("")) {
            message += (" " + append);
            return true;
        } else {
            setMessage(append);
            return true;
        }
    }

    public void setSubject(String sub) {
        subject = sub;
    }
}
