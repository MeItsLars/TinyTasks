package ru.group12.tinytasks.popups.tasks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.TaskManager;
import ru.group12.tinytasks.util.database.Database;

public class ViewMyTasksScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks_screen);

        initializeContents();
    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        finish();
    }

    private void initializeContents() {
        LinearLayout layout = findViewById(R.id.profile_my_tasks);
        if(Database.getCurrentUser() == null) return;

        TaskManager.loadUserTasks(this, layout, Database.getCurrentUser().getUid());
    }
}
