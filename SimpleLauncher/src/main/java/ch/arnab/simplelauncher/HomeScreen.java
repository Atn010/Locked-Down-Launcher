package ch.arnab.simplelauncher;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.EditText;

public class HomeScreen extends FragmentActivity {

    public final static int REQUEST_CODE = 10101;
    private static final int PRESS_INTERVAL = 2500;
    private long mUpKeyEventTime = 0;



    /**
     * Block illegal Keys in Launcher, Or Allow
     * @param event key event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_CAMERA
                || event.getKeyCode() == KeyEvent.KEYCODE_POWER
                || event.getKeyCode() == KeyEvent.KEYCODE_APP_SWITCH
                || event.getKeyCode() == KeyEvent.KEYCODE_3D_MODE
                || event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_UP
                || event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_DOWN
                || event.getKeyCode() == KeyEvent.KEYCODE_CALL
                || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            Log.i("Key", "keycode " + event.getKeyCode());
            return true;
        }else {
            Log.i("Key", "keycode " + event.getKeyCode());
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * Override the Back button on this activitys
     */
    @Override
    public void onBackPressed() {


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()) {
            if ((event.getEventTime() - mUpKeyEventTime) < PRESS_INTERVAL) {

                showAlertWindow();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true;
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_VOLUME_UP == keyCode){
            mUpKeyEventTime = event.getEventTime();
        }
        return true;
    }

    /**
     * This function detect the window change and close System Menu Dialog when detected
     * @param hasFocus the focus of the activity
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);

        }
        if(hasFocus){
            if(!isMyServiceRunning(KioskService.class)){
                startKioskService();
            }
            if(!isMyServiceRunning(PowerButtonService.class)){
                if (checkDrawOverlayPermission()) {
                    startService(new Intent(this, PowerButtonService.class));
                }
            }

        }
    }

    /**
     * On Create: Remove Login when close, start "blockStatusBarPullDown" function, start Kiosk Service and start PowerButtonService
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.homescreen);

        if (checkDrawOverlayPermission()) {
            startService(new Intent(this, PowerButtonService.class));
        }
        startKioskService();

        blockStatusBarPullDown();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    /**
     * Starts Kiosk Service
     */
    private void startKioskService() {
        startService(new Intent(this, KioskService.class));
    }


    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    /**
     * Check for  permission
     * @return
     */
    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    /**
     * Start Power Button Service if true
     * @param requestCode the request code for System Alert WIndow
     * @param resultCode the result, true or false
     * @param data the intent
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, PowerButtonService.class));
            }
        }
    }

    /**
     * Create an invisible Alert Window to block Status Bar Pull Down
     */
    private void blockStatusBarPullDown() {
        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        customViewGroup newView = new customViewGroup(this);

        manager.addView(newView, localLayoutParams);
    }



    public void showAlertWindow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Admin Password");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               tryToAccessAdminApplication(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void tryToAccessAdminApplication(String password){

        Log.i("Entered password", password);

        if(password.equals("admin") ) {
            Intent intent1 = new Intent(this, PowerButtonService.class);
            stopService(intent1);

            Intent intent2 = new Intent(this, KioskService.class);
            stopService(intent2);

            Log.i("Try to open", "Settings");
            openApp(this,"com.admin.admin");
        }


    }

    /** Open another app.
     * @param context current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                Log.i("Package", "Package Not Found" + packageName);
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
