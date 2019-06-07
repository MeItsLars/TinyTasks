package ru.group12.tinytasks.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.tasks.ViewTaskScreen;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.ImageManager;
import ru.group12.tinytasks.util.database.objects.SearchSettings;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.User;
import ru.group12.tinytasks.util.database.objects.enums.Category;

public class TaskManager {

    public static void loadRandomTasks(final Activity context, final LinearLayout parent) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Task> tasks = new ArrayList<>();

                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    if(Database.getCurrentUser() != null) {
                        if(!Database.getCurrentUser().getUid().equals(user.getKey())) {
                            for(DataSnapshot userTask : user.getChildren()) {
                                tasks.add(new Task(
                                        userTask.getKey(),
                                        user.getKey(),
                                        CarmenFeature.fromJson((String) userTask.child("location").getValue()),
                                        Category.valueOf((String) userTask.child("category").getValue()),
                                        (String) userTask.child("title").getValue(),
                                        (String) userTask.child("description").getValue(),
                                        (String) userTask.child("price").getValue(),
                                        (String) userTask.child("work").getValue(),
                                        (double) userTask.child("latitude").getValue(),
                                        (double) userTask.child("longitude").getValue()));
                            }
                        }
                    } else {
                        for(DataSnapshot userTask : user.getChildren()) {
                            tasks.add(new Task(
                                    userTask.getKey(),
                                    user.getKey(),
                                    CarmenFeature.fromJson((String) userTask.child("location").getValue()),
                                    Category.valueOf((String) userTask.child("category").getValue()),
                                    (String) userTask.child("title").getValue(),
                                    (String) userTask.child("description").getValue(),
                                    (String) userTask.child("price").getValue(),
                                    (String) userTask.child("work").getValue(),
                                    (double) userTask.child("latitude").getValue(),
                                    (double) userTask.child("longitude").getValue()));
                        }
                    }
                }

                Collections.shuffle(tasks);
                for(int i = 0; i < Math.min(5, tasks.size()); i++) {
                    parent.addView(getTaskButton(tasks.get(i), context));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
    }

    public static List<Task> sortTasks(Activity context, LocationManager locationManager, List<Task> tasks, SearchSettings settings, int limit) {
        Location userLocation = DeviceLocationManager.getDeviceLocation(context, locationManager);

        List<Task> rawSortedTasks = new ArrayList<>();
        for(Task task : tasks) {
            double taskPrice = Double.valueOf(task.getPrice());
            double minPrice = Double.valueOf(settings.getPriceFrom());
            double maxPrice = Double.valueOf(settings.getPriceTo());

            double taskWork = Double.valueOf(task.getWork());
            double minWork = Double.valueOf(settings.getWorkFrom());
            double maxWork = Double.valueOf(settings.getWorkTo());

            double maxDistance = Double.valueOf(settings.getDistanceTo());

            User currentUser = Database.getCurrentUser();

            if((settings.getCategory().equals("All") || settings.getCategory().equals(task.getCategory().name())) &&
                    ((minPrice <= taskPrice && taskPrice <= maxPrice) || maxPrice <= 0) &&
                    ((minWork <= taskWork && taskWork <= maxWork) || maxWork <= 0) &&
                    (maxDistance <= 0 || isLocationInRange(userLocation, task.getAndroidLocation(), maxDistance))) {

                if(currentUser != null) {
                    System.out.println("======================");
                    System.out.println("Current user ID: " + currentUser.getUid());
                    System.out.println("Task user ID: " + task.getUserID());
                    System.out.println("======================");
                    if(!task.getUserID().equals(currentUser.getUid())) {
                        rawSortedTasks.add(task);
                        task.prepareSort(context, locationManager, settings.getSortBy(), settings.getOrderBy());
                    }
                } else {
                    rawSortedTasks.add(task);
                    task.prepareSort(context, locationManager, settings.getSortBy(), settings.getOrderBy());
                }
            }
        }

        Collections.sort(rawSortedTasks);

        List<Task> sortedTasks = new ArrayList<>();
        for(int i = 0; i < Math.min(limit, rawSortedTasks.size()); i++) sortedTasks.add(rawSortedTasks.get(i));

        return sortedTasks;
    }

    public static boolean isLocationInRange(Location location1, Location location2, double distance) {
        return (location1 == null || location2 == null) || location1.distanceTo(location2) <= distance * 1000;
    }

    public static ConstraintLayout getTaskButton(final Task task, final Context context) {
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setId(ViewCompat.generateViewId());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, PixelManager.convertDpToPixel(20, context));
        constraintLayout.setLayoutParams(params);
        constraintLayout.setBackgroundResource(R.drawable.task_background);

        TextView titleText = new TextView(context);
        titleText.setTextSize(25);
        titleText.setMaxHeight(PixelManager.convertDpToPixel(35, context));
        String title = task.getTitle().substring(0, Math.min(task.getTitle().length(), 15));
        if(task.getTitle().length() > 15) title += "...";
        titleText.setText(title);
        titleText.setId(ViewCompat.generateViewId());

        TextView descriptionText = new TextView(context);
        descriptionText.setMaxWidth(PixelManager.convertDpToPixel(200, context));
        descriptionText.setHeight(PixelManager.convertDpToPixel(50, context));

        String description = task.getDescription().substring(0, Math.min(task.getDescription().length(), 70));
        if(task.getDescription().length() > 70) description += "...";
        descriptionText.setText(description);
        descriptionText.setId(ViewCompat.generateViewId());

        TextView priceText = new TextView(context);
        priceText.setText("Price: € " + task.getPrice());
        priceText.setId(ViewCompat.generateViewId());

        TextView workText = new TextView(context);
        workText.setText("Work: " + task.getWork() + " hr(s)");
        workText.setId(ViewCompat.generateViewId());

        TextView cityText = new TextView(context);
        cityText.setText("Location: " + task.getLocation().context().get(1).text());


        System.out.println(task.getLocation().toJson());
        cityText.setId(ViewCompat.generateViewId());

        ImageView taskImage = new ImageView(context);
        taskImage.setLayoutParams(new ConstraintLayout.LayoutParams(PixelManager.convertDpToPixel(90, context), PixelManager.convertDpToPixel(90, context)));
        taskImage.setImageResource(R.drawable.app_logo);
        taskImage.setId(ViewCompat.generateViewId());

        // Load image to task
        ImageManager.loadTaskImage(context, task, taskImage);

        constraintLayout.addView(titleText);
        constraintLayout.addView(descriptionText);
        constraintLayout.addView(priceText);
        constraintLayout.addView(workText);
        constraintLayout.addView(cityText);
        constraintLayout.addView(taskImage);

        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);

        // TITLE
        set.connect(titleText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(titleText.getId(), ConstraintSet.RIGHT, taskImage.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(titleText.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, PixelManager.convertDpToPixel(8, context));

        // LOCATION
        set.connect(cityText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(cityText.getId(), ConstraintSet.TOP, titleText.getId(), ConstraintSet.BOTTOM, 0);

        // IMAGE
        set.connect(taskImage.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, PixelManager.convertDpToPixel(8, context));
        set.connect(taskImage.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, PixelManager.convertDpToPixel(8, context));

        // DESCRIPTION
        set.connect(descriptionText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(descriptionText.getId(), ConstraintSet.TOP, cityText.getId(), ConstraintSet.BOTTOM, 2);

        // PRICE
        set.connect(priceText.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, PixelManager.convertDpToPixel(15, context));
        set.connect(priceText.getId(), ConstraintSet.TOP, taskImage.getId(), ConstraintSet.BOTTOM, 10);

        // WORK
        set.connect(workText.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, PixelManager.convertDpToPixel(15, context));
        set.connect(workText.getId(), ConstraintSet.TOP, priceText.getId(), ConstraintSet.BOTTOM, 0);
        set.connect(workText.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, PixelManager.convertDpToPixel(8, context));

        set.applyTo(constraintLayout);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewTaskScreen.class);
                intent.putExtra("task", task);
                context.startActivity(intent);
            }
        });

        return constraintLayout;
    }
}
