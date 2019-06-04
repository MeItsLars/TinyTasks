package ru.group12.tinytasks.popups.createtask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.Task;

public class CreateTaskSuccessScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_success);

        Task task = getIntent().getParcelableExtra("task");
        Database.uploadTask(task);

        initializeContents();
    }

    private void initializeContents() {
        Button backToHomeButton = findViewById(R.id.back_to_home_button);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.backToHomeActivity(CreateTaskSuccessScreen.this, "Home");
            }
        });
    }
}