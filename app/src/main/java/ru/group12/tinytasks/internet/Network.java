package ru.group12.tinytasks.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import ru.group12.tinytasks.activities.MainActivity;
import ru.group12.tinytasks.popups.internet.InternetUnavailableScreen;

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
                    Intent intent2 = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent2);
                } else {
                    Intent intent2 = new Intent(activity, InternetUnavailableScreen.class);
                    activity.startActivity(intent2);
                }
            }
        }, intentFilter);
    }
}
