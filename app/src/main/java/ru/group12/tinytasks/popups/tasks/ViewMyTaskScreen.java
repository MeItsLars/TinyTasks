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

public class ViewMyTaskScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmytask);
        Network.registerInternetStateChangedListener(this);

        initializeContents();
    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        finish();
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

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final Button deleteTask = findViewById(R.id.delete_task_button);
        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskManager.deleteTask(task);
                ActivityManager.startNewActivity(ViewMyTaskScreen.this, TaskDeletedScreen.class, true);
            }
        });
    }
}
