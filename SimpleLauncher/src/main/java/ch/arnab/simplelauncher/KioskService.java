package ch.arnab.simplelauncher;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Starts a KioskService.
 * May require configuration. Deletion is also possible.
 * @author atn01
 */
public class KioskService extends Service {

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2); // periodic interval to check in seconds -> 2 seconds
    private static final String TAG = KioskService.class.getSimpleName();

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    /**
     * On Destroying the Kiosk Service
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running =false;
        super.onDestroy();
    }

    /**
     * On Starting the Service
     *
     * @param intent
     * @param flags necessary flags
     * @param startId the ID
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'KioskService'");
        running = true;
        ctx = this;

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'KioskService'");
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }


    /**
     * handles Kiosk Mode
     */
    private void handleKioskMode() {
        // is Kiosk Mode active?
        if(PrefUtils.isKioskModeActive(ctx)) {
            // is App in background?
            if(isInBackground()) {
                restoreApp(); // restore!
            }
        }
    }

    /**
     * Check if the app is in backrground
     * @return
     */
    private boolean isInBackground() {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return (!ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName()));
    }

    /**
     * Restore app into the front
     */
    private void restoreApp() {
        // Restart activity
        Intent i = new Intent(ctx, HomeScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
