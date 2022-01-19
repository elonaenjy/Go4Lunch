package com.example.go4lunch.Repository;

import static com.example.go4lunch.Activity.Main.Fragments.Map.MapFragment.userLocationStr;
import static com.example.go4lunch.Activity.Main.MainActivity.RADIUS_MAX;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.autocomplete.PredictionAPIAutocomplete;
import com.example.go4lunch.Model.autocomplete.PredictionsAPIAutocomplete;
import com.example.go4lunch.Service.APIClient;
import com.example.go4lunch.Service.APIRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AutoCompleteRepository {

    private static final String TAG = "AutoCompleteRepository";

    public static final String API_AUTOCOMPLETE_KEYWORD = "establishment";

    public static final MutableLiveData<List<PredictionAPIAutocomplete>> listenAutoCompletePredictions = new MutableLiveData<>();

    public void getAutocomplete(String input) {
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
                listenAutoCompletePredictions.setValue(null);
            }
        });
    }

    public LiveData<List<PredictionAPIAutocomplete>> getListAutoComplete() {
        return listenAutoCompletePredictions;
    }
}

