package com.example.go4lunch.Model.map;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.SphericalUtil;

public class Location {
    public Location(Double lat,Double lng){
        this.lat = lat;
        this.lng = lng;
    }
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lng")
    @Expose
    public Double lng;

    public Location() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public static Double computeDistance(Location startLocation, Location endLocation) {
        LatLng from = new LatLng(startLocation.getLat(),startLocation.getLng());
        LatLng to = new LatLng(endLocation.getLat(),endLocation.getLng());

        Double dis = SphericalUtil.computeDistanceBetween(from,to);

        return dis;
    }

}
