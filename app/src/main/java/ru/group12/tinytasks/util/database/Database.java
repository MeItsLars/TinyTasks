package ru.group12.tinytasks.util.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.models.Search;

import java.util.List;

import ru.group12.tinytasks.popups.signin.SignInSuccessScreen;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.objects.SearchSettings;
import ru.group12.tinytasks.util.database.objects.User;

public class Database {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static User currentUser;

    public static boolean userSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public static User getCurrentUser() {
        return userSignedIn() ? currentUser : null;
    }

    public static void signOutCurrentUser(Context context) {
        if(userSignedIn()) {
            AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    currentUser = null;
                }
            });
        }
    }

    public static void loadCurrentUser() {
        if(userSignedIn()) {
            final String uid = mAuth.getCurrentUser().getUid();
            final String email = mAuth.getCurrentUser().getEmail();
            DatabaseReference userProfile = mDatabase.getReference("users").child(uid);
            userProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = new User(uid,
                            email,
                            (String) dataSnapshot.child("name").getValue(),
                            (String) dataSnapshot.child("surname").getValue(),
                            (String) dataSnapshot.child("phoneNumber").getValue(),
                            (String) dataSnapshot.child("birthDate").getValue(),
                            (String) dataSnapshot.child("gender").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }
    }

    public static void loadCurrentUser(final AppCompatActivity activity) {
        if(userSignedIn()) {
            final String uid = mAuth.getCurrentUser().getUid();
            final String email = mAuth.getCurrentUser().getEmail();
            DatabaseReference userProfile = mDatabase.getReference("users").child(uid);
            userProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = new User(uid,
                            email,
                            (String) dataSnapshot.child("name").getValue(),
                            (String) dataSnapshot.child("surname").getValue(),
                            (String) dataSnapshot.child("phoneNumber").getValue(),
                            (String) dataSnapshot.child("birthDate").getValue(),
                            (String) dataSnapshot.child("gender").getValue());

                    ActivityManager.startNewActivity(activity, SignInSuccessScreen.class, true,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }
    }

    public static void registerNewUser(String uid, String name, String surname, String phoneNumber, String birthDate, String gender) {
        DatabaseReference ref = mDatabase.getReference().child("users");

        ref.child(uid).child("name").setValue(name);
        ref.child(uid).child("surname").setValue(surname);
        ref.child(uid).child("phoneNumber").setValue(phoneNumber);
        ref.child(uid).child("birthDate").setValue(birthDate);
        ref.child(uid).child("gender").setValue(gender);
    }

    public static void uploadTask(Activity activity, ru.group12.tinytasks.util.database.objects.Task task) {
        DatabaseReference tasksReference = mDatabase.getReference().child("tasks");
        DatabaseReference userTasksReference = tasksReference.child(task.getUserID());
        DatabaseReference taskReference = userTasksReference.child(task.getUniqueTaskID());

        taskReference.child("title").setValue(task.getTitle());
        taskReference.child("description").setValue(task.getDescription());
        taskReference.child("category").setValue(task.getCategory().name());
        taskReference.child("price").setValue(task.getPrice());
        taskReference.child("work").setValue(task.getWork());
        taskReference.child("location").setValue(task.getLocation().toJson());
        taskReference.child("latitude").setValue(task.getLatitude());
        taskReference.child("longitude").setValue(task.getLongitude());

        ImageManager.uploadTaskImages(activity, task);
    }

    public static Query searchTasks() {
        return mDatabase.getReference("tasks");
    }
}
