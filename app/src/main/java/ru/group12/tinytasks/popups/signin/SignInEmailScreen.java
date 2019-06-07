package ru.group12.tinytasks.popups.signin;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.internet.Network;

public class SignInEmailScreen extends AppCompatActivity {

    private SignInEmailScreen activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinemailscreen);
        Network.registerInternetStateChangedListener(this);

        activity = this;

        initializeContents();

    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        finish();
    }

    // Method for initializing important views, and adding functionality to buttons and edittexts
    private void initializeContents() {
        final TextView emailText = findViewById(R.id.emailText);
        emailText.setText(getIntent().getStringExtra("email"));
        GradientDrawable emailTextBackground = (GradientDrawable) emailText.getBackground();
        emailTextBackground.setColor(Color.argb(51, 152, 229, 121));
        emailTextBackground.setStroke(2, Color.argb(255, 82, 173, 46));

        final EditText passwordEditText = findViewById(R.id.enterPassword);
        changeDrawableState(passwordEditText.getBackground(), DrawableState.NEUTRAL);

        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(emailText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    // Load user and start new activity when done
                                    Database.loadCurrentUser(activity);
                                } else {
                                    passwordEditText.setError("Incorrect password. Please try again.");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        passwordEditText.setError("Wrong password. Please try again!");
                    }
                });
            }
        });
    }

    // ============= Code for changing edittext appearance ===========
    // EditText border colors are changed when the input is right/wrong
    private enum DrawableState {RIGHT, WRONG, NEUTRAL}

    private void changeDrawableState(Drawable drawable, DrawableState state) {
        GradientDrawable gDrawable = (GradientDrawable) drawable;

        switch(state) {
            case RIGHT:
                gDrawable.setColor(Color.argb(51, 152, 229, 121));
                gDrawable.setStroke(2, Color.argb(255, 82, 173, 46));
                break;
            case WRONG:
                gDrawable.setColor(Color.argb(51, 229, 121, 121));
                gDrawable.setStroke(2, Color.argb(255, 173, 45, 45));
                break;
            case NEUTRAL:
                gDrawable.setColor(Color.argb(51, 0, 0, 0));
                gDrawable.setStroke(2, Color.rgb(225, 225, 225));
                break;
            default:
                break;
        }
    }
}