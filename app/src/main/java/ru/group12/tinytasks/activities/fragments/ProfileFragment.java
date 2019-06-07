package ru.group12.tinytasks.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.signin.SignInScreen;
import ru.group12.tinytasks.popups.tasks.MyTasksScreen;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.User;

public class ProfileFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        System.out.println(" ON CREATE CALLED ");

        initializeContents();

        return inflatedView;
    }

    private TextView userNameText;
    private TextView birthDateText;
    private TextView genderText;
    private TextView emailText;
    private TextView phoneNumberText;

    private void initializeContents() {
        setLayout();

        userNameText = inflatedView.findViewById(R.id.A_name_text_view);
        birthDateText = inflatedView.findViewById(R.id.A_birthdate_text_view);
        genderText = inflatedView.findViewById(R.id.A_gender_text_view);
        emailText = inflatedView.findViewById(R.id.A_email_text_view);
        phoneNumberText = inflatedView.findViewById(R.id.A_phone_number_text_view);

        Button signInButton = inflatedView.findViewById(R.id.B_profile_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivity(inflatedView.getContext(), SignInScreen.class, Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        Button signOutButton = inflatedView.findViewById(R.id.SignOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.signOutCurrentUser(getActivity());
                ActivityManager.backToHomeActivity(getActivity(), "Home");
            }
        });

        Button myTaskButton = inflatedView.findViewById(R.id.A_view_my_tasks);
        myTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivity(inflatedView.getContext(), MyTasksScreen.class, Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    private void setLayout() {
        ConstraintLayout profile_signed_in = inflatedView.findViewById(R.id.A_profile);
        ConstraintLayout profile_not_signed_in = inflatedView.findViewById(R.id.B_profile);
        if(Database.userSignedIn()) {
            profile_signed_in.setVisibility(View.VISIBLE);
            profile_signed_in.setClickable(true);
            profile_not_signed_in.setVisibility(View.INVISIBLE);
            profile_not_signed_in.setClickable(false);
        } else {
            profile_signed_in.setVisibility(View.INVISIBLE);
            profile_signed_in.setClickable(false);
            profile_not_signed_in.setVisibility(View.VISIBLE);
            profile_not_signed_in.setClickable(true);
        }
    }

    private void setUserData() {
        try {
            User user = Database.getCurrentUser();

            userNameText.setText(user.getFullName());
            birthDateText.setText("Birth date: " + user.getBirthdate());
            genderText.setText("Gender: " + user.getGender());
            emailText.setText("Email: " + user.getEmail());
            phoneNumberText.setText("Phone number: " + user.getPhoneNumber());
        } catch(Exception e) {
        }
    }

    public void showFragment() {
        if(inflatedView != null) setLayout();
        if(Database.getCurrentUser() != null) setUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        showFragment();
    }
}
