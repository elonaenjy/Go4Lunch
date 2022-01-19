package com.example.go4lunch.ViewModel;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Model.User;
import com.example.go4lunch.Model.autocomplete.PredictionAPIAutocomplete;
import com.example.go4lunch.Model.map.Location;
import com.example.go4lunch.Model.map.LocationAPIMap;
import com.example.go4lunch.Model.map.OpenStatutAPIMap;
import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.Repository.AutoCompleteRepository;
import com.example.go4lunch.Repository.LocationRepository;
import com.example.go4lunch.Repository.NearByPlacesRepository;
import com.example.go4lunch.Repository.UserRepository;
import com.example.go4lunch.Service.PermissionChecker;
import com.example.go4lunch.Util.SortRestaurantsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListRestaurantViewModel extends ViewModel {
    NearByPlacesRepository nearByPlaces;
    AutoCompleteRepository autoCompleteRepository;
    public static MutableLiveData<List<ResultAPIMap>> lRestaurantMutableLiveData = new MutableLiveData<>();
    public static MutableLiveData<List<User>> lWorkmatesLiveData = new MutableLiveData<>();

    @NonNull
    private MutableLiveData<ArrayList<RestaurantStateItem>> lRestaurantStateItemLiveData = new MutableLiveData<>();

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;

    @NonNull
    private final LiveData<Location> gpsMessageLiveData;

    UserRepository userRepository;

    public ListRestaurantViewModel(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull NearByPlacesRepository nearByPlaces,
            @NonNull UserRepository userRepository,
            AutoCompleteRepository autoCompleteRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.nearByPlaces = nearByPlaces;
        this.userRepository = userRepository;
        this.autoCompleteRepository = autoCompleteRepository;

        getLocation();

        gpsMessageLiveData = Transformations.map(locationRepository.getLocationLiveData(), location -> location);
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        // No GPS permission
        if (!permissionChecker.hasLocationPermission()) {
            locationRepository.stopLocationRequest();
        } else {
            locationRepository.getLocationRequest();
        }
    }

    public LiveData<Location> getGpsMessageLiveData() {
        return Objects.requireNonNull(gpsMessageLiveData);
    }

    public void initListViewMutableLiveData(String userLocationStr) {
        lRestaurantMutableLiveData = nearByPlaces.getNearbyPlacesSortDistanceASC(userLocationStr);
    }

    //Mapping data from remote source to view data
    private LiveData<List<RestaurantStateItem>> mapDataToViewState
            (LiveData<List<ResultAPIMap>> lRestaurantMutableLiveData, List<User> listWorkmates) {
        return Transformations.map(lRestaurantMutableLiveData, restaurantMutableLiveData -> {
            ArrayList<RestaurantStateItem> listRestaurantStateItems = new ArrayList<>();
            lRestaurantStateItemLiveData = new MutableLiveData<>();
            for (ResultAPIMap result : restaurantMutableLiveData) {
                listRestaurantStateItems.add(new RestaurantStateItem(
                        result.getPlaceId(),
                        result.getName().toLowerCase(),
                        result.getRating(),
                        defineStatut(result.getOpenNow()),
                        result.getVicinity(),
                        result.getGeometry().getLocation(),
                        (int) Math.round(computeDistance(result.getGeometry().getLocation())),
                        result.getPhotos(),
                        computeNbWorkmates(result.getPlaceId(), listWorkmates)));
            }
            lRestaurantStateItemLiveData.setValue(listRestaurantStateItems);
            return listRestaurantStateItems;
        });
    }

    public LiveData<List<RestaurantStateItem>> getListRestaurants() {
        List lWorkmates = userRepository.getAllUsersExceptCurrentUser().getValue();
        return mapDataToViewState(lRestaurantMutableLiveData, lWorkmates);
    }

    public MutableLiveData<ArrayList<RestaurantStateItem>> sortAZ() {
        ArrayList<RestaurantStateItem> listRestaurants = SortRestaurantsUtil.sortAZ(lRestaurantStateItemLiveData.getValue());
        lRestaurantStateItemLiveData.setValue(listRestaurants);
        return lRestaurantStateItemLiveData;
    }

    public MutableLiveData<ArrayList<RestaurantStateItem>> sortZA() {
        ArrayList<RestaurantStateItem> listRestaurants = SortRestaurantsUtil.sortZA(lRestaurantStateItemLiveData.getValue());
        lRestaurantStateItemLiveData.setValue(listRestaurants);
        return lRestaurantStateItemLiveData;
    }

    public MutableLiveData<ArrayList<RestaurantStateItem>> sortRatingDesc() {
        ArrayList<RestaurantStateItem> listRestaurants = SortRestaurantsUtil.sortRatingDesc(lRestaurantStateItemLiveData.getValue());
        lRestaurantStateItemLiveData.setValue(listRestaurants);
        return lRestaurantStateItemLiveData;
    }

    public MutableLiveData<ArrayList<RestaurantStateItem>> sortNbWorkmatesDesc() {
        ArrayList<RestaurantStateItem> listRestaurants = SortRestaurantsUtil.sortWorkmatesDesc(lRestaurantStateItemLiveData.getValue());
        lRestaurantStateItemLiveData.setValue(listRestaurants);
        return lRestaurantStateItemLiveData;
    }

    private Double computeDistance(LocationAPIMap restaurantLocation) {
        double distance = 0;
        Location rLocation = new Location(restaurantLocation.getLat(), restaurantLocation.getLng());
        Location userLocation = locationRepository.getLocationLiveData().getValue();

        distance = Location.computeDistance(userLocation, rLocation);
        return distance;
    }

    private Boolean defineStatut(OpenStatutAPIMap openingNow) {
        Boolean statut;
        if (openingNow != null) {
            statut = openingNow.getOpen_now();
        } else {
            statut = false;
        }
        return statut;
    }

    private Integer computeNbWorkmates(String placeId, List<User> listWorkmates) {
        int nbWorkmates = 0;
        for (User result : listWorkmates) {
            if ((result.getChosenRestaurantId() != null) && (result.getChosenRestaurantId().equals(placeId))) {
                nbWorkmates++;
            }
        }
        return nbWorkmates;
    }

    public void getListRestaurantAutoCompletevalue(List<PredictionAPIAutocomplete> listRestaurantAutoCompletevalue) {
        List<ResultAPIMap> lRestaurantNearByPlaces = nearByPlaces.lRestaurants().getValue();
        ArrayList<ResultAPIMap> lrestaurantAPIForAutoComplete = new ArrayList<>();
        int sizeListAutoComplete = listRestaurantAutoCompletevalue.size();
        int position;
        for (position = 0; position < sizeListAutoComplete; position++) {
            String placeId = listRestaurantAutoCompletevalue.get(position).getPlace_id();
            int position2;
            for (position2 = 0; position2 < Objects.requireNonNull(lRestaurantNearByPlaces).size(); position2++) {
                if (lRestaurantNearByPlaces.get(position2).getPlaceId().equals(placeId)) {
                    lrestaurantAPIForAutoComplete.add(lRestaurantNearByPlaces.get(position2));
                }
            }
        }
        lRestaurantMutableLiveData.setValue(lrestaurantAPIForAutoComplete);
    }

    public Location getLocationForReinitMap() {
        return locationRepository.getLocationLiveData().getValue();
    }

    public void getAutocomplete(String toString) {

    }
}