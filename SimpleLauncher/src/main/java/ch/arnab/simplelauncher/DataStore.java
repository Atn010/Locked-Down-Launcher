package ch.arnab.simplelauncher;

import java.util.ArrayList;

/**
 * This Singleton store Applist and Admin Password
 * @author atn010
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
    }
    String adminPassword;
    ArrayList<String> appList;
}
