package com.division.ticketer.packet;

import com.division.ticketer.core.Ticketer;
import com.division.ticketer.net.ServerUser;

/**
 * Created by Evan on 1/28/2015.
 */
public class Packet10GetStatuses extends Packet {

    static final long serialVersionUID = 1194810L;

    private String returnData;

    public Packet10GetStatuses(ServerUser user) {
        super(user);
    }

    @Override
    public void executePacket() {
        returnData = Ticketer.getDatasource().getStatuses();
        getServer().queuePacket(this);

    }
}
