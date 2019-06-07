package ru.group12.tinytasks.popups.createtask;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.internet.Network;

public class CreateTaskSuccessScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_success);
        Network.registerInternetStateChangedListener(this);

        Task task = getIntent().getParcelableExtra("task");
        String uriString = getIntent().getStringExtra("uri");
        Uri uri = null;
        if(uriString != null) uri = Uri.parse(uriString);

        Database.uploadTask(this, task, uri);

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