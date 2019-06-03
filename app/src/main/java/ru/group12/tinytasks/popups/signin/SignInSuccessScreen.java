package ru.group12.tinytasks.popups.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.activities.ProfileActivity;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;

public class SignInSuccessScreen extends AppCompatActivity {

    SignInSuccessScreen activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinsuccess);

        activity = this;

        TextView userFullName = findViewById(R.id.fullNameText);
        if(Database.getCurrentUser() != null) {
            userFullName.setText(Database.getCurrentUser().getFullName());
        }

        Button button = findViewById(R.id.myProfileButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivity(activity, ProfileActivity.class, true);
            }
        });
    }
}
