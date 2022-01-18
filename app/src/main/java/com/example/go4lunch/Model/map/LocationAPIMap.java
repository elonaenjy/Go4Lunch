package com.example.go4lunch.Model.map;

import com.google.gson.annotations.SerializedName;

public class LocationAPIMap  {
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
