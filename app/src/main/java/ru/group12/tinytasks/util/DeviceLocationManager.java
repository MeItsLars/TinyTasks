package ru.group12.tinytasks.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class DeviceLocationManager {
    // Retrives the location from the user to help decide if tasks are in range.
    public static Location getDeviceLocation(Activity context, LocationManager locationManager) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;

        } else {
            Location location = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(android.location.LocationManager.PASSIVE_PROVIDER);

            if(location != null) return location;
            else if(location1 != null) return location1;
            else if(location2 != null) return location2;
            return null;
        }
    }
}
