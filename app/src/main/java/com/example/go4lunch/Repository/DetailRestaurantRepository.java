package com.example.go4lunch.Repository;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.details.ResultAPIDetails;
import com.example.go4lunch.Model.details.ResultsAPIDetails;
import com.example.go4lunch.Service.APIClient;
import com.example.go4lunch.Service.APIRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurantRepository {

    public DetailRestaurantRepository() {
    }
    private static final String TAG = "PlaceDetailsService";
    private static final String API_MAP_FIELDS = "formatted_address,geometry,photos,place_id,name,rating,opening_hours,website,international_phone_number";
    // Restaurant Detail from API Place
    public static final MutableLiveData<ResultAPIDetails> placeDetailResults = new MutableLiveData<>();

    public void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(
                placeId,
                API_MAP_FIELDS,
                Resources.getSystem().getConfiguration().locale.getLanguage(),
                BuildConfig.GOOGLE_MAPS_KEY);

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NonNull Call<ResultsAPIDetails> call,
                                   @NonNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        placeDetailResults.setValue(body.getResult());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResultsAPIDetails> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
                placeDetailResults.setValue(null);
            }
        });
    }

    public MutableLiveData<ResultAPIDetails> getRestaurantDetails() {
        return placeDetailResults;
    }

}

