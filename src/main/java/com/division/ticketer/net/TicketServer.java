package com.division.ticketer.net;


import com.division.ticketer.packet.Packet;
import com.division.ticketer.packet.Packet00KeepAlive;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evan on 1/25/2015.
 */
public class TicketServer implements Server{

    private ServerSocket mServerSocket;
    private Map<String,TicketUser> mConnectedUsers = new HashMap<>();
    private ArrayList<Packet> mPacketQueue = new ArrayList<>();
    private Map<String,Long> mPingMap = new HashMap<>();
    private boolean mRunning = true;
    private int mPort;

    @Override
    public void startServer(int port){
        mPort = port;
        try {
            mServerSocket = new ServerSocket();
        } catch (IOException ex){
        }
        Thread listenThread = new Thread(new ListenThread());
        listenThread.setDaemon(true);
        listenThread.start();
        Thread transmitThread = new Thread(new TransmitThread());
        transmitThread.setDaemon(true);
        transmitThread.start();
    }

    @Override
    public void queuePacket(Packet packet) {
        if(!mPacketQueue.contains(packet)){
            mPacketQueue.add(packet);
        }
    }

    @Override
    public boolean isUserConnected(ServerUser user) {
       return mConnectedUsers.containsKey(user.getUsername());
    }

    @Override
    public void regsiterUser(ServerUser user) {
        final TicketUser tUser = (TicketUser) user;
        if(!isUserConnected(tUser)){
            mConnectedUsers.put(tUser.getUsername(),tUser);
            mPingMap.put(tUser.getUsername(),System.currentTimeMillis());
            try {
                tUser.getConnection().setSoTimeout(300000);
            } catch (SocketException e) {
                //Swallow
            }
            startUserThread(tUser);
            System.out.println("Started user thread.");
        }
    }

    @Override
    public void deregisterUser(ServerUser user) {
        mConnectedUsers.remove(user.getUsername());
        mPingMap.remove(user.getUsername());
    }

    public void shutdown() {
        mRunning = false;
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class ListenThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            SocketAddress sockAddr;
            sockAddr = new InetSocketAddress("0.0.0.0", mPort);
            try {
                mServerSocket.bind(sockAddr);
                System.out.println("Listening for connections...");
                Socket tempSock;
                while (mRunning) {
                    if (mServerSocket.isBound()) {
                        tempSock = mServerSocket.accept();
                        if (tempSock.isConnected()) {
                            InputStream in = tempSock.getInputStream();
                            ObjectInputStream oin = new ObjectInputStream(in);
                            Object obj;
                            try {
                                obj = oin.readObject();
                                if (obj instanceof Packet) {
                                    Packet packet = (Packet) obj;
                                    packet.setSocket(tempSock);
                                    packet.setServer(TicketServer.this);
                                    System.out.print("Received Packet: "+packet);
                                    packet.executePacket();
                                } else {
                                    System.out.println("Not a packet.");
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startUserThread(TicketUser user){
        Thread userThread = new Thread(new UserThread(user));
        userThread.setDaemon(true);
        userThread.start();
    }

    public class UserThread implements Runnable {

        private TicketUser user;
        private Socket userSocket;

        public UserThread(TicketUser user) {
            this.user = user;
            userSocket = user.getConnection();
        }

        @Override
        public void run() {
            InputStream in = null;
            try {
                in = userSocket.getInputStream();
            } catch (IOException e) {
            }
            while (mRunning) {
                try {
                    ObjectInputStream oin = new ObjectInputStream(in);
                    Object obj = null;
                    try {
                        obj = oin.readObject();
                        if (obj instanceof Packet) {
                            Packet packet = (Packet) obj;
                            packet.setSocket(userSocket);
                            packet.setServer(TicketServer.this);
                            System.out.print("Received Packet: "+packet);
                            packet.executePacket();
                        } else {
                            System.out.println("Not a packet.");
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("No class found.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    deregisterUser(user);
                    break;
                }

            }
        }
    }

    public class TransmitThread implements Runnable {

        @Override
        public void run(){
            while (mRunning) {
                for(String user: mPingMap.keySet()){
                    long lastSent = mPingMap.get(user);
                    if(System.currentTimeMillis() - lastSent >= 45000){
                        mPacketQueue.add(new Packet00KeepAlive(mConnectedUsers.get(user)));
                        mPingMap.put(user, System.currentTimeMillis());
                        System.out.println("Added Keep alive.");
                    }
                }
                if (mPacketQueue.isEmpty()) {
                    try {
                        Thread.sleep(10);
                        continue;
                    } catch (InterruptedException e) {
                    }
                }
                Packet pack = mPacketQueue.get(0);
                if(pack == null) continue;
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(pack.getUser().getConnection().getOutputStream());
                    oos.writeObject(pack);
                    oos.flush();
                    System.out.println("Sent packet: "+pack);
                    mPacketQueue.remove(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    deregisterUser(pack.getUser());
                    mPacketQueue.remove(0);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
