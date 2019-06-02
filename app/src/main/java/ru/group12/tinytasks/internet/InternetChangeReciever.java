package ru.group12.tinytasks.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class InternetChangeReciever extends BroadcastReceiver {

    /**
    Class for detecting when the internet has been lost.
     If there is no internet connection available, the user is sent to an activity that enables the user to check the internet connection.
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent networkStateIntent = new Intent("ru.group12.tinytasks.InternetChange");
        networkStateIntent.putExtra("isNetworkAvailable",  Network.isConnectedToInternet(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
    }
}