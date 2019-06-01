package ru.group12.tinytasks.popups.signin;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.database.Database;

public class SignInScreen extends AppCompatActivity {

    SignInScreen activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize twitter SDK
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("EH1FDWkfphl87dtuvvQp8DeE9", "BdP0e9kLcjFCqmHEKmbBcbqmcsXYoE5jBYTCnF3xY55RvLJWC5"))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setContentView(R.layout.activity_signinscreen);

        activity = this;

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            AuthUI.getInstance().signOut(this);
        }

        EditText emailEditText = findViewById(R.id.enterEmail);
        initializeEmailEditText(emailEditText);

        Button signInButton = findViewById(R.id.signInButton);
        initializeSignInButton(signInButton, emailEditText);

        initializeFacebookSignIn();
        initializeTwitterSignIn();
        initializeGoogleSignIn();
    }

    // Email edittext correctness code
    public void initializeEmailEditText(final EditText emailText) {
        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0) {
                    changeDrawableState(emailText.getBackground(), DrawableState.NEUTRAL);
                } else if(Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
                    changeDrawableState(emailText.getBackground(), DrawableState.RIGHT);
                } else {
                    changeDrawableState(emailText.getBackground(), DrawableState.WRONG);
                    emailText.setError("Please enter a valid email address.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });
    }

    // Sign in button code
    public void initializeSignInButton(Button signInButton, final EditText emailEditText) {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailEditText.getText().length() > 0 && emailEditText.getError() == null) {

                    //Starting loading screen:
                    final LoadingTask loadingTask = new LoadingTask();
                    loadingTask.execute();

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.fetchSignInMethodsForEmail(emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            //Stopping loading screen:
                            loadingTask.cancel(false);

                            if(task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();
                                if(signInMethods.isEmpty()) {
                                    Intent intent = new Intent(activity, SignUpEmailScreen.class);
                                    activity.startActivity(intent);
                                } else {
                                    Intent intent = new Intent(activity, SignInEmailScreen.class);
                                    activity.startActivity(intent);
                                }
                            }
                        }
                    });
                } else if(emailEditText.length() == 0) {
                    emailEditText.setError("Please enter an email address.");
                }
            }
        });
    }

    // Facebook sign in code

    private CallbackManager mCallbackManager;

    public void initializeFacebookSignIn() {
        ImageButton customFacebookSignInButton = findViewById(R.id.customFacebookSignInButton);
        final LoginButton loginButton = findViewById(R.id.facebookSignInButton);
        loginButton.setPermissions("public_profile", "user_birthday");

        customFacebookSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile", "user_birthday");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    System.out.println("Email: " + object.getString("email"));
                                    System.out.println("Birthdate: " + object.getString("birthday"));
                                } catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //TODO: Facebook sign in failed
            }

            @Override
            public void onError(FacebookException error) {
                //TODO: Facebook sign in failed
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Facebook sign in success: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        } else {
                            System.out.println("Facebook sign in failed.");
                            //TODO: Facebook sign in failed
                        }
                    }
                });
    }

    // Google sign in code

    private static final int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;

    private void initializeGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleScopes.USERINFO_PROFILE),
                        new Scope(PeopleScopes.USER_BIRTHDAY_READ),
                        new Scope(PeopleScopes.USER_PHONENUMBERS_READ))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageButton googleSignInButton = findViewById(R.id.signInGoogle);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Google sign in success: " + Database.userSignedIn());
                        } else {
                            System.out.println("Google sign in failed.");
                            //TODO: Google sign in failed
                        }
                    }
                });
    }

    // Twitter sign in code

    private TwitterLoginButton mTwitterLoginButton;

    private void initializeTwitterSignIn() {
        mTwitterLoginButton = findViewById(R.id.twitterSignInButton);

        ImageButton twitterButton = findViewById(R.id.customTwitterSignInButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(mTwitterLoginButton);
                mTwitterLoginButton.performClick();
            }
        });

        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                //TODO: Twitter sign in failed
                System.out.println("Twitter sign in failed.");
            }
        });
    }

    private void handleTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Twitter sign in success: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        } else {
                            System.out.println("Twitter sign in failed.");
                            //TODO: Twitter sign in failed
                        }
                    }
                });

    }

    // Activity result function used by multiple inlog methods

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //TODO: Google sign in failed
                System.out.println("Google sign in failed.");
            }
        }

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

    // Task for managing waiting screen
    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FrameLayout layout = findViewById(R.id.waitingOverlay);
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FrameLayout layout = findViewById(R.id.waitingOverlay);
            layout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
