package ru.group12.tinytasks.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.TaskManager;

public class HomeFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_homescreen, container, false);

        parentLayout = inflatedView.findViewById(R.id.recommended_task_list);

        return inflatedView;
    }

    private LinearLayout parentLayout;

    public void showFragment() {
        loadRecommendedTasks();
    }

    private void loadRecommendedTasks() {
        if(parentLayout == null) return;
        parentLayout.removeAllViewsInLayout();

        TaskManager.loadRandomTasks(getActivity(), parentLayout);
    }
}
