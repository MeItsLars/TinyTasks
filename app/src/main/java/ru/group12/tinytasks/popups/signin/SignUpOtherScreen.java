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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import java.util.regex.Pattern;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.ActivityManager;
import ru.group12.tinytasks.util.database.Database;

public class SignUpOtherScreen extends AppCompatActivity {

    private SignUpOtherScreen activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupotherscreen);
        activity = this;

        AuthCredential credential = getIntent().getParcelableExtra("credential");
        initializeTextFields();
        initializeSignUpButton(credential);
    }

    // Initialize filled in texts:
    private void initializeTextFields() {
        @NotNull String email = getIntent().getStringExtra("email");
        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String birthDate = getIntent().getStringExtra("birthDate");
        String gender = getIntent().getStringExtra("gender");

        TextView emailText = findViewById(R.id.emailText);
        emailText.setText(email);
        changeDrawableState(emailText.getBackground(), DrawableState.RIGHT);

        EditText nameEditText = findViewById(R.id.firstnameText);
        addChangeListener(nameEditText, DrawablePattern.namePattern, "Please enter a valid first name.");
        if(name != null) nameEditText.setText(name);

        EditText surnameEditText = findViewById(R.id.surnameText);
        addChangeListener(surnameEditText, DrawablePattern.namePattern, "Please enter a valid surname.");
        if(surname != null) surnameEditText.setText(surname);

        EditText phoneNumberEditText = findViewById(R.id.phoneNumber);
        addChangeListener(phoneNumberEditText, DrawablePattern.phoneNumberPattern, "Please enter a valid (dutch) phone number.");
        if(phoneNumber != null) phoneNumberEditText.setText(phoneNumber);

        EditText birthDateEditText = findViewById(R.id.birthDate);
        addChangeListener(birthDateEditText, DrawablePattern.birthDatePattern, "Please enter a valid birth date (DD-MM-YYYY).");
        if(birthDate != null) birthDateEditText.setText(birthDate);

        EditText genderEditText = findViewById(R.id.gender);
        addChangeListener(genderEditText, DrawablePattern.genderPattern, "Please enter a valid gender: M (Male), F (Female), U (Unknown).");
        if(gender != null) genderEditText.setText(gender);
    }

    private void addChangeListener(final EditText editText, final Pattern pattern, final String message) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkDrawableState(editText, pattern))
                    editText.setError(message);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });
    }

    // Initialize sign up button
    private void initializeSignUpButton(final AuthCredential credential) {
        Button signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText nameEditText = findViewById(R.id.firstnameText);
                final EditText surnameEditText = findViewById(R.id.surnameText);
                final EditText phoneNumberEditText = findViewById(R.id.phoneNumber);
                final EditText birthDateEditText = findViewById(R.id.birthDate);
                final EditText genderEditText = findViewById(R.id.gender);

                if(checkDrawableState(nameEditText, DrawablePattern.namePattern) &&
                        checkDrawableState(surnameEditText, DrawablePattern.namePattern) &&
                        checkDrawableState(phoneNumberEditText, DrawablePattern.phoneNumberPattern) &&
                        checkDrawableState(birthDateEditText, DrawablePattern.birthDatePattern) &&
                        checkDrawableState(genderEditText, DrawablePattern.genderPattern)) {

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if(Database.userSignedIn()) {
                                            Database.registerNewUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                    nameEditText.getText().toString(),
                                                    surnameEditText.getText().toString(),
                                                    phoneNumberEditText.getText().toString(),
                                                    birthDateEditText.getText().toString(),
                                                    genderEditText.getText().toString());

                                            Database.loadCurrentUser(activity);
                                        } else {
                                            //TODO: Failed.
                                            System.out.println("User account registration failed. User not signed in.");
                                        }
                                    } else {
                                        //TODO: Failed.
                                        System.out.println("User account registration failed due to an unknown reason.");
                                    }
                                }
                            });
                }
            }
        });
    }

    // ============= Code for changing drawable appearance ===========
    static class DrawablePattern {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$");
        private static final Pattern phoneNumberPattern = Pattern.compile("^((\\+|00(\\s|\\s?\\-\\s?)?)31(\\s|\\s?\\-\\s?)?(\\(0\\)[\\-\\s]?)?|0)[1-9]((\\s|\\s?\\-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]$");
        private static final Pattern birthDatePattern = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");
        private static final Pattern genderPattern = Pattern.compile("^(?:[MFU])$");
    }

    private boolean checkDrawableState(TextView view, Pattern pattern) {
        if(pattern.matcher(view.getText()).matches()) {
            changeDrawableState(view.getBackground(), DrawableState.RIGHT);
            return true;
        }
        //else:
        changeDrawableState(view.getBackground(), DrawableState.WRONG);
        return false;
    }

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