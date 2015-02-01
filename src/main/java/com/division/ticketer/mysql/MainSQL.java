/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.mysql;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.ActiveTicket;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;

import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.division.ticketer.datastructs.Status;
import com.division.ticketer.datastructs.StatusList;
import com.division.ticketer.datastructs.Ticket;
import com.division.ticketer.datastructs.TicketList;
import com.division.ticketer.net.TicketUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public abstract class MainSQL implements DataSource {
    
    protected Connection conn;
    Ticketer TI;
    private GsonBuilder gsonBuilder;
    private Gson gson;
    
    protected abstract void getConnection() throws ClassNotFoundException,
            SQLException;
    
    protected abstract void setup() throws SQLException;
    
    public MainSQL(Ticketer instance) {
        this.TI = instance;
        gsonBuilder = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.VOLATILE, Modifier.TRANSIENT);
        gson = gsonBuilder.create();
    }

    //<editor-fold desc="Ticket Retrieval Methods">
    @Override
    public String getOpenTickets() {
        ResultSet rs = null;
        try (CallableStatement st = conn.prepareCall("{call ti_getOpenTickets}")){
            rs = st.executeQuery();
            if (rs != null) {
                return generateGson(generateTicketList(rs));
            }
        } catch (Exception ex) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return "";
    }
    
    @Override
    public String getAssignments(int user_id) {
        ResultSet rs = null;
        String output = "";
        try (CallableStatement st = conn.prepareCall("{call ti_getAssignedTickets(?)}")) {
            st.setInt(1, user_id);
            rs = st.executeQuery();
            if (rs != null) {
                return generateGson(generateTicketList(rs));
            }
        } catch (SQLException ex) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return null;
    }

    private TicketList generateTicketList(ResultSet rs) throws SQLException{
        ArrayList<Ticket> ticketList = new ArrayList<>();
        while (rs.next()) {
            //(int ticket_id, String player, String subject, String message, int status_id)
            ticketList.add(new Ticket(rs.getInt("id"),rs.getString("player"),rs.getString("subject"),rs.getString("message"),rs.getInt("status")));
        }
        return new TicketList(ticketList);
    }

    private String generateGson(Object object){
        String output = gson.toJson(object);
        System.out.print("Got Data: "+output);
        return output;
    }

    //</editor-fold>

    public String getStatuses(){
        ResultSet rs = null;
        try(CallableStatement cs = conn.prepareCall("{call ti_getStatuses}")){
            rs = cs.executeQuery();
            if(rs != null){
                ArrayList<Status> statuses = new ArrayList<>();
                while(rs.next()){
                    //(int status_id, String displayName, boolean isSystem, boolean isNotify, boolean isClosed, String notifyMsg)
                    statuses.add(new Status(rs.getInt("status_id"),rs.getString("display_name"),rs.getBoolean("isSystem"),rs.getBoolean("isNotify"),rs.getBoolean("isClosed"),rs.getString("notify_msg")));
                }
                StatusList statusList = new StatusList(statuses);
                return generateGson(statusList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    @Override
    public String getPlayer(int ticketid) {
        Statement st = null;
        ResultSet rs = null;
        String output = "";
        
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT player FROM tickets WHERE id=" + ticketid);
            if (rs != null) {
                while (rs.next()) {
                    if (!rs.getString("player").equals("")) {
                        output += rs.getString("player");
                    }
                }
                return output;
            }
        } catch (Exception ex) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return null;
    }
    
    @Override
    public String getMessage(int ticketid) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String output = "";
        try {
            st = conn.prepareStatement("SELECT message FROM tickets WHERE id=?");
            st.setInt(1,ticketid);
            rs = st.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (!rs.getString("message").equals("")) {
                        output += rs.getString("message");
                    }
                }
                return output;
            }
        } catch (Exception ex) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return null;
    }
    
    @Override
    public String getStatus(int ticketid) {
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT status FROM tickets WHERE id=" + ticketid);
            while (rs.next()) {
                if (!rs.getString("status").equals("")) {
                    return rs.getString("status");
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }
    
    @Override
    public String getFlagMsg(int ticketid) {
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT flag_msg FROM tickets WHERE id=" + ticketid);
            while (rs.next()) {
                if (!rs.getString("flag_msg").equals("")) {
                    System.out.print(rs.getString("flag_msg"));
                    return rs.getString("flag_msg");
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }
    
    @Override
    public boolean hasOpenTicket(Player p) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM tickets WHERE player='" + p.getName() + "' AND NOT status='CLOSED'");
            while (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Exception ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
        }
        return false;
    }
    
    @Override
    public void setState(int ticketid, String state) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("UPDATE tickets SET status=? WHERE id=?");
            pst.setString(1, state);
            pst.setInt(2, ticketid);
            pst.executeUpdate();
        } catch (SQLException ex) {
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    @Override
    public void setGMAssign(int ticketid, String gm) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("UPDATE tickets SET GMAssign=? WHERE id=?");
            pst.setString(1, gm);
            pst.setInt(2, ticketid);
            pst.executeUpdate();
        } catch (SQLException ex) {
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    @Override
    public void setFlag(int ticketid, String msg, int flag) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("UPDATE tickets SET flag=?,flag_msg=? WHERE id=?");
            pst.setInt(1, flag);
            pst.setString(2, msg);
            pst.setInt(3, ticketid);
            pst.executeUpdate();
        } catch (SQLException ex) {
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    @Override
    public boolean isFlagged(int ticketid) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement("SELECT flag FROM tickets WHERE id=?");
            pst.setInt(1, ticketid);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("flag") == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException ex) {
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return false;
    }
    
    @Override
    public String getFlagged() {
        Statement st = null;
        ResultSet rs = null;
        String output = "";
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT id FROM tickets WHERE NOT status='CLOSED' AND NOT flag=0");
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getInt("id") != -1) {
                        output += rs.getInt("id") + "%";
                    }
                }
                return output;
            }
        } catch (Exception ex) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return null;
    }
    
    @Override
    public void createTicket(ActiveTicket activeTicket) {
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatcolor = TicketerConfig.getChatColor();
        if (!hasOpenTicket(activeTicket.getPlayer())) {
            PreparedStatement pst = null;
            try {
                pst = conn.prepareStatement("INSERT INTO tickets (player,subject,message) VALUES(?,?,?)");
                String subject = activeTicket.getSubject();
                String message = activeTicket.getMessage();
                pst.setString(1, activeTicket.getPlayer().getName());
                pst.setString(2, subject);
                pst.setString(3, message);
                pst.executeUpdate();
                activeTicket.getPlayer().sendMessage(rankformat + ": " + chatcolor + "Your ticket has been submitted successfully.");
                TI.removeTicket(activeTicket);
            } catch (Exception ex) {
            } finally {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (Exception ex) {
                    }
                }
            }
        } else {
            activeTicket.getPlayer().sendMessage(rankformat + ": " + chatcolor + "You already have an open ticket.");
        }
    }
    
    @Override
    public int getTicketId(Player p) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT id FROM tickets WHERE player='" + p.getName() + "' AND NOT status='CLOSED'");
            while (rs.next()) {
                if (rs.getInt("id") != 0) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception ex) {
            return 0;
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Exception ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
        }
        return 0;
    }
    
    @Override
    public String getSubject(int ticketid) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement("SELECT subject FROM tickets WHERE id=?");
            pst.setInt(1, ticketid);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (!rs.getString("subject").equals("")) {
                    return rs.getString("subject");
                }
            }
            return null;
        } catch (Exception ex) {
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (Exception ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean createAccount(String username, String password, String ingame, String rank){
        try {
            CallableStatement cs;
            cs = conn.prepareCall("call ti_createAccount(?,?,?,?)");
            cs.setString(1, username);
            cs.setString(2, password);
            cs.setString(3, ingame);
            cs.setString(4, rank);
            cs.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    @Override
    public TicketUser loginCheck(String username, String password){
        try {
            CallableStatement cs;
            ResultSet rs;
            cs = conn.prepareCall("call ti_checkLogin(?,?)");
            cs.setString(1, username);
            cs.setString(2, password);
            rs = cs.executeQuery();
            while(rs.next()){
                if(rs.getInt("account_id") != -1){
                    return new TicketUser(rs.getInt("account_id"),rs.getString("username"),rs.getInt("rank_id"));
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(MainSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public ArrayList<String> getRanks(){
        ArrayList<String> ranks = new ArrayList<String>();
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT `name` FROM ranks");
            while(rs.next()){
                ranks.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ranks;
    }
    
    public Map<String,String> getRankFormat(int rank_id){
        Map<String,String> rankMap = new HashMap<String,String>();
        CallableStatement cs;
        ResultSet rs;
        try{
            cs = conn.prepareCall("CALL ti_getRankFormat(?)");
            cs.setInt(1,rank_id);
            rs = cs.executeQuery();
            while(rs.next()){
                rankMap.put("tag", rs.getString("tag"));
                rankMap.put("color_name", rs.getString("color_name"));
            }
        }catch(SQLException ex){
        }
        return rankMap;
    }
}
