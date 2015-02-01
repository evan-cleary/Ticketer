/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.crypto;

import com.division.ticketer.config.Accounts;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evan
 */
public class SHA1 {

    public String getHash(int iterationNB, String phrase) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(phrase.getBytes());
            for (int i = 0; i < iterationNB; i++) {
                digest = sha1.digest(digest);
            }
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String bytesToHex(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;

    }
}
