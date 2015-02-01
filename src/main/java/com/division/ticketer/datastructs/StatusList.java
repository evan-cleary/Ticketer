package com.division.ticketer.datastructs;

import java.util.ArrayList;

/**
 * Created by Evan on 1/27/2015.
 */
public class StatusList {

    private ArrayList<Status> statusList;

    public StatusList(ArrayList<Status> statusList){
        this.statusList = statusList;
    }

    public ArrayList<Status> getStatusList() {
        return statusList;
    }
}
