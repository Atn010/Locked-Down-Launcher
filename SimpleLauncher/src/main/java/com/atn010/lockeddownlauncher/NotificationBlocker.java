package com.atn010.lockeddownlauncher;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Created by atn01 on 01/08/2018.
 */


public class NotificationBlocker extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Blocker running");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("Stopped ");
        cancelAllNotifications();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
