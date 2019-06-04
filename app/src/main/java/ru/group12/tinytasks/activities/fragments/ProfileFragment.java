package ru.group12.tinytasks.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.activities.MainActivity;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.User;

public class ProfileFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        /*TextView name = inflatedView.findViewById(R.id.tv1a);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(container.getContext(), MainActivity.class);
                container.getContext().startActivity(intent);
            }
        });

        //TextView surname = findViewById(R.id.tv2a);
        TextView phoneNumber = container.findViewById(R.id.tv3a);
        TextView birthDate = container.findViewById(R.id.tv4a);
        TextView gender = container.findViewById(R.id.tv5a);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            System.out.println("===============================");
            System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        if(Database.userSignedIn()) {
            User signedInUser = Database.getCurrentUser();

            name.setText(signedInUser.getName());
            //surname.setText(signedInUser.getSurname());
            phoneNumber.setText(signedInUser.getPhoneNumber());
            birthDate.setText(signedInUser.getBirthdate());
            gender.setText(signedInUser.getGender());
        }*/

        return inflatedView;
    }
}
