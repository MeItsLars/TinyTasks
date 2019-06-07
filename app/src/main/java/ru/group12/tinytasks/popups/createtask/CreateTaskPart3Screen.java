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
import ru.group12.tinytasks.util.internet.Network;

// Activity for creating tasks
public class CreateTaskPart3Screen extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part3);
        Network.registerInternetStateChangedListener(this);

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

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        finish();
    }

    private ImageButton addImage;
    private Uri uri;

    // Method for initializing important views, and adding functionality to buttons and edittexts
    private void initializeContents() {
        Button createTaskButton = findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityManager.startNewActivityTask(CreateTaskPart3Screen.this, CreateTaskSuccessScreen.class, true, Intent.FLAG_ACTIVITY_CLEAR_TASK, task);

                Intent intent = new Intent(CreateTaskPart3Screen.this, CreateTaskSuccessScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("task", task);
                if(uri != null) intent.putExtra("uri", uri.toString());
                startActivity(intent);
                finish();
            }
        });

        addImage = findViewById(R.id.add_image_1);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    // UPLOADING IMAGES
    private static int PICK_IMAGE_REQUEST = 1;

    // Method for opening the android file chooser, to select an image.
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Method for retrieving the selected image and putting it into an URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();

            Picasso.with(this).load(uri).into(addImage);
        }
    }
}