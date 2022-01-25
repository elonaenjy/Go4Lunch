package com.example.go4lunch.Activity.Main;

import static com.example.go4lunch.Activity.Main.Fragments.Map.MapFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunch.Activity.BaseActivity;
import com.example.go4lunch.Activity.Login.LoginActivity;
import com.example.go4lunch.Activity.Main.Fragments.ListView.RestaurantsFragment;
import com.example.go4lunch.Activity.Main.Fragments.Map.MapFragment;
import com.example.go4lunch.Activity.Main.Fragments.Settings.SettingFragment;
import com.example.go4lunch.Activity.Main.Fragments.Workmates.WorkmatesFragment;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.Model.autocomplete.PredictionAPIAutocomplete;
import com.example.go4lunch.Model.map.Location;
import com.example.go4lunch.R;
import com.example.go4lunch.Repository.UserSingletonRepository;
import com.example.go4lunch.Util.DetailsUtil;
import com.example.go4lunch.ViewModel.ListRestaurantViewModel;
import com.example.go4lunch.ViewModelFactory;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    // STATIC PARAMETERS
    private static final String TAG = "MAIN_ACTIVITY";

    private static final String API_AUTOCOMPLETE_FILTER_KEYWORD = "restaurant";

    public static int RADIUS_INIT = 1000; // radius in meters around user for search
    public static final int RADIUS_MAX = 5000; // MAX Radius distance in meters

    String restaurantChoiceId;
    // UI
    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TextView userMail;
    private TextView userName;
    private ImageView userPicture;
    private AutoCompleteTextView autoCompleteTextView;
    private MenuItem clearButton;
    private ListRestaurantViewModel listRestaurantViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // configure
        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();
        configureInitialState();
        autoCompleteTextListener();
    }

    //----- INITIAL STATE -----
    private void configureInitialState() {
        listRestaurantViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance()).get(ListRestaurantViewModel.class);

        Fragment fragment = new MapFragment();
        mToolbar.setTitle(R.string.title_mapview);
        showFragment(fragment);
    }


    // TOOLBAR configuration
    private void configureToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextViewPlace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        clearButton = menu.findItem(R.id.main_activity_menu_clear);
        System.out.println("texte :" + autoCompleteTextView.getText()+"fintexte");
        if (autoCompleteTextView.getText().equals(null)) {
            clearButton.setVisible(false);
        } else {
            clearButton.setVisible(true);
        }
        return true;
    }

    // BOTTOM NAVIGATION configuration //
    private void configureNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            final int navigation_mapview_id = R.id.navigation_mapview;
            final int navigation_listview_id = R.id.navigation_listview;
            final int navigation_workmates_id = R.id.navigation_workmates;

            switch (item.getItemId()) {
                case navigation_mapview_id:
                    showSearch();
                    mToolbar.setTitle(R.string.title_mapview);
                    fragment = new MapFragment();
                    showFragment(fragment);
                    break;

                case navigation_listview_id:
                    showSearch();
                    mToolbar.setTitle(R.string.title_listview);
                    fragment = new RestaurantsFragment();
                    showFragment(fragment);
                    break;

                case navigation_workmates_id:
                    hideSearch();
                    mToolbar.setTitle(R.string.title_workmates);
                    fragment = new WorkmatesFragment();
                    showFragment(fragment);
                    break;
            }
            return true;
        });
    }

    private void showSearch() {
        findViewById(R.id.main_activity_menu_search).setVisibility(View.VISIBLE);
    }

    private void hideSearch() {
        if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.GONE);
        }
        findViewById(R.id.main_activity_menu_search).setVisibility(View.GONE);
    }

    // DRAWER configuration //
    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                alimDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        //Use Navigation Callback listener as follows
        NavigationView navigationView = findViewById(R.id.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userMail = header.findViewById(R.id.drawer_user_mail);
        userName = header.findViewById(R.id.drawer_user_name);
        userPicture = header.findViewById(R.id.drawer_user_picture);

        ImageButton logo_button = findViewById(R.id.activity_main_drawer_logo);

        logo_button.setOnClickListener(v -> {
            // quit the app
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        });
    }

    // ----------- DATA -------------------- //
    private void alimDrawer() {
        User mUser;
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        mUser = userSingletonRepository.getUser();

        userName.setText(mUser.getUserName());
        userMail.setText(mUser.getUserMail());
        if (mUser.getUrlPicture() != null) {
            Glide.with(getApplicationContext())
                    .load(Objects.requireNonNull(mUser.getUrlPicture()))
                    .circleCrop()
                    .into(userPicture);
        }
        restaurantChoiceId = mUser.getChosenRestaurantId();
    }


    // ------------------------ DRAWER options INTERFACE  -----------------------//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final int main_drawer_lunch_id = R.id.activity_main_drawer_lunch;
        final int main_drawer_settings_id = R.id.activity_main_drawer_settings;
        final int main_drawer_logout_id = R.id.activity_main_drawer_logout;
        switch (id) {
            // "YOUR LUNCH"
            case main_drawer_lunch_id:
                yourLunchClick();
                return true;
            // "SETTINGS"
            case main_drawer_settings_id:
                settings();
                return true;
            // "LOGOUT"
            case main_drawer_logout_id:
                deleteAuthAndLogOut();
                return true;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void yourLunchClick() {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        if (restaurantChoiceId == null || restaurantChoiceId.equals("")) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.main_activity_lunch_no_restaurant_chosen, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            DetailsUtil.openDetailsFragment(this, restaurantChoiceId);
        }
    }

    private void settings() {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        Fragment fragment = new SettingFragment();
        showFragment(fragment);
    }

    // ----------------------------- LOGOUT ----------------------------//
    private void deleteAuthAndLogOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // ------------------- FRAGMENT Navigations ----------------------//
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // DRAWER closed with back button
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(currentFragment instanceof MapFragment
                || currentFragment instanceof RestaurantsFragment
                || currentFragment instanceof WorkmatesFragment)) {
            super.onBackPressed();
        }
    }

    // ------------------- HANDLES LOCATION PERMISSIONS RESPONSE ---------------------//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Received permission result for location permission.
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted, preview can be displayed
                showFragment(new MapFragment());
            } else {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission was NOT granted in MapFragment view.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == RestaurantsFragment.RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Received permission result for location permission.
            Log.i(TAG, "Received response for LOCATION permission request from RestaurantFragment.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted, return to MapView to show the restaurants
                Log.i(TAG, "LOCATION permission has now been granted in RestaurantsFragment view.");
                showFragment(new MapFragment());
            } else {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission was NOT granted.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ---------------------- AUTOCOMPLETE SEARCH -----------------------//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int main_activity_menu_search = R.id.main_activity_menu_search;
        final int main_activity_menu_clear = R.id.main_activity_menu_clear;

        switch (id) {
            // Autocomplete lens
            case main_activity_menu_search:
                showOrHideAutocompleteItem();
                return true;
            // Autocomplete clear
            case main_activity_menu_clear:
                autoCompleteTextView.setText("");
                hideAutocompleteItem();
                reinitMap();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reinitMap() {
        Location userLocation = (listRestaurantViewModel.getLocationForReinitMap());
        String userLocationStr = userLocation.getLat() + "," + userLocation.getLng();

        listRestaurantViewModel.initListViewMutableLiveData(userLocationStr);
    }

    private void showOrHideAutocompleteItem() {
        if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.GONE);
            clearButton.setVisible(false);
        } else {
            autoCompleteTextView.setVisibility(View.VISIBLE);
            clearButton.setVisible(true);
        }
    }
    private void hideAutocompleteItem() {
        clearButton.setVisible(false);
        autoCompleteTextView.setVisibility(View.GONE);
    };
    private void autoCompleteTextListener() {
        // call APIAutocomplete when typing in search
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    listRestaurantViewModel.getAutocomplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    clearButton.setVisible(true);
                    findViewById(R.id.main_activity_menu_search).setVisibility(View.GONE);
                } else {
                    clearButton.setVisible(false);
                    findViewById(R.id.main_activity_menu_search).setVisibility(View.VISIBLE);
                    reinitMap();
                }
            }
        });

        listRestaurantViewModel.getListRestaurantAutoComplete().observe(this, this::filterAutocompleteResults);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            // Handles no results click
            MutableLiveData<List<PredictionAPIAutocomplete>> predictions = listRestaurantViewModel.getListRestaurantAutoComplete();
            if (parent.getItemAtPosition(position) == getResources().getString(R.string.autoCompleteTextView_noresult)) {
                autoCompleteTextView.setText("");
            } else {
                // Handles click on an autocomplete search dropdown result
                for (PredictionAPIAutocomplete prediction :
                        Objects.requireNonNull(predictions.getValue())) {
                    if (parent.getItemAtPosition(position) == prediction.getStructured_formatting().getMain_text()) {
                        String placeId = prediction.getPlace_id();
                        // this method updates the lRestaurantLiveData used by an observe in each fragment.
                        // Here we will show only the selected restaurant
                        alimlRestaurantMutableLiveData(placeId);
                    }
                }
            }
        });
    }

    private void alimlRestaurantMutableLiveData(String placeId) {
        listRestaurantViewModel.callPlaces(placeId);
        listRestaurantViewModel.getDetailRestaurant().observe(this,
                changedDetailRestaurant -> {
                    System.out.println("je passe dans l'observe" + changedDetailRestaurant.get(0).getPlaceId());
                });
    }

    private void filterAutocompleteResults(List<PredictionAPIAutocomplete> predictionAPIAutocompletes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_dropdown_item_1line);
        adapter.setNotifyOnChange(true);
        //attach the adapter to textview
        autoCompleteTextView.setAdapter(adapter);

        for (PredictionAPIAutocomplete prediction : predictionAPIAutocompletes) {
            if (prediction.getTypes().contains(API_AUTOCOMPLETE_FILTER_KEYWORD)) {
                // only return places as establishment of type "restaurant"
                Log.d(TAG, "getAutocompleteSearch : prediction = " + prediction.getDescription());
                adapter.add(prediction.getStructured_formatting().getMain_text());
                adapter.notifyDataSetChanged();
            }
        }
        if (adapter.getCount() == 0) {
            adapter.add(getResources().getString(R.string.autoCompleteTextView_noresult));
            adapter.notifyDataSetChanged();
        }
    }
}
