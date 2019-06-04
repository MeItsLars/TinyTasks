package ru.group12.tinytasks.util.database.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import ru.group12.tinytasks.util.database.objects.enums.Category;

public class Task implements Parcelable {

    private String uniqueTaskID;
    private String userID;
    private CarmenFeature location;
    private Category category;
    private String title;
    private String description;
    private String price;
    private String work;

    public Task(String uniqueTaskID, String userID, CarmenFeature location, Category category, String title, String description, String price, String work) {
        this.uniqueTaskID = uniqueTaskID;
        this.userID = userID;
        this.location = location;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.work = work;
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

    protected Task(Parcel in) {
        uniqueTaskID = in.readString();
        userID = in.readString();
        location = CarmenFeature.fromJson(in.readString());
        category = (Category) in.readValue(Category.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        price = in.readString();
        work = in.readString();
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
}