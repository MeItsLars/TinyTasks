package ru.group12.tinytasks.popups.createtask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;

public class CreateTaskPart1Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part1);

        Button nextPartButton = findViewById(R.id.task_next_page_button);
        nextPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivity(CreateTaskPart1Screen.this, CreateTaskPart2Screen.class);
            }
        });
    }
}
