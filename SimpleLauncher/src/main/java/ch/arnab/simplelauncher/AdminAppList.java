package ch.arnab.simplelauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *  This is the main Admin Application List.
 *  This class will display a list of installed application in a phone.
 *  After selecting the Application to display, a toast is shown and the list of selected application is stored in Data Store.
 *  Return to Admin Menu in the previous fragment.
 * @author atn010
 */
public class AdminAppList extends Activity {
    AppInfoAdapter adapter ;
    AppInfo app_info[] ;
    @SuppressLint("WrongConstant")
    DataStore dataStore = DataStore.getInstance();

    private void completeList(){
        dataStore.save(this);
    }


    /**
     * This will load and display a list of Application.
     * A listener is created when clicking on button.
     * When a button is clicked, all the selected application will be stored.
     * A toast and log will display the application selected.
     * The Activity will return to Admin Menu.
     * @param savedInstanceState
     */
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList <String> appList = new ArrayList<String>();


        final ListView listApplication = (ListView)findViewById(R.id.listApplication);
        Button b= (Button) findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                appList.clear();

                StringBuilder result = new StringBuilder();
                for(int i=0;i<adapter.getCount();i++)
                {
                    if(adapter.mCheckStates.get(i))
                    {

                        result.append(app_info[i].applicationName);
                        result.append("\n");
                        appList.add(app_info[i].applicationPackage);
                    }

                }
                System.out.println(result);
                Log.i("Result", "onClick: " + printArrayList(appList));
                Toast.makeText(AdminAppList.this, result, Toast.LENGTH_SHORT).show();
                dataStore.appList.clear();
                dataStore.appList.addAll(appList);
                completeList();
                finish();
            }

        });



        ApplicationInfo applicationInfo = getApplicationInfo();
        PackageManager pm = getPackageManager();
        List<PackageInfo> pInfo = new ArrayList<PackageInfo>();
        pInfo.addAll(pm.getInstalledPackages(0));
        app_info = new AppInfo[pInfo.size()];


        int counter = 0;
        for(PackageInfo item: pInfo){

            try{

                applicationInfo = pm.getApplicationInfo(item.packageName, 1);
                if(applicationInfo==null){
                    break;
                }


                app_info[counter] = new AppInfo(pm.getApplicationIcon(applicationInfo),
                        String.valueOf(pm.getApplicationLabel(applicationInfo)), item.packageName,false );

                System.out.println(counter);

            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }

            counter++;
        }

        adapter = new AppInfoAdapter(this, R.layout.listview_item_row, app_info);
        listApplication.setAdapter(adapter);

    }

    /**
     * This is used to print the log
     * @param appList
     * @return
     */
    private String printArrayList(ArrayList<String> appList) {
        String listBuild = null;
        for(int i = 0; i<appList.size();i++){
            if(i == 0){
                listBuild = appList.get(i);
            }else {
                listBuild = listBuild + " | " + appList.get(i);
            }
        }
        return listBuild;
    }
}
