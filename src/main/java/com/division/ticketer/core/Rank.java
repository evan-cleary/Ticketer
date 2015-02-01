/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.core;

/**
 *
 * @author Evan
 */
public enum Rank {

    SUPERUSER("superuser", 3),
    SYSTEM("system", 3),
    USER("user", 2),
    GUEST("guest", 1);
    private String rank;
    private int level;

    private Rank(String rankname, int level) {
        this.rank = rankname;
        this.level = level;
    }

    public String getRank() {
        return rank;
    }

    public int getLevel() {
        return level;
    }
}
