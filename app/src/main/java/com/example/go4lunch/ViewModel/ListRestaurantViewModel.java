package com.example.go4lunch.ViewModel;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Model.User;
import com.example.go4lunch.Model.autocomplete.PredictionAPIAutocomplete;
import com.example.go4lunch.Model.details.ResultAPIDetails;
import com.example.go4lunch.Model.map.GeometryAPIMap;
import com.example.go4lunch.Model.map.Location;
import com.example.go4lunch.Model.map.LocationAPIMap;
import com.example.go4lunch.Model.map.OpenStatutAPIMap;
import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.Repository.AutoCompleteRepository;
import com.example.go4lunch.Repository.DetailRestaurantRepository;
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
    DetailRestaurantRepository detailRestaurantRepository;
    public static MutableLiveData<List<ResultAPIMap>> lRestaurantMutableLiveData = new MutableLiveData<>();
    public static MutableLiveData<List<User>> lWorkmatesLiveData = new MutableLiveData<>();
    public static MutableLiveData<ResultAPIDetails> dRestaurant = new MutableLiveData<>();

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
            AutoCompleteRepository autoCompleteRepository,
            DetailRestaurantRepository detailRestaurantRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.nearByPlaces = nearByPlaces;
        this.userRepository = userRepository;
        this.autoCompleteRepository = autoCompleteRepository;
        this.detailRestaurantRepository = detailRestaurantRepository;

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
        return Transformations.map(lRestaurantMutableLiveData, restaurant -> {
            ArrayList<RestaurantStateItem> listRestaurantStateItems = new ArrayList<>();
            lRestaurantStateItemLiveData = new MutableLiveData<>();
            for (ResultAPIMap result : restaurant) {
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
        List<User> lWorkmates = userRepository.getAllUsersExceptCurrentUser().getValue();
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

    public Double distance(Location oldLocation, Location newLocation) {
        return Location.computeDistance(oldLocation, newLocation);
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

    public Location getLocationForReinitMap() {
        return locationRepository.getLocationLiveData().getValue();
    }

    public void getAutocomplete(String placeId) {
        autoCompleteRepository.getAutocomplete(placeId);
    }

    public MutableLiveData<List<PredictionAPIAutocomplete>> getListRestaurantAutoComplete() {
        return autoCompleteRepository.getListAutoComplete();
    }

    public void callPlaces(String placeId) {
        // method to get the place information by the api Place - Format ResultAPIDetail
        dRestaurant = detailRestaurantRepository.getPlaceDetails(placeId);

    }

    public LiveData<ArrayList<ResultAPIMap>> getDetailRestaurant() {
        detailRestaurantRepository.getRestaurantDetails();
        return mapDataToResulAPIMap(dRestaurant);
    }

    private LiveData<ArrayList<ResultAPIMap>> mapDataToResulAPIMap(LiveData<ResultAPIDetails> dRestaurant) {
        return Transformations.map(dRestaurant, detailRestaurant -> {
            ArrayList<ResultAPIMap> lRestauDetail = new ArrayList();
            ResultAPIMap restauDetail = new ResultAPIMap();
            // define boolean openNow
            Boolean openNow = detailRestaurant.getOpening_hours().getOpen_now();
            OpenStatutAPIMap open_Now = new OpenStatutAPIMap();
            open_Now.setOpen_now(openNow);
            // define location
            GeometryAPIMap geometryApiMapRestau = new GeometryAPIMap();
            geometryApiMapRestau.setLocation(detailRestaurant.getGeometry().getLocation());
            // alim ResulAPIMap format
            restauDetail.setPlaceId(detailRestaurant.getPlaceId());
            restauDetail.setName(detailRestaurant.getName());
            restauDetail.setVicinity(detailRestaurant.getVicinity());
            restauDetail.setRating(detailRestaurant.getRating());
            restauDetail.setPhotos(detailRestaurant.getPhotos());
            restauDetail.setOpen_now(open_Now);
            restauDetail.setGeometry(geometryApiMapRestau);
            lRestauDetail.add(restauDetail);
            lRestaurantMutableLiveData.setValue(lRestauDetail);
            return lRestauDetail;
        });
    }

}

