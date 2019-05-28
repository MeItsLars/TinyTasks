package ru.group12.tinytasks.popups.series.taskcreation.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.series.taskcreation.TaskCreation;

public class TestA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_a);

        final TaskCreation taskCreation = (TaskCreation) getIntent().getSerializableExtra("manager");

        Button prevButton = findViewById(R.id.previousButton);
        Button nextButton = findViewById(R.id.nextButton);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskCreation.openPreviousActivity();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskCreation.openNextActivity();
            }
        });
    }
}
