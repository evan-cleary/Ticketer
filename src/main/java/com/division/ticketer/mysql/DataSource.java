/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.mysql;

import com.division.ticketer.core.ActiveTicket;
import java.util.ArrayList;
import java.util.Map;

import com.division.ticketer.net.TicketUser;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public interface DataSource {

    public String getOpenTickets();

    public String getAssignments(int user_id);

    public String getPlayer(int ticketid);

    public String getMessage(int ticketid);

    public String getStatus(int ticketid);

    public void setState(int ticketid, String state);

    public void setGMAssign(int ticketid, String gm);

    public void createTicket(ActiveTicket activeTicket);

    public boolean hasOpenTicket(Player p);

    public int getTicketId(Player p);

    public String getSubject(int ticketid);

    public void setFlag(int ticketid, String msg, int flag);

    public boolean isFlagged(int ticketid);

    public String getFlagged();

    public String getFlagMsg(int ticketid);

    public String getStatuses();
    
    public boolean createAccount(String username,String password,String ingame,String rank);
    
    public TicketUser loginCheck(String username, String password);
    
    public ArrayList<String> getRanks();
    
    public Map<String,String> getRankFormat(int rank_id);
}
