package ru.group12.tinytasks.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.NotificationsManager;

public class NotificationsFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_notifications, container, false);

        layout = inflatedView.findViewById(R.id.notifications_list);
        // TODO fix that you can log out and still see your alerts.
        retrieveNotifications();
        return inflatedView;
    }

    private LinearLayout layout;

    private void retrieveNotifications() {
        if(Database.getCurrentUser() != null) {
            layout.removeAllViewsInLayout();
            NotificationsManager.updateLatestNotifications(getContext(), layout, Database.getCurrentUser().getUid(), 10);
        }
    }

    public void showFragment() {
        retrieveNotifications();
    }
}
