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

public class CreateTaskPart1Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part1);

        initializeContents();
    }

    private EditText taskNameEditText;
    private EditText taskDescriptionEditText;
    private Spinner categorySpinner;

    private boolean taskTitleReady = false;
    private boolean taskDescriptionReady = false;

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
                String taskTitle = charSequence.toString();
                if(taskTitle.length() >= 20) {
                    changeDrawableState(taskDescriptionEditText.getBackground(), DrawableState.RIGHT);
                    taskDescriptionEditText.setError(null);
                    taskDescriptionReady = true;
                } else {
                    changeDrawableState(taskDescriptionEditText.getBackground(), DrawableState.WRONG);
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

                if(categorySpinner == null) System.out.println("CATEGORY SPINNER NULL");
                else if(categorySpinner.getSelectedItem() == null) System.out.println("SELECTED ITEM NULL");
                else System.out.println("AAAAAAAAAAAAAAAAAAH");

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