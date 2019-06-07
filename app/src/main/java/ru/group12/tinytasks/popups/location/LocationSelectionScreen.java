package ru.group12.tinytasks.popups.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.internet.Network;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * Drop a marker at a specific location and then perform
 * reverse geocoding to retrieve and display the location's address
 */
public class LocationSelectionScreen extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {

    private static final String TAG = "LocationPickerActivity";
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Button selectLocationButton;
    private Button confirmLocationButton;
    private TextView messageTextView;
    private PermissionsManager permissionsManager;
    private ImageView hoveringMarker;

    private CarmenFeature lastSelectedFeature;
    private double lastSelectedLatitude;
    private double lastSelectedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML
        setContentView(R.layout.activity_locationselectionscreen);
        Network.registerInternetStateChangedListener(this);

        // Initialize the mapboxMap view
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LocationSelectionScreen.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                enableLocationPlugin(style);

                sendMessage("Please select a location on the map");

                // When user is still picking a location, we hover a marker above the mapboxMap in the center.
                hoveringMarker = new ImageView(LocationSelectionScreen.this);
                hoveringMarker.setImageResource(R.drawable.ic_location_on_red_24dp);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                hoveringMarker.setLayoutParams(params);
                mapView.addView(hoveringMarker);

                // Initialize, but don't show, a SymbolLayer for the marker icon which will represent a selected location.
                initDroppedMarker(style);

                // Button for user to drop marker or to pick marker back up.
                selectLocationButton = findViewById(R.id.select_location_button);
                selectLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hoveringMarker.getVisibility() == View.VISIBLE) {

                            // Use the map target's coordinates to make a reverse geocoding search
                            final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

                            // Hide the hovering red hovering ImageView marker
                            hoveringMarker.setVisibility(View.INVISIBLE);

                            // Transform the appearance of the button to become the cancel button
                            selectLocationButton.setText("Select other location");

                            // Show the SymbolLayer icon to represent the selected map location
                            if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                                if (source != null) {
                                    source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                                }
                                style.getLayer(DROPPED_MARKER_LAYER_ID).setProperties(visibility(VISIBLE));
                            }

                            // Use the map camera target's coordinates to make a reverse geocoding search
                            reverseGeocode(style, Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));

                        } else {

                            // Switch the button appearance back to select a location.
                            selectLocationButton.setText("Select location");
                            setConfirm(null);
                            sendMessage("");

                            // Show the red hovering ImageView marker
                            hoveringMarker.setVisibility(View.VISIBLE);

                            // Hide the selected location SymbolLayer
                            if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                style.getLayer(DROPPED_MARKER_LAYER_ID).setProperties(visibility(NONE));
                            }
                        }
                    }
                });
            }
        });


        confirmLocationButton = findViewById(R.id.confirm_location_button);
        setConfirm(null);
        confirmLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("resultFeature", lastSelectedFeature.toJson());
                returnIntent.putExtra("latitude", lastSelectedLatitude);
                returnIntent.putExtra("longitude", lastSelectedLongitude);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        messageTextView = findViewById(R.id.messageTextView);
    }

    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        // Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", getResources().getDrawable(R.drawable.ic_location_on_blue_24dp));

        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Toast.makeText(this, "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                enableLocationPlugin(style);
            }
        } else {
            //Toast.makeText(this, "user_location_permission_not_granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * This method is used to reverse geocode where the user has dropped the marker.
     * This method is NOT made by us, referring to Radboud University R&D Group 12.
     * We only modified it to fit our application needs
     *
     * @param style style
     * @param point The location to use for the search
     */

    private void reverseGeocode(@NonNull final Style style, final Point point) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .query(Point.fromLngLat(point.longitude(), point.latitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();

            System.out.println("Coördinates: " + point.coordinates());

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                    List<CarmenFeature> results = response.body().features();
                    if (results.size() > 0) {
                        CarmenFeature feature = results.get(0);

                        // If the geocoder returns a result, we take the first in the list and show a Toast with the place name.
                        if (style.isFullyLoaded() && style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            sendMessage("Location: " + feature.placeName());
                            setConfirm(feature.placeName());
                            lastSelectedFeature = feature;
                            lastSelectedLatitude = point.latitude();
                            lastSelectedLongitude = point.longitude();
                        }

                    } else {
                        sendMessage("No results found for this location");
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    System.out.println("Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            System.out.println("Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void setConfirm(@Nullable String text) {
        if(text == null) {
            confirmLocationButton.setVisibility(View.INVISIBLE);
            confirmLocationButton.setClickable(false);
        }
        else {
            confirmLocationButton.setText("Confirm: " + text);
            confirmLocationButton.setVisibility(View.VISIBLE);
            confirmLocationButton.setClickable(true);
        }
    }

    private void sendMessage(String message) {
        messageTextView.setText(message);
    }
}
