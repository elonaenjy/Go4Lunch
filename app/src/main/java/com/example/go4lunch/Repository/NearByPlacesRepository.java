package com.example.go4lunch.Repository;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.Model.map.ResultsAPIMap;
import com.example.go4lunch.Service.APIClient;
import com.example.go4lunch.Service.APIRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByPlacesRepository {
    private static final String TAG = "NearByPlacesRepository";
    public static final String API_MAP_KEYWORD = "restaurant";
    // The Places API lets you search for place information
    // using a variety of categories, including establishments, prominent points of interest,
    // and geographic locations. You can search for places either by proximity or a text string.
    // A Place Search returns a list of places along with summary information about each place;
    // additional information is available via a Place Details query.
    // Here we use the keywords "Restaurants" to identify the interesting places for the application

    // Nearby Places API variables
    private static final MutableLiveData<List<ResultAPIMap>> listenNearbyPlacesResults = new MutableLiveData<>();

    public MutableLiveData<List<ResultAPIMap>> getNearbyPlacesSortDistanceASC(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        if (userLocationStr != null) {
            Call<ResultsAPIMap> nearbyPlaces = apiMap.getNearbyPlacesSortDistanceASC(
                    userLocationStr,
                    Resources.getSystem().getConfiguration().locale.getLanguage(),
                    API_MAP_KEYWORD,
                    BuildConfig.GOOGLE_MAPS_KEY);

            nearbyPlaces.enqueue(new Callback<ResultsAPIMap>() {
                @Override
                public void onResponse(@NonNull Call<ResultsAPIMap> call, @NonNull Response<ResultsAPIMap> response) {
                    if (response.isSuccessful()) {
                        ResultsAPIMap body = response.body();
                        if (body != null) {
                            listenNearbyPlacesResults.setValue(body.getResults());
                            // Handle more than 40 results
                            if (body.getNext_page_token() != null) {
                                getNearbyPlacesNextPage(body.getNext_page_token());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultsAPIMap> call, @NonNull Throwable t) {
                    Log.d(TAG, "getPlace failure" + t);
                }
            });
        }
        return listenNearbyPlacesResults;
    }

    public static void getNearbyPlacesNextPage(String nextPageToken) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIMap> nearbyPlacesNextPage = apiMap.getNearbyPlacesNextPage(
                nextPageToken,
                BuildConfig.GOOGLE_MAPS_KEY);

        nearbyPlacesNextPage.enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(@NonNull Call<ResultsAPIMap> call, @NonNull Response<ResultsAPIMap> response) {
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        List<ResultAPIMap> resultsWithNextPageToken = new ArrayList<>();
                        resultsWithNextPageToken.addAll(Objects.requireNonNull(listenNearbyPlacesResults.getValue()));
                        resultsWithNextPageToken.addAll(body.getResults());
                        listenNearbyPlacesResults.setValue(resultsWithNextPageToken);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsAPIMap> call, @NonNull Throwable t) {
                Log.d(TAG, "getPlace failure" + t);
            }
        });
    }

    public MutableLiveData<List<ResultAPIMap>> lRestaurants() {
        return listenNearbyPlacesResults;
    }
}