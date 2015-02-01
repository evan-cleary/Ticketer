package com.division.ticketer.datastructs;

/**
 * Created by Evan on 1/27/2015.
 */
public class Ticket {

    private int ticket_id;
    private String player;
    private String subject;
    private String message;
    private int status_id;

    public Ticket(int ticket_id, String player, String subject, String message, int status_id) {
        this.ticket_id = ticket_id;
        this.player = player;
        this.subject = subject;
        this.message = message;
        this.status_id = status_id;
    }

}
