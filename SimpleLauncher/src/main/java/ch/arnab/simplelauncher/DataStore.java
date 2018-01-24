package ch.arnab.simplelauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        if(adminPassword == null) {
            /// if no password is set, use 'admin' as password
            adminPassword = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
        }
        if(appList == null) {
            /// if no applist, array list is empty
            appList = new ArrayList<String>();
        }

    }
    String adminPassword;
    ArrayList<String> appList;


    /**
     * Hash string
     * @param message the string to be hashed
     * @return a hash of the message
     */
    public static String hashString(String message) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(message);
        return message;
    }

    /**
     * convert  Byte Array to Hex
     * @param arrayBytes array of bytes
     * @return hex characters
     */
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    /**
     * Saves all content of applist and password into preference
     * @param context context of the class that calls this function
     * @return return nothing
     */
    public String save(Context context){
        Set<String> set = new HashSet<String>(appList);
        String look = "saved";
        SharedPreferences launcher = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = launcher.edit();
        editor.putStringSet("appList", set);
        editor.putString("admin",adminPassword);
        editor.apply();
        return look;
    }

    /**
     * Load all of the content of the applist and password  from the preference
     * @param context context of the class that calls this function
     * @return return nothing
     */
    public String load(Context context){
        System.out.println("File is loaded");
        String look = "load";
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        adminPassword = settings.getString("admin", null);
        Set<String> set = settings.getStringSet("appList", null);
        if(adminPassword == null || adminPassword.isEmpty()){
            adminPassword = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
        }

        if(set == null || set.isEmpty()){
            System.out.println("list is not found");
            appList = new ArrayList<String>();
        }else {
            System.out.println("list is loaded");
            appList = new ArrayList<String>(set);
        }
        return look;
    }
}
