package com.example.mostafaaboelnasr.attendancemanagment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class CheckNetworkState {
    public static Context contextx;

    public CheckNetworkState(Context context) {
        this.contextx = context;
    }

    public static boolean isConnect(Context context) {
        contextx = context;
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
        }
        return false;
    }
}
