package ru.group12.tinytasks.popups.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.internet.Network;

public class SignInSuccessScreen extends AppCompatActivity {

    /*
    When a user successfully signs in they are sent to this activity.
    Their username is displayed and they get the option between going to the homescreen and their profile.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinsuccess);
        Network.registerInternetStateChangedListener(this);

        TextView userFullName = findViewById(R.id.fullNameText);
        if(Database.getCurrentUser() != null) {
            userFullName.setText(Database.getCurrentUser().getFullName());
        }

        Button profileButton = findViewById(R.id.myProfileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                ActivityManager.backToHomeActivity(SignInSuccessScreen.this, "Profile");
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        ActivityManager.backToHomeActivity(this, "Profile");
    }
}
