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
import ru.group12.tinytasks.util.TaskManager;

// Fragment activity for application home
public class HomeFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_homescreen, container, false);

        initializeContents();

        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecommendedTasks();
    }

    private LinearLayout parentLayout;

    private ConstraintLayout taskWaitingLayout;
    private TextView searchingStateText;

    // Method for retrieving important views
    private void initializeContents() {
        parentLayout = inflatedView.findViewById(R.id.recommended_task_list);
        taskWaitingLayout = inflatedView.findViewById(R.id.task_waiting_layout);
        searchingStateText = inflatedView.findViewById(R.id.task_searching_state);
    }

    // Method for updating fragment contents when fragment is re-opened
    public void showFragment() {
        loadRecommendedTasks();
    }

    // Method for loading recommended tasks into fragment
    private void loadRecommendedTasks() {
        if(parentLayout == null) return;
        startSearching();
        parentLayout.removeAllViewsInLayout();

        TaskManager.loadRandomTasks(this, parentLayout);
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
