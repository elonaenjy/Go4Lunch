package com.example.go4lunch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.Repository.DetailRestaurantRepository;
import com.example.go4lunch.Repository.SharedPreferencesRepository;
import com.example.go4lunch.Repository.UserLikingRestaurantRepository;
import com.example.go4lunch.Repository.LocationRepository;
import com.example.go4lunch.Repository.NearByPlacesRepository;
import com.example.go4lunch.Repository.UserRepository;
import com.example.go4lunch.Repository.UserSingletonRepository;
import com.example.go4lunch.Service.PermissionChecker;
import com.example.go4lunch.ViewModel.DetailRestaurantViewModel;
import com.example.go4lunch.ViewModel.SettingViewModel;
import com.example.go4lunch.ViewModel.WorkmateViewModel;
import com.example.go4lunch.ViewModel.ListRestaurantViewModel;
import com.google.android.gms.location.LocationServices;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private volatile static ViewModelFactory sInstance;

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;

    @NonNull
    private final NearByPlacesRepository nearByPlacesRepository;

    @NonNull
    private final UserRepository userRepository;
    private final DetailRestaurantRepository detailRestaurantRepository;
    private final UserLikingRestaurantRepository userLikingRestaurantRepository;
    private final SharedPreferencesRepository sharedPreferencesRepository;

    public static ViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (ViewModelFactory.class) {
                if (sInstance == null) {
                    Application application = MainApplication.getApplication();

                    sInstance = new ViewModelFactory(
                            new PermissionChecker(application),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(application)),
                            new NearByPlacesRepository(),
                            new UserRepository(),
                            new DetailRestaurantRepository(),
                            new UserLikingRestaurantRepository(),
                            new SharedPreferencesRepository()
                    );
                }
            }
        }

        return sInstance;
    }

    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull NearByPlacesRepository nearByPlacesRepository,
            @NonNull UserRepository userRepository,
            @NonNull DetailRestaurantRepository detailRestaurantRepository,
            UserLikingRestaurantRepository userLikingRestaurantRepository,
            SharedPreferencesRepository sharedPreferencesRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.nearByPlacesRepository = nearByPlacesRepository;
        this.userRepository = userRepository;
        this.detailRestaurantRepository = detailRestaurantRepository;
        this.userLikingRestaurantRepository = userLikingRestaurantRepository;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListRestaurantViewModel.class)) {
            return (T) new ListRestaurantViewModel(
                    permissionChecker,
                    locationRepository,
                    nearByPlacesRepository,
                    userRepository
            );
        } else if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel(
                    userRepository
            );
        } else if (modelClass.isAssignableFrom(DetailRestaurantViewModel.class)) {
            return (T) new DetailRestaurantViewModel(
                    detailRestaurantRepository,
                    userRepository,
                    userLikingRestaurantRepository
            );
        } else if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(
                    sharedPreferencesRepository
            );
        } else
            throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}