package ru.group12.tinytasks.util.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.media.Image;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ru.group12.tinytasks.util.database.objects.Task;

public class ImageManager {

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
    private static DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

    public static void uploadTaskImages(Activity activity, Task task) {
        for(Uri uri : task.getUris()) {
            if(uri != null) uploadTaskImage(activity, task, uri);
        }
    }

    private static void uploadTaskImage(Activity activity, Task task, Uri uri) {
        StorageReference fileReference = mStorageRef.child(task.getUniqueTaskID()).child(System.currentTimeMillis() + "." + getFileExtension(activity, uri));

        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Image added!");
            }
        });
    }

    private static String getFileExtension(Activity activity, Uri uri) {
        ContentResolver cR = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static void loadImagesToTask(Task task) {
        mStorageRef.child(task.getUniqueTaskID()).getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

            }
        });
    }
}
