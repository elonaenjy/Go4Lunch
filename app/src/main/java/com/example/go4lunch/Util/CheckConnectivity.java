package com.example.go4lunch.Util;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;


@SuppressWarnings("ALL")
public class CheckConnectivity {
    static boolean connected = true;

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!connected) {
            return false;
        } else {
            return true;
        }

    }
}
