package ru.group12.tinytasks.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.NotificationsManager;

// Fragment activity for the notifications screen
public class NotificationsFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_notifications, container, false);

        initializeContents();
        retrieveNotifications();
        return inflatedView;
    }

    private LinearLayout layout;

    private ConstraintLayout taskWaitingLayout;
    private TextView searchingStateText;

    // Method for initializing important views
    private void initializeContents() {
        layout = inflatedView.findViewById(R.id.notifications_list);
        taskWaitingLayout = inflatedView.findViewById(R.id.task_waiting_layout);
        searchingStateText = inflatedView.findViewById(R.id.task_searching_state);
    }

    // Method for retrieving notifications from the database
    private void retrieveNotifications() {
        layout.removeAllViewsInLayout();
        startSearching();
        if(Database.getCurrentUser() != null) {
            NotificationsManager.updateLatestNotifications(this, layout, Database.getCurrentUser().getUid(), 10);
        } else finishSearching(false);
    }

    // Method for updating fragment when fragment is re-opened
    public void showFragment() {
        retrieveNotifications();
    }

    // ===============================
    // These methods are for the 'Loading' texts that display while data is being retrieved
    private void startSearching() {
        setSearchingState("Loading...");
        taskWaitingLayout.setVisibility(View.VISIBLE);
    }

    public void finishSearching(boolean success) {
        if(success) {
            taskWaitingLayout.setVisibility(View.INVISIBLE);
        } else {
            setSearchingState("No results found");
        }
    }

    private void setSearchingState(String state) {
        searchingStateText.setText(state);
    }
    // ===============================
}
