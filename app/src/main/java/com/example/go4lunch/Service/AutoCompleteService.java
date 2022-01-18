package com.example.go4lunch.Service;

import static com.example.go4lunch.Activity.Main.Fragments.Map.MapFragment.userLocationStr;
import static com.example.go4lunch.Activity.Main.MainActivity.RADIUS_MAX;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.autocomplete.PredictionAPIAutocomplete;
import com.example.go4lunch.Model.autocomplete.PredictionsAPIAutocomplete;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AutoCompleteService {

    private static final String TAG = "AutoCompleteService";

    public static final String API_AUTOCOMPLETE_KEYWORD = "restaurant";

    public static final MutableLiveData<List<PredictionAPIAutocomplete>> listenAutoCompletePredictions = new MutableLiveData<>();
    public static final LiveData<List<PredictionAPIAutocomplete>> predictions = listenAutoCompletePredictions;

    public static void getAutocomplete(String input) {
        APIRequest apiAutocomplete = APIClient.getClient().create(APIRequest.class);
        Call<PredictionsAPIAutocomplete> autocompleteSearch = apiAutocomplete.getAutocomplete(
                userLocationStr,
                RADIUS_MAX,
                input,
                API_AUTOCOMPLETE_KEYWORD,
                "",
                BuildConfig.GOOGLE_MAPS_KEY);
        autocompleteSearch.enqueue(new Callback<PredictionsAPIAutocomplete>() {
            @Override
            public void onResponse(@NonNull Call<PredictionsAPIAutocomplete> call,
                                   @NonNull Response<PredictionsAPIAutocomplete> response) {
                if (response.isSuccessful()) {
                    PredictionsAPIAutocomplete predictionsAPIAutocomplete = response.body();
                    if (predictionsAPIAutocomplete != null) {
                        listenAutoCompletePredictions.setValue(predictionsAPIAutocomplete.getPredictions());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PredictionsAPIAutocomplete> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "getPlace API failure" + t);
            }
        });
    }
}

