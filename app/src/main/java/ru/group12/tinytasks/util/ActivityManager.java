package ru.group12.tinytasks.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;

import ru.group12.tinytasks.activities.ApplicationHomeActivity;
import ru.group12.tinytasks.util.database.objects.Task;

public class ActivityManager {
    public static void startNewActivity(Activity activity, Class newClass) {
        Intent intent = new Intent(activity, newClass);
        activity.startActivity(intent);
    }

    public static void startNewActivity(Activity activity, Class newClass, Map<String, String> data) {
        Intent intent = new Intent(activity, newClass);
        for(String key : data.keySet()) intent.putExtra(key, data.get(key));
        activity.startActivity(intent);
    }

    public static void startNewActivity(Activity activity, Class newClass, boolean finishCurrent) {
        Intent intent = new Intent(activity, newClass);
        activity.startActivity(intent);
        if(finishCurrent) activity.finish();
    }

    public static void backToHomeActivity(Activity activity, String fragment) {
        activity.finishAffinity();
        Intent intent = new Intent(activity, ApplicationHomeActivity.class);
        intent.putExtra("fragment", fragment);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void startNewActivity(Activity activity, Class newClass, boolean finishCurrent, int flags) {
        Intent intent = new Intent(activity, newClass);
        intent.addFlags(flags);
        activity.startActivity(intent);
        if(finishCurrent) activity.finish();
    }

    public static void startNewActivityTask(Activity activity, Class newClass, boolean finishCurrent, int flags, Task task) {
        Intent intent = new Intent(activity, newClass);
        intent.addFlags(flags);
        intent.putExtra("task", task);
        activity.startActivity(intent);
        if(finishCurrent) activity.finish();
    }

    public static void startNewActivityForResult(Activity activity, Class newClass, int requestCode) {
        Intent intent = new Intent(activity, newClass);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startNewActivity(Context activity, Class newClass, int flags) {
        Intent intent = new Intent(activity, newClass);
        intent.addFlags(flags);
        activity.startActivity(intent);
    }
}
