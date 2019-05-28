package ru.group12.tinytasks.popups.series.taskcreation;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import ru.group12.tinytasks.popups.series.taskcreation.activities.TestA;
import ru.group12.tinytasks.popups.series.taskcreation.activities.TestB;
import ru.group12.tinytasks.popups.series.taskcreation.activities.TestC;

public class TaskCreation implements Serializable {

    private Activity[] activities;
    private int currentActivity = 0;

    //private String result = "CANCELLED";

    public TaskCreation(Activity parent) {
        this.activities = new Activity[]{parent, new TestA(), new TestB(), new TestC()};
        openNextActivity();

        //Intent intent = new Intent(parent, TestA.class);
        //intent.putExtra("manager", this);
        //parent.startActivity(intent);
    }

    public void openPreviousActivity() {
        //Intent intent = new Intent(activities[currentActivity], activities[currentActivity <= 1 ? 0 : currentActivity - 1].getClass());
        //intent.putExtra("manager", this);
        //activities[currentActivity].startActivity(intent);
        //currentActivity--;
    }

    public void openNextActivity() {
        //System.out.println("length: " + activities.length);
        //System.out.println("A: " + activities[currentActivity]);
        //System.out.println("B: " + activities[(currentActivity + 1) % activities.length]);
        Intent intent = new Intent(activities[currentActivity], activities[(currentActivity + 1) % activities.length].getClass());
        intent.putExtra("manager", this);
        activities[currentActivity].startActivity(intent);
        currentActivity++;
        //if(currentActivity == activities.length) result = "FINISHED";
    }

    public String getResult() {
        //return result;
        return null;
    }
}
