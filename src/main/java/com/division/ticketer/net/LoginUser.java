package com.division.ticketer.net;


import java.net.Socket;

/**
 * Created by Evan on 1/26/2015.
 */
public class LoginUser implements ServerUser {

    static final long serialVersionUID = 1194900L;

    private String username;
    private String password;

    public LoginUser(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Socket getConnection() {
        return null;
    }

    @Override
    public void setConnection(Socket socket) {

    }
}
