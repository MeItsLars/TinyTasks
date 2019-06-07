package ru.group12.tinytasks.popups.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.signin.SignInScreen;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.TaskManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.ImageManager;
import ru.group12.tinytasks.util.database.NotificationsManager;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.User;
import ru.group12.tinytasks.util.internet.Network;

public class ViewTaskScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtask);
        Network.registerInternetStateChangedListener(this);

        initializeContents();
    }

    public void initializeContents() {
        final Task task = getIntent().getParcelableExtra("task");

        TextView titleView = findViewById(R.id.task_title);
        TextView descriptionView = findViewById(R.id.task_description);
        TextView categoryView = findViewById(R.id.taskcategory2);
        TextView priceView = findViewById(R.id.taskprice2);
        TextView locationView = findViewById(R.id.tasklocation2);
        TextView workView = findViewById(R.id.tasktime2);
        ImageView taskImage = findViewById(R.id.task_image);

        titleView.setText(task.getTitle());
        descriptionView.setText(task.getDescription());
        categoryView.setText(task.getCategory().getName());
        priceView.setText("€ " + task.getPrice());
        locationView.setText(task.getLocation().placeName());
        workView.setText(task.getWork() + " hr(s)");
        ImageManager.loadTaskImage(this, task, taskImage);

        Button backToHome = findViewById(R.id.back_to_home_button);
        final Button reactToTask = findViewById(R.id.react_to_task_button);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(Database.userSignedIn()) {
            reactToTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User sender = Database.getCurrentUser();
                    if(sender != null) NotificationsManager.sendRespondNotification(sender.getUid(), sender.getName(), task.getUserID(), task.getUniqueTaskID());
                    reactToTask.setText("Reaction sent");
                }
            });
        } else {
            reactToTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        ActivityManager.startNewActivity(ViewTaskScreen.this, SignInScreen.class, Intent.FLAG_ACTIVITY_NEW_TASK);
                        reactToTask.setEnabled(false);
                    }

            });
        }
    }
}
