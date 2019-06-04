package ru.group12.tinytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.group12.tinytasks.util.database.objects.enums.Category;

public class createTask_part3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part2);
        final Bundle extra = getIntent().getExtras();
        final String name = extra.getString("name");
        final String description = extra.getString("description");
        final String tprice = extra.getString("price");
        final String thours = extra.getString("hours");
        final Category category;
        // also get the location probably.

        Button create = findViewById(R.id.create_task_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make listing
                // add it to database
                // go to the listing
                Intent intent = new Intent(createTask_part3.this, createTask_part3.class);
                startActivity(intent);
            }
        });

        ImageButton image1 = findViewById(R.id.add_image_1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // let user upload an image from their phone
                // update image of this button to that image
                // make that part of the task image image1.setImageIcon();
                // allow removing all images(?) rn you can update but not remove them i think.
            }
        });
    }
}
