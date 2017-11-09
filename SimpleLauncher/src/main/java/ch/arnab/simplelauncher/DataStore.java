package ch.arnab.simplelauncher;

import java.util.ArrayList;

/**
 * Created by atn01 on 11/09/2017.
 */

class DataStore {
     static final DataStore ourInstance = new DataStore();

    static DataStore getInstance() {
        return ourInstance;
    }

    public DataStore() {

        adminPassword = "admin";
         appList = new ArrayList<>();

    }
    String adminPassword;
    ArrayList<String> appList;
}
