package ch.arnab.simplelauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atn010.lockeddownlauncher.R;

/**
 * This a menu Activity which display 3 menu button which:
 * Open Android Settings, Change Application Display List, and Open Change Password Window.
 * Change Password will be displayed as an alert window. A toast will be displayed in cased of error.
 * @author atn010
 */
public class AdminMenu extends Activity {
    DataStore dataStore = DataStore.getInstance();


    /**
     * This is on create command
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

    }

    /**
     * This opens the Android Settings
     * @param view
     */
    public void openSettings(View view){
        openApp(this,"com.android.settings");
    }

    /**
     * This open the Application List
     * @param view
     */
    public void openAppList(View view){
    startActivity(new Intent(this, AdminAppList.class));

    }

    /**
     * This shows the Change Admin Password Dialog.
     * @param view
     */
    public void changeAdminPassword(View view){
        //showAlertWindow();
        changeAdminPasswordDialogBox();
    }

    /**
     * This build and show Change Admin Password Dialog.
     * A standard square box with title and ok cancel button.
     * Inside the square is 3 text box for: Input Previous Password, New Password, and Confirm Password.
     * A hint is provided.
     * When Cancel, it will close the dialog with no action.
     * When OK, changeAdminPassword is run to check if it fits the condition.
     *
     */
    public void changeAdminPasswordDialogBox(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Change Admin Password");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText previousAdminPassword = new EditText(this);
        previousAdminPassword.setHint("Input Previous Admin Password");
        previousAdminPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(previousAdminPassword);

        final EditText newAdminPassword = new EditText(this);
        newAdminPassword.setHint("Input New Admin Password");
        newAdminPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newAdminPassword);

        final EditText confirmNewAdminPassword = new EditText(this);
        confirmNewAdminPassword.setHint("Confirm New Admin Password");
        confirmNewAdminPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(confirmNewAdminPassword);

        // Set up the buttons
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changePassword(previousAdminPassword.getText().toString(),newAdminPassword.getText().toString(),confirmNewAdminPassword.getText().toString());
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        dialog.setView(layout);
        dialog.show();
    }


    /**
     * This function will change password.
     * Failure and Success will activate a toast which will show the message.
     * @param previousAdminPassword The old Admin Password.
     * @param newAdminPassword The new Admin Password.
     * @param confirmedAdminPassword The Re-enteredd Admin Password to confirm.
     */
    public void changePassword(String previousAdminPassword, String newAdminPassword, String confirmedAdminPassword){

        String message = "";
        previousAdminPassword = DataStore.hashString(previousAdminPassword);
        if(previousAdminPassword.equals(dataStore.adminPassword)){

            if(newAdminPassword.length()>=5){

                if(newAdminPassword.equals(confirmedAdminPassword)){
                    dataStore.adminPassword = DataStore.hashString(newAdminPassword);
                    message = "successfully changed admin password";
                    dataStore.save(this);
                }else {
                    message = "new and confirm admin password does not match";
                }

            }else{
                message = "incorrect new password length";
            }


        }else{
            message = "incorrect previous password";
        }

        toastInformation(message);

    }

    /**
     * This will show a toast with the message.
     * @param information this is the message to show
     */
    public void toastInformation(String information){
        Toast.makeText(this,information ,Toast.LENGTH_SHORT).show();
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
