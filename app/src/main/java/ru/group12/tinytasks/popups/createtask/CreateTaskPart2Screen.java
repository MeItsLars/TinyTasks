package ru.group12.tinytasks.popups.createtask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.location.LocationSelectionScreen;
import ru.group12.tinytasks.util.ActivityManager;

public class CreateTaskPart2Screen extends AppCompatActivity {

    private final int MAP_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask_part2);

        initializeContents();
    }

    private TextView taskLocationText;

    private Spinner priceType;
    private EditText taskPrice;
    private EditText amountOfWork;
    private CarmenFeature feature;
    private double latitude;
    private double longitude;

    private boolean priceCorrect = false;
    private boolean amountOfWorkCorect = false;
    private boolean locationCorrect = false;

    private void initializeContents() {

        priceType = findViewById(R.id.price_type_listview);
        taskPrice = findViewById(R.id.task_price_edittext);

        final String title = getIntent().getStringExtra("title");
        final String description = getIntent().getStringExtra("description");
        final String category = getIntent().getStringExtra("category");

        priceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String content = priceType.getSelectedItem().toString();
                if(content.equals("Enter price")) {
                    taskPrice.setVisibility(View.VISIBLE);
                    taskPrice.setClickable(true);

                    if(Pattern.compile("^(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$").matcher(taskPrice.getText()).matches()) {
                        priceCorrect = true;
                    } else priceCorrect = false;
                } else if(content.equals("To be determined")) {
                    taskPrice.setVisibility(View.INVISIBLE);
                    taskPrice.setClickable(false);
                    priceCorrect = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        taskPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Pattern.compile("^(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{1,2})?$").matcher(charSequence).matches()) {
                    changeDrawableState(taskPrice.getBackground(), DrawableState.RIGHT);
                    priceCorrect = true;
                    taskPrice.setError(null);
                } else {
                    changeDrawableState(taskPrice.getBackground(), DrawableState.WRONG);
                    priceCorrect = false;
                    taskPrice.setError("Please enter a valid price");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Do nothing
            }
        });

        amountOfWork = findViewById(R.id.work_amount_edittext);
        amountOfWork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Pattern.compile("^\\d{0,3}(\\.\\d{1,2})?$").matcher(charSequence).matches()) {
                    changeDrawableState(amountOfWork.getBackground(), DrawableState.RIGHT);
                    amountOfWorkCorect = true;
                    amountOfWork.setError(null);
                } else {
                    changeDrawableState(amountOfWork.getBackground(), DrawableState.WRONG);
                    amountOfWorkCorect = false;
                    amountOfWork.setError("Please enter a valid amount of work");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Do nothing
            }
        });

        Button nextPage = findViewById(R.id.task_next_page_button);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceCorrect && amountOfWorkCorect && locationCorrect) {
                    Map<String, String> data = new HashMap<String, String>(){{
                        put("title", title);
                        put("description", description);
                        put("category", category);
                        put("work", amountOfWork.getText().toString());
                        put("location", feature.toJson());
                        put("latitude", String.valueOf(latitude));
                        put("longitude", String.valueOf(longitude));
                    }};
                    if(priceType.getSelectedItem().toString().equals("Enter price")) data.put("price", taskPrice.getText().toString());
                    else data.put("price", "To be determined");

                    ActivityManager.startNewActivity(CreateTaskPart2Screen.this, CreateTaskPart3Screen.class, data);
                }
            }
        });

        Button selectLocationButton = findViewById(R.id.select_location_button);
        taskLocationText = findViewById(R.id.task_location_text);

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivityForResult(CreateTaskPart2Screen.this, LocationSelectionScreen.class, MAP_REQUEST_CODE);
            }
        });
    }

    // ============= Code for changing drawable appearance ===========
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

    // MapBox location retriever
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MAP_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                feature = CarmenFeature.fromJson(data.getStringExtra("resultFeature"));
                latitude = data.getDoubleExtra("latitude", 0);
                longitude = data.getDoubleExtra("longitude", 0);
                taskLocationText.setText(feature.placeName());
                locationCorrect = true;
            }
        }
    }
}
