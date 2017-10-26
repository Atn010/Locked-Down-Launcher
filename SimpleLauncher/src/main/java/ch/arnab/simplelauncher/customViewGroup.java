package ch.arnab.simplelauncher;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by atn01 on 10/18/2017.
 */

public class customViewGroup extends ViewGroup {
    public customViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v("Statusbar Pulldown", "Intercepted");
        return true;
    }
}
