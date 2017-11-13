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

public class MainActivity extends Activity {
    AppInfoAdapter adapter ;
    AppInfo app_info[] ;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList <String> appList = new ArrayList<>();


        final ListView listApplication = (ListView)findViewById(R.id.listApplication);
        Button b= (Button) findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                appList.clear();

                StringBuilder result = new StringBuilder();
                for(int i=0;i<adapter.mCheckStates.size();i++)
                {
                    if(adapter.mCheckStates.get(i))
                    {

                        result.append(app_info[i].applicationName);
                        result.append("\n");
                        appList.add(app_info[i].applicationName);
                    }

                }
                System.out.println(result);
                Log.i("Result", "onClick: " + printArrayList(appList));
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }

        });



        ApplicationInfo applicationInfo = getApplicationInfo();
        PackageManager pm = getPackageManager();
        List<PackageInfo> pInfo = new ArrayList<PackageInfo>();
        pInfo.addAll(pm.getInstalledPackages(0));
        app_info = new AppInfo[pInfo.size()];


        int counter = 0;
        for(PackageInfo item: pInfo){

            /*
            final PackageManager ACPM = getApplicationContext().getPackageManager();
            ApplicationInfo ai;
            try {
                ai = ACPM.getApplicationInfo( this.getPackageName(), 0);
                ai = ACPM.getApplicationInfo(getPackageName(),0);
            } catch (final Exception e) {
                ai = null;
            }
            final String applicationName = (String) (ai != null ? ACPM.getApplicationLabel(ai) : "(unknown)");
            */

            try{

                applicationInfo = pm.getApplicationInfo(item.packageName, 1);


                app_info[counter] = new AppInfo(pm.getApplicationIcon(applicationInfo),
                        String.valueOf(pm.getApplicationLabel(applicationInfo)), item.packageName );

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
