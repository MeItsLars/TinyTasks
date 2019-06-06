package ru.group12.tinytasks.activities.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.TaskManager;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.enums.Category;

public class HomeFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_homescreen, container, false);

        loadRecommendedTasks();

        return inflatedView;
    }

    /*
    Gets a list of tasks from the database and decides which to show.
    Ambition is to remove the users own tasks from this list and recommend based on preferences the user might set in settings.
     */
    private void loadRecommendedTasks() {
        final LinearLayout parentLayout = inflatedView.findViewById(R.id.recommended_task_list);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                        System.out.println("TASK LOADED:");
                        System.out.println("TaskID: " + task.getUniqueTaskID());
                        System.out.println("UserID: " + task.getUserID());
                        System.out.println("Title: " + task.getTitle());
                        System.out.println("Description: " + task.getDescription());
                        System.out.println("Category: " + task.getCategory().getName());
                        System.out.println("Price: " + task.getPrice());
                        System.out.println("Work: " + task.getWork());
                        System.out.println("Location: " + task.getLocation().toJson());

                        parentLayout.addView(TaskManager.getTaskButton(task, getContext()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
    }
}
