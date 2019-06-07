package ru.group12.tinytasks.util.database.objects;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.Arrays;
import java.util.List;

import ru.group12.tinytasks.util.DeviceLocationManager;
import ru.group12.tinytasks.util.database.objects.enums.Category;

public class Task implements Parcelable, Comparable<Task> {

    private String uniqueTaskID;
    private String userID;
    private CarmenFeature location;
    private Category category;
    private String title;
    private String description;
    private String price;
    private String work;
    private double latitude;
    private double longitude;

    public Task(String uniqueTaskID, String userID, CarmenFeature location, Category category, String title, String description, String price, String work, double latitude, double longitude) {
        this.uniqueTaskID = uniqueTaskID;
        this.userID = userID;
        this.location = location;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.work = work;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String sortBy = "Price";
    private String orderBy = "Ascending";
    private Activity activity;
    private LocationManager locationManager;

    public void prepareSort(Activity activity, LocationManager locationManager, String sortBy, String orderBy) {
        this.sortBy = sortBy;
        this.orderBy = orderBy;
        this.activity = activity;
        this.locationManager = locationManager;
    }

    public String getUniqueTaskID() {
        return uniqueTaskID;
    }

    public String getUserID() {
        return userID;
    }

    public CarmenFeature getLocation() {
        return location;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getWork() {
        return work;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Location getAndroidLocation() {
        Location location = new Location("");
        location.setLatitude(getLatitude());
        location.setLongitude(getLongitude());
        return location;
    }

    protected Task(Parcel in) {
        uniqueTaskID = in.readString();
        userID = in.readString();
        location = CarmenFeature.fromJson(in.readString());
        category = (Category) in.readValue(Category.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        price = in.readString();
        work = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uniqueTaskID);
        dest.writeString(userID);
        dest.writeString(location.toJson());
        dest.writeValue(category);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(work);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int compareTo(Task task) {
        if(sortBy.equals("Price")) {
            if(orderBy.equals("Ascending")) return Double.compare(Double.valueOf(getPrice()), Double.valueOf(task.getPrice()));
            else return Double.compare(Double.valueOf(task.getPrice()), Double.valueOf(getPrice()));
        } else if(sortBy.equals("Work amount")) {
            if(orderBy.equals("Ascending")) return Double.compare(Double.valueOf(getWork()), Double.valueOf(task.getWork()));
            else return Double.compare(Double.valueOf(task.getWork()), Double.valueOf(getWork()));
        } else if(sortBy.equals("Distance")) {
            Location deviceLocation = DeviceLocationManager.getDeviceLocation(activity, locationManager);
            if(deviceLocation == null) return 0;
            if(orderBy.equals("Ascending")) return Double.compare(deviceLocation.distanceTo(getAndroidLocation()), deviceLocation.distanceTo(task.getAndroidLocation()));
            else return Double.compare(deviceLocation.distanceTo(task.getAndroidLocation()), deviceLocation.distanceTo(getAndroidLocation()));
        } else return 0;
    }
}