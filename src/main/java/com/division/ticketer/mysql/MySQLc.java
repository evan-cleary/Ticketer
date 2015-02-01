/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.mysql;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Ticketer;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Evan
 */
public class MySQLc extends MainSQL {

    Ticketer TI;

    public MySQLc(Ticketer instance) throws ClassNotFoundException, SQLException {
        super(instance);
        this.TI = instance;
        getConnection();
        setup();
    }

    final protected synchronized void getConnection()
            throws ClassNotFoundException, SQLException {
        TicketerConfig TC = TI.getTConfig();
        String host = TC.getMySQLHost();
        String username = TC.getMySQLUsername();
        String password = TC.getMySQLPassword();
        String databaseName = TC.getMySQLDatabase();
        String port = TC.getMySQLPort();
        Class.forName("com.mysql.jdbc.Driver");
        TI.getLogger().info("MySQL driver loaded");
        conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port
                + "/" + databaseName, username, password);
        TI.getLogger().info("Connected to Database");
    }

    final protected synchronized void setup() throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS `tickets` ("
                    + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "`player` varchar(255) NOT NULL,"
                    + "`subject` varchar(255) DEFAULT NULL,"
                    + "`message` text NOT NULL,"
                    + "`status` varchar(255) NOT NULL DEFAULT 'OPEN',"
                    + "`GMAssign` varchar(255) DEFAULT NULL,"
                    + "`flagged` int(11) NOT NULL DEFAULT '0',"
                    + "PRIMARY KEY (`id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");

        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
}
