package com.division.ticketer.net;

import java.net.Socket;

/**
 * Created by Evan on 1/25/2015.
 */
public class TicketUser implements ServerUser {

    static final long serialVersionUID = 1194901L;

    private int mUserId;
    private String mUsername;
    private int mUserRank;
    private transient Socket mConnection;

    public TicketUser(int userId, String username, int userRank){
        this(userId,username,userRank,null);
    }

    public TicketUser(int userId, String username, int userRank, Socket socket) {
        mUserId = userId;
        mUsername = username;
        mUserRank = userRank;
        mConnection = socket;
    }

    public String getUsername() {
        return mUsername;
    }

    public int getUserRank() {
        return mUserRank;
    }

    public int getUserId() {
        return mUserId;
    }

    public Socket getConnection() {
        return mConnection;
    }

    @Override
    public void setConnection(Socket socket) {
        mConnection = socket;
    }
}
