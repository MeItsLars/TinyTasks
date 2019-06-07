package ru.group12.tinytasks.popups.tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;

public class TaskDeletedScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_deleted);

        initializeContents();
    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        ActivityManager.backToHomeActivity(TaskDeletedScreen.this, "Profile");
    }

    private void initializeContents() {
        Button button = findViewById(R.id.back_to_home_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.backToHomeActivity(TaskDeletedScreen.this, "Profile");
            }
        });
    }
}
