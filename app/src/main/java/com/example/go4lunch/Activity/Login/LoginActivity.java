package com.example.go4lunch.Activity.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.go4lunch.Model.RestaurantChoice;
import com.example.go4lunch.Repository.SharedPreferencesRepository;
import com.example.go4lunch.Util.CheckConnectivity;

import com.example.go4lunch.Activity.BaseActivity;
import com.example.go4lunch.Activity.Main.MainActivity;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Repository.UserRepository;
import com.example.go4lunch.Repository.UserSingletonRepository;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "Login Activity";
    public FirebaseAuth firebaseAuth;
    SwipeRefreshLayout pullToRefresh;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if a user is connected then the application goes to MainActivity
        //    else
        // a user must be connected to use the application, then the signIn screen appears)
        setContentView(R.layout.activity_login);
        // In case no connection is detected, enable pull to refresh on "no connection layout"
        pullToRefresh = findViewById(R.id.signin_no_connection_pull_to_refresh);
        pullToRefresh.setOnRefreshListener(() -> {
            checkConnectivity(); // your code
            pullToRefresh.setRefreshing(false);
        });
        checkConnectivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkConnectivity() {
        if (!CheckConnectivity.isConnected(getApplicationContext())) {
            pullToRefresh.setVisibility(View.VISIBLE);
        } else {
            pullToRefresh.setVisibility(View.GONE);
            if (isCurrentUserLogged()) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                saveUserSingleton(userId);
                startMainActivity();
            } else {
                setContentView(R.layout.activity_login);
                SignIn();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveUserSingleton(String userId) {
        UserRepository userRepository = new UserRepository();
        userRepository.getUserFirestore(userId).addOnSuccessListener(user -> {
            // Set the data with the user information
            User singletonUser = new User();
            singletonUser.setUid(userId);
            saveSingleton(user);
        });
    }

    private void SignIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.logo_g4l)
                .setIsSmartLockEnabled(false, true)
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            UserRepository userRepository = new UserRepository();
            User mUser = userRepository.createUserInFirestore();
            //Init UserSingletonRepository
            saveSingleton(mUser);
            // Start Main Activity when all is OK
            startMainActivity();
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            Log.w(TAG,
                    "signInWithCredential:failure",
                    response.getError());
            Toast.makeText(LoginActivity.this, "Authentication Failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveSingleton(User mUser) {
        // Creation of a singleton with the user information. The singleton will be used by the drawer
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        userSingletonRepository.setUser(mUser);

        Context context = getApplicationContext();
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository();
        RestaurantChoice restaurantChoice = new RestaurantChoice();
        restaurantChoice.setUserId(mUser.getUid());
        restaurantChoice.setChoosenRestaurantId(mUser.getChosenRestaurantId());
        restaurantChoice.setChoosenRestaurantName(mUser.getChosenRestaurantName());
        restaurantChoice.setChoosenRestaurantAdress(mUser.getChosenRestaurantAdress());
        restaurantChoice.setChoosenDate(mUser.getChosenRestaurantDate());
        sharedPreferencesRepository.saveRestaurantChoice(context, restaurantChoice);
    }
}


