package ru.group12.tinytasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.User;

public class ProfileActivity extends AppCompatActivity {

    private ProfileActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = this;

        TextView name = findViewById(R.id.tv1a);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                finish();
            }
        });

        TextView surname = findViewById(R.id.tv2a);
        TextView phoneNumber = findViewById(R.id.tv3a);
        TextView birthDate = findViewById(R.id.tv4a);
        TextView gender = findViewById(R.id.tv5a);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            System.out.println("===============================");
            System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        if(Database.userSignedIn()) {
            User signedInUser = Database.getCurrentUser();

            name.setText(signedInUser.getName());
            surname.setText(signedInUser.getSurname());
            phoneNumber.setText(signedInUser.getPhoneNumber());
            birthDate.setText(signedInUser.getBirthdate());
            gender.setText(signedInUser.getGender());
        }
    }
}
