package ch.arnab.simplelauncher;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by atn01 on 10/25/2017.
 */

public class PowerButtonService extends Service {

    public PowerButtonService() {

    }

    /**
     * On create Detect Long Power Button Press and  block it
     */
    @Override
    public void onCreate() {
        super.onCreate();
        LinearLayout mLinear = new LinearLayout(getApplicationContext()) {

            //home or recent button
            public void onCloseSystemDialogs(String reason) {

                if ("globalactions".equals(reason)) {
                    Log.i("Key", "Long press on power button");
                    //Toast.makeText(getContext(), "You Pressed the Power Button", Toast.LENGTH_SHORT).show();
                    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(closeDialog);
                } else if ("homekey".equals(reason)) {
                    //home key pressed
                    Log.i("Key", "Home Key");
                } else if ("recentapps".equals(reason)) {
                    // recent apps button clicked
                    Log.i("Key", "Recent Button");
                    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(closeDialog);
                }
            }


        };


        mLinear.setFocusable(true);

        View mView = LayoutInflater.from(this).inflate(R.layout.service_layout, mLinear);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // params
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                100,
                100,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        wm.addView(mView, params);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

