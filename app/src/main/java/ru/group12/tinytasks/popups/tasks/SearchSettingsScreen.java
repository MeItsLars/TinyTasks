package ru.group12.tinytasks.popups.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.adapters.CategoryStringAdapter;
import ru.group12.tinytasks.util.database.objects.SearchSettings;
import ru.group12.tinytasks.util.database.objects.enums.Category;
import ru.group12.tinytasks.util.internet.Network;

public class SearchSettingsScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchsettings);
        Network.registerInternetStateChangedListener(this);

        initializeContents();
    }

    private void initializeContents() {
        final EditText priceFrom = findViewById(R.id.price_from_edittext);
        final EditText priceTo = findViewById(R.id.price_to_edittext);
        final EditText workFrom = findViewById(R.id.work_from_edittext);
        final EditText workTo = findViewById(R.id.work_to_edittext);
        final EditText distanceTo = findViewById(R.id.distance_to_edittext);

        final Spinner categorySpinner = findViewById(R.id.category_spinner);
        final Spinner searchType = findViewById(R.id.search_type_spinner);
        final Spinner searchOrdering = findViewById(R.id.search_ordering_spinner);

        Category[] categoryValues = Category.values();
        String[] categories = new String[categoryValues.length + 1];
        categories[0] = "All";
        for(int i = 1; i < categories.length; i++) categories[i] = categoryValues[i - 1].name();

        CategoryStringAdapter customAdapter = new CategoryStringAdapter(getApplicationContext(), categories);
        categorySpinner.setAdapter(customAdapter);

        Button doneButton = findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceFromString = priceFrom.getText().toString();
                String priceToString = priceTo.getText().toString();
                String workFromString = workFrom.getText().toString();
                String workToString = workTo.getText().toString();
                String distanceToString = distanceTo.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String sortBy = searchType.getSelectedItem().toString();
                String orderBy = searchOrdering.getSelectedItem().toString();

                SearchSettings settings = new SearchSettings(
                        priceFromString,
                        priceToString,
                        workFromString,
                        workToString,
                        distanceToString,
                        category,
                        sortBy,
                        orderBy);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("settings", settings);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
