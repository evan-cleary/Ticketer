package com.division.ticketer.net;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Evan on 1/25/2015.
 */
public interface ServerUser extends Serializable {


    public String getUsername();

    public Socket getConnection();

    public void setConnection(Socket socket);

}
