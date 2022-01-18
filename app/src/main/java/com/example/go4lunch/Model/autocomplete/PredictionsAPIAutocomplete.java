package com.example.go4lunch.Model.autocomplete;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredictionsAPIAutocomplete {

    @SerializedName("status")
    private final String status;

    @SerializedName("predictions")
    private final List<PredictionAPIAutocomplete> predictions;

    public PredictionsAPIAutocomplete(String status, List<PredictionAPIAutocomplete> predictions) {
        this.status = status;
        this.predictions = predictions;
    }

    public List<PredictionAPIAutocomplete> getPredictions() {
        return predictions;
    }

}
