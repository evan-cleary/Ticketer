package com.division.ticketer.packet;

import com.division.ticketer.core.Ticketer;
import com.division.ticketer.net.TicketUser;

/**
 * Created by Evan on 1/27/2015.
 */
public class Packet20RequestOpenTickets extends Packet{

    static final long serialVersionUID = 1194820L;

    private TicketUser ticketUser;
    private String openTickets;
    private String assignedTickets;

    public Packet20RequestOpenTickets(TicketUser user) {
        super(user);
    }

    @Override
    public void executePacket() {
        openTickets = Ticketer.getDatasource().getOpenTickets();
        TicketUser user = (TicketUser) getUser();
        assignedTickets = Ticketer.getDatasource().getAssignments(user.getUserId());
        getServer().queuePacket(this);
    }
}
