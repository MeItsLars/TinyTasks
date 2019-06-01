package ru.group12.tinytasks.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.group12.tinytasks.database.objects.User;

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

    public static void registerCurrentUser() {
        if(userSignedIn()) {
            String uid = mAuth.getCurrentUser().getUid();
            DatabaseReference userProfile = mDatabase.getReference("users").child(uid);
            userProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("========================================");
                    System.out.println(dataSnapshot);

                    currentUser = new User(
                            (String) dataSnapshot.child("name").getValue(),
                            (String) dataSnapshot.child("surname").getValue(),
                            (String) dataSnapshot.child("phoneNumber").getValue(),
                            (String) dataSnapshot.child("birthDate").getValue(),
                            (String) dataSnapshot.child("gender").getValue());

                    System.out.println("User: " + currentUser);

                    /*String name, surname, phoneNumber, birhtDate, gender;

                    for(DataSnapshot subData : dataSnapshot.getChildren()) {
                        if(subData.getKey().equals("name")) gender = (String) subData.getValue();

                        System.out.println("Sub: " + subData);
                        currentUser = new User(
                                (String) subData.child("name").getValue(),
                                (String) subData.child("surname").getValue(),
                                (String) subData.child("phoneNumber").getValue(),
                                (String) subData.child("birthDate").getValue(),
                                (String) subData.child("gender").getValue());
                    }*/
                    System.out.println("========================================");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }
    }

    public static void addUser(String uid, String name, String surname, String phoneNumber, String birthDate, String gender, boolean registerUser) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        ref.child(uid).child("name").setValue(name);
        ref.child(uid).child("surname").setValue(surname);
        ref.child(uid).child("phoneNumber").setValue(phoneNumber);
        ref.child(uid).child("birthDate").setValue(birthDate);
        ref.child(uid).child("gender").setValue(gender);

        if(registerUser) registerCurrentUser();
    }
}
