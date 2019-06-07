package ru.group12.tinytasks.util.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import ru.group12.tinytasks.util.database.objects.Task;
import ru.group12.tinytasks.util.database.objects.User;

public class ImageManager {

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

    //Uploads the image of a task to the database
    public static void uploadTaskImage(Activity activity, Task task, Uri uri) {
        if(uri != null) {
            StorageReference fileReference = mStorageRef.child(task.getUniqueTaskID()).child("taskimage." + getFileExtension(activity, uri));

            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Image added!");
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    // Used to get the type of file for the image needed
    private static String getFileExtension(Activity activity, Uri uri) {
        ContentResolver cR = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // Used to get an image from the database and load it into the relevant place
    public static void loadTaskImage(final Context context, Task task, final ImageView view) {
        mStorageRef.child(task.getUniqueTaskID()).child("taskimage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(view);
            }
        });
    }

}
