package com.atn010.lockeddownlauncher;

import android.graphics.drawable.Drawable;


/**
 * This function will hold the Application Info.
 * @author atn010
 */
public class AppInfo {
    public Drawable icon;
    public String applicationName;
    public String applicationPackage;
    public boolean isActive;

    public AppInfo() {
        super();
    }

    public AppInfo(Drawable icon, String applicationName, String applicationPackage, boolean isActive) {
        super();
        this.icon = icon;
        this.applicationName = applicationName;
        this.applicationPackage = applicationPackage;
        this.isActive = isActive;
    }
}


