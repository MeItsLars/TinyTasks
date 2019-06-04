package ru.group12.tinytasks.util;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class ActivityManager {
    public static void startNewActivity(AppCompatActivity activity, Class newClass) {
        Intent intent = new Intent(activity, newClass);
        activity.startActivity(intent);
    }

    public static void startNewActivity(AppCompatActivity activity, Class newClass, boolean finishCurrent) {
        Intent intent = new Intent(activity, newClass);
        activity.startActivity(intent);
        if(finishCurrent) activity.finish();
    }

    public static void startNewActivity(AppCompatActivity activity, Class newClass, boolean finishCurrent, int flags) {
        Intent intent = new Intent(activity, newClass);
        intent.addFlags(flags);
        activity.startActivity(intent);
        if(finishCurrent) activity.finish();
    }

    public static void startNewActivityForResult(AppCompatActivity activity, Class newClass, int requestCode) {
        Intent intent = new Intent(activity, newClass);
        activity.startActivityForResult(intent, requestCode);
    }
}
