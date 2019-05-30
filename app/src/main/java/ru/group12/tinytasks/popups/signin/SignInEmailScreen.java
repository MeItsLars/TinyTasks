package ru.group12.tinytasks.popups.signin;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.group12.tinytasks.R;

public class SignInEmailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinemailscreen);

        // Initialising top textview
        TextView emailText = findViewById(R.id.emailText);
        emailText.setText("itslarsyt@gmail.com");
        GradientDrawable emailTextBackground = (GradientDrawable) emailText.getBackground();
        emailTextBackground.setColor(Color.argb(51, 152, 229, 121));
        emailTextBackground.setStroke(2, Color.argb(255, 82, 173, 46));

        EditText password = findViewById(R.id.enterPassword);
        changeDrawableState(password.getBackground(), DrawableState.NEUTRAL);

        Button signInButton = findViewById(R.id.signInButton);
        initializeSignInButton(signInButton, emailText.getText().toString(), password);
    }

    // Sign in button code
    public void initializeSignInButton(Button signInButton, final String email, final EditText password) {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email, password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //TODO: Succesfull task
                            password.setError("Correct!");
                        } else {
                            password.setError("Incorrect password. Please try again.");
                        }
                    }
                });
            }
        });
    }


    // ============= Code for changing edittext appearance ===========

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