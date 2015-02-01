/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.net;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.interpreters.TicketerInterpreter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;

/**
 *
 * @author Evan
 */
public class Net_Framework extends Thread {

    private static String host;
    private static int port;
    private ServerSocket tbes;
    private static boolean active = true;
    private ArrayList<Connected_User> connected_users = new ArrayList<Connected_User>();
    Ticketer TI;
    TicketerConfig tcfg;
    Accounts acc;

    public Net_Framework(Ticketer instance) {
        this.TI = instance;
        this.tcfg = Ticketer.getTConfig();
        this.acc = Ticketer.getAccounts();
        host = tcfg.getServerHost();
        port = tcfg.getServerPort();
        System.out.println("[Ticketer] Ticketer back-end started on port: " + port);
        Thread keepAliveThread = new Thread(new keepAlive(this));
        keepAliveThread.setDaemon(active);
        keepAliveThread.setName("keepAlive");
        keepAliveThread.start();
    }

    public Ticketer getInstance() {
        return TI;
    }

    public void shutdownListener() {
        active = false;
        if (tbes == null) {
        } else {
            try {
                tbes.close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void run() {
        try {
            tbes = new ServerSocket();
            tbes.bind(new InetSocketAddress(host, port));
        } catch (Exception ex) {
            System.out.print("Unable to start Ticketer back-end");
        }

        int bytes;
        while (active) {
            try {
                Socket conn = tbes.accept();
                conn.setSoTimeout(1800000);
                boolean connected = false;
                for (Connected_User cu : connected_users) {
                    if (cu.getSocket() == conn) {
                        connected = true;
                        continue;
                    }
                }
                if (!connected) {
                    InputStream in = conn.getInputStream();
                    byte[] bytesRec = new byte[1024];
                    bytes = in.read(bytesRec, 0, bytesRec.length);
                    String data = new String(bytesRec, 0, bytes);
                    String rawCase = data.substring(0, 4);
                    NetCase netCase = getNetCase(rawCase);
                    try {
                        runInterpreter(netCase.name(), data, conn);
                    } catch (Exception ex) {
                        sendToClient(conn, NetCase.UKNC, null);
                    }
                }


            } catch (Exception ex) {
            }
        }
    }

    public class SpecListenThread implements Runnable {

        Socket specSocket;
        Net_Framework netFrame;
        Connected_User user;

        public SpecListenThread(Connected_User user, Net_Framework instance) {
            this.specSocket = user.getSocket();
            this.netFrame = instance;
            this.user = user;
        }

        @Override
        public void run() {
            InputStream in = null;
            String data;
            try {
                int bytes;
                in = specSocket.getInputStream();
                while (user.isConnected()) {
                    byte[] bytesRec = new byte[1024];
                    while ((bytes = in.read(bytesRec, 0, bytesRec.length)) != -1) {
                        data = new String(bytesRec, 0, bytes);
                        String rawCase = data.substring(0, 4);
                        NetCase netCase = getNetCase(rawCase);
                        try {
                            runInterpreter(netCase.name(), data, specSocket);
                        } catch (Exception ex) {
                            sendToClient(specSocket, NetCase.UKNC, null);
                        }

                    }
                }
            } catch (IOException ex) {
                try {
                    sendToClient(specSocket, NetCase.CTO, null);
                } catch (Exception ex2) {
                }
            } finally {
                netFrame.disconnectUser(user);
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Net_Framework.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public class keepAlive implements Runnable {

        Net_Framework netFrame;

        public keepAlive(Net_Framework netFrame) {
            this.netFrame = netFrame;
        }

        @Override
        public void run() {
            while (true) {
                for (Connected_User cUser : connected_users) {
                    netFrame.sendToClient(cUser.getSocket(), NetCase.PING, "");
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public void createSpecListen(Connected_User user) {
        Thread specListen = new Thread(new SpecListenThread(user, this));
        specListen.setDaemon(true);
        specListen.setName(user.getUsername());
        specListen.start();
    }

    public void setActive(boolean active) {
        Net_Framework.active = active;
    }

    public void closeConnection() {
        try {
            this.tbes.close();
        } catch (IOException ex) {
            Logger.getLogger(Net_Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendToClient(Socket sock, NetCase ncase, String data) {
        byte[] bytesSent = (ncase.getNetCase() + data).getBytes();
        try {
            OutputStream os = sock.getOutputStream();
            os.write(bytesSent);
            os.flush();
        } catch (IOException ex) {
        }

    }

    public void connectUser(Socket sock, String username) {
        connected_users.add(new Connected_User(username, sock));
        System.out.println("[Ticketer] User: " + username + " has connected to the Ticketer servers.");
    }

    public void disconnectUser(Connected_User user) {
        System.out.println("[Ticketer] User: " + user.getUsername() + " has disconnected from the Ticketer servers.");
        try {
            user.getSocket().close();
        } catch (Exception ex) {
        }
        connected_users.remove(user);
    }

    public Connected_User getConnectedUser(Socket sock) {
        for (Connected_User cuser : connected_users) {
            if (cuser.getSocket() == sock) {
                return cuser;
            }
        }
        return null;
    }

    public Connected_User getConnectedUser(String username) {
        for (Connected_User cuser : connected_users) {
            if (cuser.getUsername().equalsIgnoreCase(username)) {
                return cuser;
            }
        }
        return null;
    }

    public NetCase getNetCase(String ncase) {
        for (NetCase netcase : NetCase.values()) {
            if (netcase.getNetCase().equals(ncase)) {
                return netcase;
            }
        }
        return null;
    }

    public void runInterpreter(String inter, String data, Socket sock) {
        try {
            TicketerInterpreter tickint;
            String clazzpath = "com.division.ticketer.interpreters.";
            tickint = (TicketerInterpreter) Ticketer.class.getClassLoader().loadClass(clazzpath + inter + "Interpreter").newInstance();
            System.out.println(tickint.toString());
            tickint.run(data, sock, this);
        } catch (Exception ex) {
        }
    }

    public void sendNotifications(String title, String message) {
        for (Connected_User cu : connected_users) {
            sendToClient(cu.getSocket(), NetCase.NOTIF, title + "%" + message);
            runInterpreter("LRQ", "", cu.getSocket());
        }
    }

    public void notifyGreater(Rank rank, int ticketid, String msg) {
        for (Connected_User cUser : connected_users) {
            if (Accounts.getRank(cUser.getUsername()).getLevel() > rank.getLevel()) {
                runInterpreter("LRQ", "" + ticketid, cUser.getSocket());
                sendToClient(cUser.getSocket(), NetCase.UFLG, ticketid + "%" + msg);
            }
        }
    }

    public void massRefresh() {
        for (Connected_User cUser : connected_users) {
            runInterpreter("LRQ", "", cUser.getSocket());
        }
    }

    public void massDisconnect() {
        for (Connected_User cUser : connected_users) {
            sendToClient(cUser.getSocket(), NetCase.CTO, "");
            try {
                cUser.getSocket().close();
            } catch (Exception ex) {
            }
        }
    }

    public ArrayList<Connected_User> getConnectedUsers() {
        return connected_users;
    }
}
