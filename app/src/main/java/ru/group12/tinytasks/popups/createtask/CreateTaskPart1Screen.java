package ru.group12.tinytasks.popups.createtask;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.adapters.CategoryAdapter;
import ru.group12.tinytasks.util.database.objects.enums.Category;
import ru.group12.tinytasks.util.internet.Network;

// Activity for creating tasks
public class CreateTaskPart1Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part1);
        Network.registerInternetStateChangedListener(this);

        initializeContents();
    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        finish();
    }

    private EditText taskNameEditText;
    private EditText taskDescriptionEditText;
    private Spinner categorySpinner;

    private boolean taskTitleReady = false;
    private boolean taskDescriptionReady = false;

    // Method for initializing important views, and adding functionality to buttons and edittexts
    private void initializeContents() {
        categorySpinner = findViewById(R.id.categories_spinner);

        CategoryAdapter customAdapter = new CategoryAdapter(getApplicationContext(), Category.values());
        categorySpinner.setAdapter(customAdapter);

        taskNameEditText = findViewById(R.id.task_name_edittext);
        taskNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // DO NOTHING
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String taskTitle = charSequence.toString();
                if(taskTitle.length() >= 5) {
                    changeDrawableState(taskNameEditText.getBackground(), DrawableState.RIGHT);
                    taskNameEditText.setError(null);
                    taskTitleReady = true;
                } else {
                    changeDrawableState(taskNameEditText.getBackground(), DrawableState.WRONG);
                    if(taskTitleReady || taskTitle.length() <= 1)
                        taskNameEditText.setError("Please use at least 5 characters");
                    taskTitleReady = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // DO NOTHING
            }
        });

        taskDescriptionEditText = findViewById(R.id.task_description_edittext);
        taskDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // DO NOTHING
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String taskDescription = charSequence.toString();
                if(taskDescription.length() >= 20) {
                    changeDrawableState(taskDescriptionEditText.getBackground(), DrawableState.RIGHT);
                    taskDescriptionEditText.setError(null);
                    taskDescriptionReady = true;
                } else {
                    changeDrawableState(taskDescriptionEditText.getBackground(), DrawableState.WRONG);
                    if(taskDescriptionReady || taskDescription.length() <= 1)
                        taskDescriptionEditText.setError("Please use at least 20 characters for your task description.");
                    taskDescriptionReady = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // DO NOTHING
            }
        });

        Button nextPage = findViewById(R.id.task_next_page_button);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(taskTitleReady && taskDescriptionReady) {
                    Map<String, String> data = new HashMap<String, String>(){{
                        put("title", taskNameEditText.getText().toString());
                        put("description", taskDescriptionEditText.getText().toString());
                        put("category", categorySpinner.getSelectedItem().toString());
                    }};
                    ActivityManager.startNewActivity(CreateTaskPart1Screen.this, CreateTaskPart2Screen.class, data);
                }
            }
        });
    }

    // ============= Code for changing edittext appearance ===========
    // EditText border colors are changed when the input is right/wrong
    private enum DrawableState {RIGHT, WRONG, NEUTRAL}

    private void changeDrawableState(Drawable drawable, DrawableState state) {
        GradientDrawable gDrawable = (GradientDrawable) drawable;

        switch(state) {
            case RIGHT:
                gDrawable.setColor(Color.argb(51, 152, 229, 121));
                gDrawable.setStroke(2, Color.argb(255, 82, 173, 46));
                break;
            case WRONG:
                gDrawable.setColor(Color.argb(51, 229, 121, 121));
                gDrawable.setStroke(2, Color.argb(255, 173, 45, 45));
                break;
            case NEUTRAL:
                gDrawable.setColor(Color.argb(51, 0, 0, 0));
                gDrawable.setStroke(2, Color.rgb(225, 225, 225));
                break;
            default:
                break;
        }
    }
}