package ch.arnab.simplelauncher;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by atn01 on 11/10/2017.
 */

public class AppInfoAdapter extends ArrayAdapter<AppInfo> implements CompoundButton.OnCheckedChangeListener
{
    SparseBooleanArray mCheckStates;

    Context context;
    int layoutResourceId;
    AppInfo  data[] = null;

    public AppInfoAdapter(Context context, int layoutResourceId, AppInfo[] data){
        super(context, layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        mCheckStates = new SparseBooleanArray(data.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View row = convertView;
        AppInfoHolder holder= null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();

            holder.imgIcon = (ImageView) row.findViewById(R.id.app_icon);
            holder.txtTitle = (TextView) row.findViewById(R.id.app_name);
            holder.textPKGName = (TextView) row.findViewById(R.id.app_paackage);
            holder.chkSelect = (CheckBox) row.findViewById(R.id.cb_app);

            row.setTag(holder);

        }
        else{
            holder = (AppInfoHolder)row.getTag();
        }


        AppInfo appinfo = data[position];
        holder.txtTitle.setText(appinfo.applicationName);
        holder.textPKGName.setText(appinfo.applicationPackage);
        holder.imgIcon.setImageDrawable(appinfo.icon);
        //holder.chkSelect.setChecked(true);
        holder.chkSelect.setTag(position);
        holder.chkSelect.setChecked(mCheckStates.get(position, false));
        holder.chkSelect.setOnCheckedChangeListener(this);
        return row;

    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        mCheckStates.append((Integer) buttonView.getTag(), isChecked);

    }
    static class AppInfoHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView textPKGName;
        CheckBox chkSelect;

    }
}
