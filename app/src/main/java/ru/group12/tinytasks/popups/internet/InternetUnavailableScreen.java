package ru.group12.tinytasks.popups.internet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.activities.MainActivity;

public class InternetUnavailableScreen extends AppCompatActivity {

    private InternetUnavailableScreen activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internetunavailable);

        activity = this;

        Button internetCheckButton = findViewById(R.id.internetCheckButton);
        internetCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}