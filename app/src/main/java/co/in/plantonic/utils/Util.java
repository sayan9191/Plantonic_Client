package co.in.plantonic.utils;

import android.app.Activity;
import android.app.Fragment;

public class Util {

    protected int getFragmentCount(Activity activity) {
        return activity.getFragmentManager().getBackStackEntryCount();
    }

    public Fragment getFragmentAt(Activity activity, int index) {
        return getFragmentCount(activity) > 0 ? activity.getFragmentManager().findFragmentByTag(Integer.toString(index)) : null;
    }
}
