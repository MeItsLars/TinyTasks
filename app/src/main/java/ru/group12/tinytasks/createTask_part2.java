package ru.group12.tinytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createTask_part2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part2);
        final Bundle extra = getIntent().getExtras();
        final String name = extra.getString("name");
        final String description = extra.getString("description");
        final Intent intent = new Intent(createTask_part2.this, createTask_part3.class);

        Button next = findViewById(R.id.task_next_page_button_p2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText price = findViewById(R.id.task_price_edittext);
                EditText hours = findViewById(R.id.work_amount_edittext);
                String taskPrice = price.getText().toString();
                String taskHours = hours.getText().toString();
                intent.putExtra("price", taskPrice);
                intent.putExtra("hours", taskHours);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                // if intent has a location
                startActivity(intent);
            }
        });

        Button setLocation = findViewById(R.id.select_location_button);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo
            }
        });
    }
}
