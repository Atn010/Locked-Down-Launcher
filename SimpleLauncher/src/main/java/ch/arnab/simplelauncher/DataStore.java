package ch.arnab.simplelauncher;

import java.util.ArrayList;

/**
 * Created by atn01 on 11/09/2017.
 */

class DataStore {
    private static DataStore ourInstance = null;

    public static DataStore getInstance() {
        if(ourInstance == null){
            ourInstance = new DataStore();
        }
        return ourInstance;
    }

    private DataStore() {
        adminPassword = "admin";
         appList = new ArrayList<>();
         appList.add("com.atn010.primaryapp");

    }
    String adminPassword;
    ArrayList<String> appList;
}
