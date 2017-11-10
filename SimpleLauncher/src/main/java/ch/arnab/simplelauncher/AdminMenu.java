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


public class AdminMenu extends Activity {
    DataStore dataStore = DataStore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

    }

    public void openSettings(View view){
        openApp(this,"com.android.settings");
    }

    public void openAppList(View view){

    }

    public void changeAdminPassword(View view){
        //showAlertWindow();
        changeAdminPasswordDialogBox();
    }


    public void showAlertWindow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Admin Password");

        final EditText previousAdminPassword = new EditText(this);
        previousAdminPassword.setHint("Previous Admin Password");
        previousAdminPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(previousAdminPassword);

        final EditText newAdminPassword = new EditText(this);
        newAdminPassword.setHint("Previous Admin Password");
        newAdminPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(newAdminPassword);

        final EditText confirmNewAdminPassword = new EditText(this);
        confirmNewAdminPassword.setHint("Previous Admin Password");
        confirmNewAdminPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(confirmNewAdminPassword);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changePassword(confirmNewAdminPassword.getText().toString(),newAdminPassword.getText().toString(),confirmNewAdminPassword.getText().toString());
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

    public void changeAdminPasswordDialogBox(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Change Admin Password");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        /*
        final EditText titleBox = new EditText(this);
        titleBox.setHint("Title");
        layout.addView(titleBox);

        final EditText descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
        layout.addView(descriptionBox);
        */

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


    public void changePassword(String previousAdminPassword, String newAdminPassword, String confirmedAdminPassword){

        String message = "";
        if(previousAdminPassword.equals(dataStore.adminPassword)){

            if(newAdminPassword.length()>=5){

                if(newAdminPassword.equals(confirmedAdminPassword)){
                    dataStore.adminPassword = newAdminPassword;
                    message = "successfully changed admin password";
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
