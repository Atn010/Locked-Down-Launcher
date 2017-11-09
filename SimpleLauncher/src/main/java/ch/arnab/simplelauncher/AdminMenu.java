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

public class AdminMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
    }

    public void openSettings(View view){
        openApp(this,"com.admin.admin");
    }

    public void openAppList(View view){

    }

    public void changeAdminPassword(View view){

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
                changePassword(input.getText().toString(),input.getText().toString(),input.getText().toString());
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

    public void changePassword(String previousAdminPassword, String newAdminPassword, String confirmedAdminPassword){

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
