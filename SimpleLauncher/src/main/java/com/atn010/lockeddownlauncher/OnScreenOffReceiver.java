package com.atn010.lockeddownlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

/**
 * Receieve Off screen and force to turn on.
 * @author  atn010
 */

public class OnScreenOffReceiver extends BroadcastReceiver {

    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";



    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            AppContext ctx = (AppContext) context.getApplicationContext();

            if(isKioskModeActive(ctx)) {
                wakeUpDevice(ctx);
            }

        }
    }

    private void wakeUpDevice(AppContext context) {
        PowerManager.WakeLock wakeLock = context.getWakeLock(); // get WakeLock reference via AppContext
        if (wakeLock.isHeld()) {
            wakeLock.release(); // release old wake lock
        }

        // create a new wake lock...
        wakeLock.acquire();

        // ... and release again
        wakeLock.release();
    }

    private boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_KIOSK_MODE, false);
    }


}
