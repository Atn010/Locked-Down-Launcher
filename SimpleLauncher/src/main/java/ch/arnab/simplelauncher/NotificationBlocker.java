package ch.arnab.simplelauncher;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Created by atn01 on 01/08/2018.
 */

@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationBlocker extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        cancelAllNotifications();
    }
}
