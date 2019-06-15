package com.nmks.matrimonyapp.utis;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {


    /**
     * Check Network
     *
     * @param context - Activity context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            } else {
                //Toast.makeText(context, Config.MSG_NO_NETWORK, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }


}
