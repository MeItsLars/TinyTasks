package ru.group12.tinytasks.popups.series.taskcreation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.series.taskcreation.TaskCreation;

public class TestC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_a);

        final TaskCreation taskCreation = (TaskCreation) getIntent().getExtras().getSerializable("manager");

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
