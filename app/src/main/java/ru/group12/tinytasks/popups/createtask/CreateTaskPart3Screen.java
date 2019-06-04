package ru.group12.tinytasks.popups.createtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.UUID;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.enums.Category;

public class CreateTaskPart3Screen extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part3);

        initializeContents();

        task = new Task(UUID.randomUUID().toString(),
                Database.getCurrentUser().getUid(),
                CarmenFeature.fromJson(getIntent().getStringExtra("location")),
                Category.valueOf(getIntent().getStringExtra("category")),
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("description"),
                getIntent().getStringExtra("price"),
                getIntent().getStringExtra("work"));
    }

    private void initializeContents() {



        Button createTaskButton = findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivityTask(CreateTaskPart3Screen.this, CreateTaskSuccessScreen.class, true, Intent.FLAG_ACTIVITY_CLEAR_TASK, task);
            }
        });
    }
}