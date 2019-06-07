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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.internet.Network;

public class SignUpEmailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupemailscreen);
        Network.registerInternetStateChangedListener(this);

        initializeContents();
    }

    // Method for determining correct actions when the phone's 'back' button is pressed.
    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean passwordCorrect = false;
    private boolean confirmPasswordCorrect = false;
    private boolean fullNameCorrect = false;
    private boolean phoneNumberCorrect = false;
    private boolean birthDateCorrect = false;
    private boolean genderCorrect = false;

    private void initializeContents() {
        final TextView emailText = findViewById(R.id.emailText);
        final String email = getIntent().getStringExtra("email");
        emailText.setText(email);
        changeDrawableState(emailText.getBackground(), DrawableState.RIGHT);

        final EditText password = findViewById(R.id.enterPassword);
        final EditText confirmPassword = findViewById(R.id.confirmPassword);
        final EditText fullName = findViewById(R.id.fullName);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);
        final EditText birthDate = findViewById(R.id.birthDate);
        final EditText gender = findViewById(R.id.gender);

        final Button signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordCorrect && confirmPasswordCorrect && fullNameCorrect
                        && phoneNumberCorrect && birthDateCorrect && genderCorrect) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String userName = fullName.getText().toString();
                                String name = userName.indexOf(' ') == -1 ? userName : userName.substring(0, userName.indexOf(' '));
                                String surname = (userName.indexOf(' ') == -1 || userName.indexOf(' ') + 1 >= userName.length()) ? "" : userName.substring(userName.indexOf(' ') + 1);

                                Database.registerAndLoadNewUser(SignUpEmailScreen.this,
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        email,
                                        name,
                                        surname,
                                        phoneNumber.getText().toString(),
                                        birthDate.getText().toString(),
                                        gender.getText().toString(),
                                        "email");
                            }
                        }
                    });
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkDrawableState(password, DrawablePattern.passwordPattern)) {
                    password.setError("Please enter a password that is 6 or more characters long.");
                    passwordCorrect = false;
                } else {
                    password.setError(null);
                    passwordCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals(password.getText().toString())) {
                    changeDrawableState(confirmPassword.getBackground(), DrawableState.RIGHT);
                    confirmPassword.setError(null);
                    confirmPasswordCorrect = true;
                } else {
                    changeDrawableState(confirmPassword.getBackground(), DrawableState.WRONG);
                    confirmPassword.setError("Passwords don't match!");
                    confirmPasswordCorrect = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkDrawableState(fullName, DrawablePattern.namePattern)) {
                    fullName.setError("Please enter a valid name.");
                    fullNameCorrect = false;
                } else {
                    fullName.setError(null);
                    fullNameCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkDrawableState(phoneNumber, DrawablePattern.phoneNumberPattern)) {
                    phoneNumber.setError("Please enter a valid (dutch) phone number.");
                    phoneNumberCorrect = false;
                } else {
                    phoneNumber.setError(null);
                    phoneNumberCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });

        birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkDrawableState(birthDate, DrawablePattern.birthDatePattern)) {
                    birthDate.setError("Please enter a valid birth date (DD-MM-YYYY).");
                    birthDateCorrect = false;
                } else {
                    birthDate.setError(null);
                    birthDateCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });

        gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkDrawableState(gender, DrawablePattern.genderPattern)) {
                    gender.setError("Please enter a valid gender: M (Male), F (Female), U (Unknown).");
                    genderCorrect = false;
                } else {
                    gender.setError(null);
                    genderCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });
    }

    // ============= Code for changing drawable appearance ===========
    static class DrawablePattern {
        private static final Pattern passwordPattern = Pattern.compile("^.{6,}$");
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