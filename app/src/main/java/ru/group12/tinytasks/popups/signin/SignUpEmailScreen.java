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

public class SignUpEmailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupemailscreen);

        // Initialising top textview
        final TextView emailText = findViewById(R.id.emailText);
        emailText.setText("larsjeurissen@hotmail.nl");
        GradientDrawable emailTextBackground = (GradientDrawable) emailText.getBackground();
        emailTextBackground.setColor(Color.argb(51, 152, 229, 121));
        emailTextBackground.setStroke(2, Color.argb(255, 82, 173, 46));

        final EditText password = findViewById(R.id.enterPassword);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() <= 3) {
                    changeDrawableState(password.getBackground(), DrawableState.NEUTRAL);
                } else if(charSequence.length() <= 6) {
                    changeDrawableState(password.getBackground(), DrawableState.WRONG);
                    password.setError("Password not long enough!");
                } else {
                    changeDrawableState(password.getBackground(), DrawableState.RIGHT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String pwd = password.getText().toString();

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            System.out.println("Current user: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        } else {
                            System.out.println("Email sign-in failed!");
                        }
                    }
                });
            }
        });
    }

    // Initial password correctness code

    // Repeating password correctness code



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