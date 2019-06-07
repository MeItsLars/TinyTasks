package ru.group12.tinytasks.util.database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.user.ViewUserProfileScreen;
import ru.group12.tinytasks.util.database.objects.Notification;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.User;
import ru.group12.tinytasks.util.database.objects.enums.Category;
import ru.group12.tinytasks.util.PixelManager;

public class NotificationsManager {

    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public static void sendRespondNotification(String senderUID, String senderName, String receiverUID, String taskUUID) {
        DatabaseReference ref = mDatabase.getReference("notifications").child(receiverUID).child(UUID.randomUUID().toString());

        ref.child("sender").setValue(senderUID);
        ref.child("sendername").setValue(senderName);
        ref.child("time").setValue(System.currentTimeMillis());
        ref.child("task").setValue(taskUUID);
    }

    public static void updateLatestNotifications(final Context context, final LinearLayout layout, final String receiverUID, final int limit) {
        if(receiverUID == null) return;
        DatabaseReference ref = mDatabase.getReference("notifications").child(receiverUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Notification> notifications = new ArrayList<>();

                for(DataSnapshot notification : dataSnapshot.getChildren()) {
                    long time = (long) notification.child("time").getValue();
                    String senderUID = notification.child("sender").getValue().toString();
                    String senderName = notification.child("sendername").getValue().toString();
                    String taskUUID = notification.child("task").getValue().toString();


                    notifications.add(new Notification(time, senderUID, senderName, taskUUID));
                }

                Collections.sort(notifications);

                List<Notification> sortedNotifications = new ArrayList<>();
                for(int i = Math.min(limit, notifications.size()) - 1; i >= 0; i--) {
                    sortedNotifications.add(notifications.get(i));
                }

                setNotifications(context, layout, sortedNotifications, receiverUID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
    }

    private static void setNotifications(final Context context, final LinearLayout layout, final List<Notification> notifications, final String receiverUID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks").child(receiverUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, DataSnapshot> tasks = new HashMap<>();
                for(DataSnapshot task : dataSnapshot.getChildren()) {
                    tasks.put(task.getKey(), task);
                }

                for(Notification notification : notifications) {
                    DataSnapshot taskSnapshot = tasks.get(notification.getTaskUUID());
                    if(taskSnapshot != null) {
                        Task task = new Task(
                                taskSnapshot.getKey(),
                                receiverUID,
                                CarmenFeature.fromJson((String) taskSnapshot.child("location").getValue()),
                                Category.valueOf((String) taskSnapshot.child("category").getValue()),
                                (String) taskSnapshot.child("title").getValue(),
                                (String) taskSnapshot.child("description").getValue(),
                                (String) taskSnapshot.child("price").getValue(),
                                (String) taskSnapshot.child("work").getValue(),
                                (double) taskSnapshot.child("latitude").getValue(),
                                (double) taskSnapshot.child("longitude").getValue());
                        layout.addView(getNotificationLayout(context, notification, task));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
    }

    private static ConstraintLayout getNotificationLayout(final Context context, final Notification notification, Task task) {
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
        descriptionText.setTextSize(12);
        descriptionText.setText(notification.getSenderName() + " heeft gereageerd!");
        descriptionText.setId(ViewCompat.generateViewId());

        ImageView taskImage = new ImageView(context);
        taskImage.setLayoutParams(new ConstraintLayout.LayoutParams(PixelManager.convertDpToPixel(70, context), PixelManager.convertDpToPixel(70, context)));
        taskImage.setImageResource(R.drawable.app_logo);
        taskImage.setId(ViewCompat.generateViewId());

        // Load image to task
        ImageManager.loadTaskImage(context, task, taskImage);

        constraintLayout.addView(titleText);
        constraintLayout.addView(descriptionText);
        constraintLayout.addView(taskImage);

        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);

        // TITLE
        set.connect(titleText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(titleText.getId(), ConstraintSet.RIGHT, taskImage.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(titleText.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, PixelManager.convertDpToPixel(8, context));

        // IMAGE
        set.connect(taskImage.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, PixelManager.convertDpToPixel(8, context));
        set.connect(taskImage.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, PixelManager.convertDpToPixel(8, context));
        set.connect(taskImage.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, PixelManager.convertDpToPixel(8, context));

        // DESCRIPTION
        set.connect(descriptionText.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(descriptionText.getId(), ConstraintSet.RIGHT, taskImage.getId(), ConstraintSet.LEFT, PixelManager.convertDpToPixel(8, context));
        set.connect(descriptionText.getId(), ConstraintSet.TOP, titleText.getId(), ConstraintSet.BOTTOM, 2);

        set.applyTo(constraintLayout);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(notification.getSenderUID());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User sender = new User(notification.getSenderUID(),
                                dataSnapshot.child("email").getValue() != null ? (String) dataSnapshot.child("email").getValue() : "EMAIL-NOT-INITIALIZED",
                                (String) dataSnapshot.child("name").getValue(),
                                (String) dataSnapshot.child("surname").getValue(),
                                (String) dataSnapshot.child("phoneNumber").getValue(),
                                (String) dataSnapshot.child("birthDate").getValue(),
                                (String) dataSnapshot.child("gender").getValue());

                        Intent intent = new Intent(context, ViewUserProfileScreen.class);
                        intent.putExtra("user", sender);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Do nothing
                    }
                });
            }
        });

        return constraintLayout;
    }
}
