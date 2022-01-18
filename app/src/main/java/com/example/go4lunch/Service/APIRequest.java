package com.example.go4lunch.Service;

import com.example.go4lunch.Model.autocomplete.PredictionsAPIAutocomplete;
import com.example.go4lunch.Model.details.ResultsAPIDetails;
import com.example.go4lunch.Model.map.ResultsAPIMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRequest {
    // Request used in Restaurant to get the list of Restaurants. This list will be transform in Live Data to be used in
    // the mapFragment and in the ListView. By default the list will be sorted by distance.
    @GET("place/nearbysearch/json?rankby=distance")
    Call<ResultsAPIMap> getNearbyPlacesSortDistanceASC(
            @Query("location") String location,
            @Query("language") String language,
            @Query("keyword") String keyword,
            @Query("key") String key
    );

    @GET("place/nearbysearch/json")
    Call<ResultsAPIMap> getNearbyPlacesNextPage(
            @Query("pagetoken") String pagetoken,
            @Query("key") String key
    );

    @GET("place/details/json")
    Call<ResultsAPIDetails> getPlaceDetails(
            @Query("place_id") String place_id,
            @Query("fields") String fields,
            @Query("language") String language,
            @Query("key") String key
    );


    @GET("place/autocomplete/json")
    Call<PredictionsAPIAutocomplete> getAutocomplete(
            @Query("location") String location,
            @Query("radius") Integer radius,
            @Query("input") String input,
            @Query("types") String types,
            @Query("strictbounds") String strictbounds,
            @Query("key") String key

    );
}