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
                                (String) userTask.child("work").getValue()
                        );

                        System.out.println("TASK LOADED:");
                        System.out.println("TaskID: " + task.getUniqueTaskID());
                        System.out.println("UserID: " + task.getUserID());
                        System.out.println("Title: " + task.getTitle());
                        System.out.println("Description: " + task.getDescription());
                        System.out.println("Category: " + task.getCategory().getName());
                        System.out.println("Price: " + task.getPrice());
                        System.out.println("Work: " + task.getWork());
                        System.out.println("Location: " + task.getLocation().toJson());

                        parentLayout.addView(getTaskButton(task));
                        parentLayout.addView(getTaskButton(task));
                        parentLayout.addView(getTaskButton(task));
                        parentLayout.addView(getTaskButton(task));
                        parentLayout.addView(getTaskButton(task));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
    }

    private ConstraintLayout getTaskButton(Task task) {

        ConstraintLayout constraintLayout = new ConstraintLayout(getContext());
        constraintLayout.setId(ViewCompat.generateViewId());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        constraintLayout.setLayoutParams(params);
        constraintLayout.setBackgroundResource(R.drawable.task_background);

        TextView titleText = new TextView(getContext());
        titleText.setTextSize(28);
        titleText.setText(task.getTitle());
        titleText.setId(ViewCompat.generateViewId());

        TextView descriptionText = new TextView(getContext());
        descriptionText.setMaxWidth(700);
        descriptionText.setHeight(300);

        String description = task.getDescription().substring(0, Math.min(task.getDescription().length(), 100));
        if(task.getDescription().length() > 80) description += "...";
        descriptionText.setText(description);
        descriptionText.setId(ViewCompat.generateViewId());

        TextView priceText = new TextView(getContext());
        priceText.setText("Price: € " + task.getPrice());
        priceText.setId(ViewCompat.generateViewId());

        TextView workText = new TextView(getContext());
        workText.setText("Work: " + task.getWork() + " hr(s)");
        workText.setId(ViewCompat.generateViewId());

        TextView cityText = new TextView(getContext());
        cityText.setText("Location: Nijmegen");
        cityText.setId(ViewCompat.generateViewId());

        ImageView taskImage = new ImageView(getContext());
        taskImage.setImageResource(R.drawable.ic_add_to_photos_blue_24dp);
        taskImage.setMaxWidth(90);
        taskImage.setMinimumWidth(90);
        taskImage.setMaxHeight(90);
        taskImage.setMinimumHeight(90);
        taskImage.setId(ViewCompat.generateViewId());

        constraintLayout.addView(titleText);
        constraintLayout.addView(descriptionText);
        constraintLayout.addView(priceText);
        constraintLayout.addView(workText);
        constraintLayout.addView(cityText);
        constraintLayout.addView(taskImage);

        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);

        // TITLE
        set.connect(titleText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 20);
        set.connect(titleText.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 20);

        // LOCATION
        set.connect(cityText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 20);
        set.connect(cityText.getId(), ConstraintSet.TOP, titleText.getId(), ConstraintSet.BOTTOM, 0);

        // IMAGE
        set.connect(taskImage.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, 20);
        set.connect(taskImage.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 20);

        // DESCRIPTION
        set.connect(descriptionText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 20);
        set.connect(descriptionText.getId(), ConstraintSet.TOP, cityText.getId(), ConstraintSet.BOTTOM, 5);
        set.connect(descriptionText.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 20);

        // PRICE
        set.connect(priceText.getId(), ConstraintSet.LEFT, descriptionText.getId(), ConstraintSet.RIGHT, 40);
        set.connect(priceText.getId(), ConstraintSet.TOP, taskImage.getId(), ConstraintSet.BOTTOM, 10);

        // WORK
        set.connect(workText.getId(), ConstraintSet.LEFT, descriptionText.getId(), ConstraintSet.RIGHT, 40);
        set.connect(workText.getId(), ConstraintSet.TOP, priceText.getId(), ConstraintSet.BOTTOM, 0);

        set.applyTo(constraintLayout);

        return constraintLayout;
    }
}
