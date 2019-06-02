package ru.group12.tinytasks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.database.Database;
import ru.group12.tinytasks.database.objects.User;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView name = findViewById(R.id.tv1a);
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
