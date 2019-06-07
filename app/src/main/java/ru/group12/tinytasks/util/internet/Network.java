package ru.group12.tinytasks.util.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import ru.group12.tinytasks.popups.internet.InternetUnavailableScreen;
import ru.group12.tinytasks.util.ActivityManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Network {
    public static boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void registerInternetStateChangedListener(final AppCompatActivity activity) {
        if(!isConnectedToInternet(activity)) {
            Intent intent = new Intent(activity, InternetUnavailableScreen.class);
            activity.startActivity(intent);
        }

        IntentFilter intentFilter = new IntentFilter("ru.group12.tinytasks.InternetChange");
        LocalBroadcastManager.getInstance(activity).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getBooleanExtra("isNetworkAvailable", false)) {
                    ActivityManager.backToHomeActivity(activity, "Home");
                } else {
                    ActivityManager.startNewActivity(activity, InternetUnavailableScreen.class);
                }
            }
        }, intentFilter);
    }
}
