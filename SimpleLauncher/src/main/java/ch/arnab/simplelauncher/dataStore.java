package ch.arnab.simplelauncher;

import java.util.ArrayList;

/**
 * Created by atn01 on 11/09/2017.
 */

class dataStore {
    private static final dataStore ourInstance = new dataStore();

    static dataStore getInstance() {
        return ourInstance;
    }

    private dataStore() {
        adminPassword = "admin";
        appList = new ArrayList<String>();
    }

    private String adminPassword;
    public ArrayList<String> appList;
}
