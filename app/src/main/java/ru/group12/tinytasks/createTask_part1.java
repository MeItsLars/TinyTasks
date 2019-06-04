package ru.group12.tinytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.group12.tinytasks.util.database.objects.enums.Category;

public class createTask_part1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part1);
        Button next = findViewById(R.id.task_next_page_button_p1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = findViewById(R.id.task_name_edittext);
                EditText description = findViewById(R.id.task_description_edittext);
                //WIDGET category = findViewByID(R.id.category)
                String taskName = name.getText().toString();
                String taskDescription = description.getText().toString();
                Category taskCategory;
                Intent intent = new Intent(createTask_part1.this, createTask_part2.class);
                intent.putExtra("name", taskName);
                intent.putExtra("description", taskDescription);
                //intent.putExtra ("category", taskCategory);
                startActivity(intent);
            }
        });
    }
}
