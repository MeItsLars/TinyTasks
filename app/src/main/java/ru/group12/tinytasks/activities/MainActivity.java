package ru.group12.tinytasks.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;

import java.util.Arrays;
import java.util.List;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.popups.location.LocationSelectionScreen;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.internet.Network;
import ru.group12.tinytasks.popups.signin.SignInScreen;

public class MainActivity extends AppCompatActivity {

    private MainActivity activity;
    private LocationManager locationManager;
    private String lattitude, longitude;

    final static int MAP_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;


        Network.registerInternetStateChangedListener(this);
        // Load current user *IF* they exist.
        Database.loadCurrentUser();

        /** =================== LOCATION STUFF ========================== */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        Button button = findViewById(R.id.getLocationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    System.out.println("LOCATION IS DISABLED");

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
            }
        });

        /** ================== GOOGLE

        Places.initialize(this, "AIzaSyBpPSBRuARo5HKn0igXFHRalyYehDDexhs");
        PlacesClient placesClient = Places.createClient(this);

        Button placePickerButton = findViewById(R.id.placePickerButton);
        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(MainActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        /** =========================================================== */

        /** ===================== MAPBOX ======================= */

        Button placePickerButton = findViewById(R.id.placePickerButton);
        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.startNewActivityForResult(MainActivity.this, LocationSelectionScreen.class, MAP_REQUEST_CODE);
            }
        });

        /** =========================================================== */

        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startNewActivity(activity, SignInScreen.class, false,
                        Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        Button viewProfileButton = findViewById(R.id.viewProfileButton);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                activity.startActivity(intent);
            }
        });

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.signOutCurrentUser(activity);
            }
        });
    }

    /** =================== LOCATION STUFF ========================== */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MAP_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                CarmenFeature feature = CarmenFeature.fromJson(data.getStringExtra("resultFeature"));
                System.out.println("Name: " + feature.placeName());
            }
        }
    }

    private void getLocation() {
        System.out.println("Retrieving location...");
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            } else {

                System.out.println("Location unavailable");
                return;
            }

            System.out.println("Your current location is" + "\n" + "Lattitude = " + lattitude
                    + "\n" + "Longitude = " + longitude);
        }
    }
}
