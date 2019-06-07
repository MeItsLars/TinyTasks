package ru.group12.tinytasks.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.createtask.CreateTaskPart1Screen;
import ru.group12.tinytasks.popups.signin.SignInScreen;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;

// Fragment activity for creating tasks
public class CreateTaskFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_createtask_home, container, false);

        initializeContents();

        return inflatedView;
    }

    // Makes the button go to an activity to sign in if needed or go to the first create task activity.
    private void initializeContents() {
        checkTaskButtonState();
        Button createTaskButton = inflatedView.findViewById(R.id.create_task_or_sign_in_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Database.userSignedIn()) {
                    ActivityManager.startNewActivity(inflatedView.getContext(), CreateTaskPart1Screen.class, Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    ActivityManager.startNewActivity(inflatedView.getContext(), SignInScreen.class, Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
        });
    }

    // Check whether the user is signed in, and displays button text accordingly
    private void checkTaskButtonState() {
        Button taskOrLoginButton = inflatedView.findViewById(R.id.create_task_or_sign_in_button);
        taskOrLoginButton.setText(Database.userSignedIn() ? "Create new task" : "Sign up / Sign in");
    }

    public void showFragment() {
        checkTaskButtonState();
    }
}
