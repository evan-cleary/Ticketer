package com.division.ticketer.datastructs;

/**
 * Created by Evan on 1/27/2015.
 */
public class Status {

    private int status_id;
    private String displayName;
    private boolean isSystem;
    private boolean isNotify;
    private boolean isClosed;
    private String notifyMsg;

    public Status(int status_id, String displayName, boolean isSystem, boolean isNotify, boolean isClosed, String notifyMsg) {
        this.status_id = status_id;
        this.displayName = displayName;
        this.isClosed = isClosed;
        this.isSystem = isSystem;
        this.isNotify = isNotify;
        this.notifyMsg = notifyMsg;
    }
}
