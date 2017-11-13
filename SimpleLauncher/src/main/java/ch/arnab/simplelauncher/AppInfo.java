package ch.arnab.simplelauncher;

import android.graphics.drawable.Drawable;

/**
 * Created by atn01 on 11/10/2017.
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


