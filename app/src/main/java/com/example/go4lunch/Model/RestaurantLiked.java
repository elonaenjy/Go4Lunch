package com.example.go4lunch.Model;

import java.util.ArrayList;

public class RestaurantLiked  {
    private String placeId;
    private String uId;

    public RestaurantLiked(String placeId, String uId) {
        this.uId = uId;
        this.placeId = placeId;
    }

    public RestaurantLiked() {
    }
    public String getUid() {
        return uId;
    }

    public void setUid(String uid) {
        this.uId = uid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
