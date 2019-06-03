package ru.group12.tinytasks.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.database.Database;
import ru.group12.tinytasks.database.objects.Task;
import ru.group12.tinytasks.database.objects.User;

public class CreateTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createjob);
        final Button createTask = findViewById(R.id.ct_bttn_create_task);

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTheTask();
                // go to next activity (where you can see your listing)
            }
        });
        /* final Button addImage = findViewById(R.id.);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createTheTask();
                // Pass the task to the next activity and let the user ask images.
            }
        });*/
    }

    public void createTheTask() {
        // Get all info from the text boxes.
        EditText nameField = findViewById(R.id.ct_create_name_task);
        String createdUserName = nameField.getText().toString();
        User createdUser = Database.getCurrentUser();
        EditText descField = findViewById(R.id.ct_create_task_description);
        String description = descField.getText().toString();
        String category;
        String taskName;
        String priceRange; // get lower and upp and concatenate with a - in between.
        String cityLocation;
        String street;
        // Task t = new Task (createdUserName, createdUser, category, taskName, description, priceRange, cityLocation, street);
    }
}
