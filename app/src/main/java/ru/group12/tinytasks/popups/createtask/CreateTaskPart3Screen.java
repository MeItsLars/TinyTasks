package ru.group12.tinytasks.popups.createtask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.squareup.picasso.Picasso;

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
                getIntent().getStringExtra("work"),
                Double.parseDouble(getIntent().getStringExtra("latitude")),
                Double.parseDouble(getIntent().getStringExtra("longitude")));
    }

    ImageButton addImage1;
    ImageButton addImage2;
    ImageButton addImage3;
    ImageButton addImage4;
    ImageButton addImage5;
    ImageButton addImage6;

    private void initializeContents() {
        Button createTaskButton = findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityManager.startNewActivityTask(CreateTaskPart3Screen.this, CreateTaskSuccessScreen.class, true, Intent.FLAG_ACTIVITY_CLEAR_TASK, task);

                task.setUri(1, uri1);
                task.setUri(2, uri2);
                task.setUri(3, uri3);
                task.setUri(4, uri4);
                task.setUri(5, uri5);
                task.setUri(6, uri6);

                Intent intent = new Intent(CreateTaskPart3Screen.this, CreateTaskSuccessScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("task", task);
                startActivity(intent);
                finish();
            }
        });

        addImage1 = findViewById(R.id.add_image_1);
        addImage2 = findViewById(R.id.add_image_2);
        addImage3 = findViewById(R.id.add_image_3);
        addImage4 = findViewById(R.id.add_image_4);
        addImage5 = findViewById(R.id.add_image_5);
        addImage6 = findViewById(R.id.add_image_6);

        addImage1.setOnClickListener(clickListener);
        addImage2.setOnClickListener(clickListener);
        addImage3.setOnClickListener(clickListener);
        addImage4.setOnClickListener(clickListener);
        addImage5.setOnClickListener(clickListener);
        addImage6.setOnClickListener(clickListener);
    }

    // UPLOADING IMAGES

    private ImageButton lastPressedButton;
    private static int PICK_IMAGE_REQUEST = 1;

    private Uri uri1;
    private Uri uri2;
    private Uri uri3;
    private Uri uri4;
    private Uri uri5;
    private Uri uri6;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageButton button = (ImageButton) view;
            lastPressedButton = button;
            openFileChooser();
        }
    };

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST &&resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(lastPressedButton);

            if(lastPressedButton.getId() == addImage1.getId()) uri1 = imageUri;
            else if(lastPressedButton.getId() == addImage2.getId()) uri2 = imageUri;
            else if(lastPressedButton.getId() == addImage3.getId()) uri3 = imageUri;
            else if(lastPressedButton.getId() == addImage4.getId()) uri4 = imageUri;
            else if(lastPressedButton.getId() == addImage5.getId()) uri5 = imageUri;
            else if(lastPressedButton.getId() == addImage6.getId()) uri6 = imageUri;
        }
    }
}