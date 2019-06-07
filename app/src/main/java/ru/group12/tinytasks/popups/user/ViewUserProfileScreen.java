package ru.group12.tinytasks.popups.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.objects.User;
import ru.group12.tinytasks.util.internet.Network;

public class ViewUserProfileScreen extends AppCompatActivity {

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Network.registerInternetStateChangedListener(this);

        user = getIntent().getParcelableExtra("user");

        initializeContents();
    }

    private void initializeContents() {
        TextView userName = findViewById(R.id.A_name_text_view);
        TextView birthDate = findViewById(R.id.A_birthdate_text_view);
        TextView email = findViewById(R.id.A_email_text_view);
        TextView phoneNumber = findViewById(R.id.A_phone_number_text_view);
        TextView gender = findViewById(R.id.A_gender_text_view);

        userName.setText(user.getFullName());
        birthDate.setText("Birth date: " + user.getBirthdate());
        email.setText("Email: " + user.getEmail());
        phoneNumber.setText("Phone number: " + user.getPhoneNumber());
        gender.setText("Gender: " + user.getGender());
    }
}
