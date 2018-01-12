package ch.arnab.simplelauncher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author atn010
 */

public class PowerButtonService extends Service {
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2); // periodic interval to check in seconds -> 2 seconds

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    public PowerButtonService() {

    }

    @Override
    public void onDestroy() {
        Log.i("PowerService", "Stopping service 'Power Service'");
        running =false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Power Service", "Starting service 'Power Button Service'");
        running = true;
        ctx = this;

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final View mView = createPowerBlocker();
        final WindowManager.LayoutParams params = param();
        final View pView = viewBlocker();
        final WindowManager.LayoutParams localLayoutParams = localLayoutParams();

        if(mView == null){
            Log.i("Power Service", "mView is Null");
        }

        if(params == null){
            Log.i("Power Service", "params is Null");
        }

        try {
            wm.addView(mView, params);
            wm.addView(pView,localLayoutParams);
        }catch (Exception e){
            e.printStackTrace();
        }

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i("Power Service", "Thread interrupted: 'Power'");
                    }
                }while(running);
                wm.removeView(mView);
                wm.removeView(pView);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    public View viewBlocker(){
        customViewGroup newView = new customViewGroup(this);
        return newView;
    }


    public WindowManager.LayoutParams localLayoutParams(){
        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // This blocks Screenshot
                WindowManager.LayoutParams.FLAG_SECURE |

                // This keeps the screen on
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        return localLayoutParams;
    }


    public WindowManager.LayoutParams param(){
        // params
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                100,
                100,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_SECURE
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;

        return params;
    }

    public View createPowerBlocker(){
        Log.i("PowerService", "Starting service 'Power Service'");
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
        return mView;
        //wm.addView(mView, params);

        //wm.removeViewImmediate(mView);
    }

    /**
     * On create Detect Long Power Button Press and  block it
     */
    @Override
    public void onCreate() {
        super.onCreate();

    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

