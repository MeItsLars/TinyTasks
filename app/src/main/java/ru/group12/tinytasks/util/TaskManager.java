package ru.group12.tinytasks.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.objects.SearchSettings;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.internet.PixelManager;

public class TaskManager {

    public static List<Task> sortTasks(Activity context, LocationManager locationManager, List<Task> tasks, SearchSettings settings, int limit) {
        Location userLocation = DeviceLocationManager.getDeviceLocation(context, locationManager);

        List<Task> rawSortedTasks = new ArrayList<>();
        for(Task task : tasks) {
            System.out.println("Data:");
            System.out.println("Task price: " + task.getPrice());
            System.out.println("Settings price: " + settings.getPriceFrom() + " to " + settings.getPriceTo());
            System.out.println("Task work: " + task.getWork());
            System.out.println("Settings work: " + settings.getWorkFrom() + " to " + settings.getWorkTo());
            System.out.println("Settings distance: " + settings.getDistanceTo());
            
            double taskPrice = Double.valueOf(task.getPrice());
            double minPrice = Double.valueOf(settings.getPriceFrom());
            double maxPrice = Double.valueOf(settings.getPriceTo());

            double taskWork = Double.valueOf(task.getWork());
            double minWork = Double.valueOf(settings.getWorkFrom());
            double maxWork = Double.valueOf(settings.getWorkTo());

            double maxDistance = Double.valueOf(settings.getDistanceTo());

            if((settings.getCategory().equals("All") || settings.getCategory().equals(task.getCategory().name())) &&
                    ((minPrice <= taskPrice && taskPrice <= maxPrice) || maxPrice <= 0) &&
                    ((minWork <= taskWork && taskWork <= maxWork) || maxWork <= 0) &&
                    (maxDistance <= 0 || isLocationInRange(userLocation, task.getAndroidLocation(), maxDistance))) {
                rawSortedTasks.add(task);
                task.prepareSort(context, locationManager, settings.getSortBy(), settings.getOrderBy());
            }
        }
        System.out.println(rawSortedTasks.size() + " tasks left after removing non valid tasks.");

        Collections.sort(rawSortedTasks);
        System.out.println(rawSortedTasks.size() + " tasks left after ordering.");

        List<Task> sortedTasks = new ArrayList<>();
        for(int i = 0; i < Math.min(10, rawSortedTasks.size()); i++) sortedTasks.add(rawSortedTasks.get(i));
        System.out.println(rawSortedTasks.size() + " tasks left after limiting.");

        return sortedTasks;
    }

    public static boolean isLocationInRange(Location location1, Location location2, double distance) {
        boolean value = (location1 == null || location2 == null) || location1.distanceTo(location2) <= distance * 1000;
        System.out.println("Location in range: " + value);
        return value;
    }

    public static ConstraintLayout getTaskButton(Task task, Context context) {
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
        cityText.setText("Location: Nijmegen");
        cityText.setId(ViewCompat.generateViewId());

        ImageView taskImage = new ImageView(context);
        taskImage.setLayoutParams(new ConstraintLayout.LayoutParams(PixelManager.convertDpToPixel(90, context), PixelManager.convertDpToPixel(90, context)));
        taskImage.setImageResource(R.drawable.app_logo);
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

        return constraintLayout;
    }
}
