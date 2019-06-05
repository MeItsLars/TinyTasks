package ru.group12.tinytasks.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.ArrayList;
import java.util.List;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.tasks.SearchSettingsScreen;
import ru.group12.tinytasks.util.TaskManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.SearchSettings;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.enums.Category;

public class SearchTaskFragment extends Fragment {

    private View inflaterView;
    private final static int SEARCH_REQUEST_CODE = 1;
    private LinearLayout taskLayout;

    private SearchSettings settings;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflaterView = inflater.inflate(R.layout.fragment_searchtask, container, false);

        initializeContents();

        return inflaterView;
    }

    private void initializeContents() {
        settings = new SearchSettings("", "", "", "", "", "All", "Price", "Ascending");

        Button searchButton = inflaterView.findViewById(R.id.search_button);
        taskLayout = inflaterView.findViewById(R.id.searched_task_list);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchSettingsScreen.class);
                startActivityForResult(intent, SEARCH_REQUEST_CODE);
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SEARCH_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                settings = data.getParcelableExtra("settings");
                searchTasks();
            }
        }
    }

    // ==========================================================================
    // Task searching methods
    // ==========================================================================

    private void searchTasks() {
        taskLayout.removeAllViewsInLayout();
        Query taskQuery = Database.searchTasks();

        taskQuery.addListenerForSingleValueEvent(listener);
    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            System.out.println("===========================");
            System.out.println("Datasnapshot:");
            System.out.println(dataSnapshot.toString());
            List<Task> tasks = getTasksFromSnapshot(dataSnapshot);
            System.out.println("Retrieved " + tasks.size() + " raw tasks!");

            List<Task> sortedTasks = TaskManager.sortTasks(getActivity(), locationManager, tasks, settings, 10);
            updateLayout(sortedTasks);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Do nothing
        }
    };

    private List<Task> getTasksFromSnapshot(DataSnapshot dataSnapshot) {
        List<Task> tasks = new ArrayList<>();
        for(DataSnapshot users : dataSnapshot.getChildren()) {
            for(DataSnapshot userTask : users.getChildren()) {
                Task task = new Task(
                        userTask.getKey(),
                        users.getKey(),
                        CarmenFeature.fromJson((String) userTask.child("location").getValue()),
                        Category.valueOf((String) userTask.child("category").getValue()),
                        (String) userTask.child("title").getValue(),
                        (String) userTask.child("description").getValue(),
                        (String) userTask.child("price").getValue(),
                        (String) userTask.child("work").getValue(),
                        (double) userTask.child("latitude").getValue(),
                        (double) userTask.child("longitude").getValue());
                //TODO: Add images to task
                tasks.add(task);
            }
        }
        return tasks;
    }

    private void updateLayout(List<Task> tasks) {
        for(Task task : tasks) {
            ConstraintLayout constraintLayout = TaskManager.getTaskButton(task, getContext());
            taskLayout.addView(constraintLayout);
        }
        System.out.println("Updated screen. Tasks added: " + tasks.size());
        System.out.println("===========================");
    }
}
