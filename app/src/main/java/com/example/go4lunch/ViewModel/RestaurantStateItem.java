package com.example.go4lunch.ViewModel;

import com.example.go4lunch.Model.map.LocationAPIMap;
import com.example.go4lunch.Model.map.PhotoAttributesAPIMap;

import java.util.List;

public class RestaurantStateItem {
    private String placeId;
    private String name;
    private Float rating;
    private Boolean open_false;
    private String vicinity;
    private LocationAPIMap restaurantLocation;
    private Integer restaurantDistance;
    private List<PhotoAttributesAPIMap> photos;
    private Integer nbWorkmates;

    public RestaurantStateItem(String placeId, String name, Float rating, Boolean open_false,
                               String vicinity, LocationAPIMap restaurantLocation, Integer restaurantDistance,
                               List<PhotoAttributesAPIMap> photos, Integer nbWorkmates) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.open_false = open_false;
        this.vicinity = vicinity;
        this.restaurantLocation = restaurantLocation;
        this.restaurantDistance = restaurantDistance;
        this.photos = photos;
        this.nbWorkmates = nbWorkmates;
    }

    public RestaurantStateItem() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setOpen_false(Boolean open_false) {
        this.open_false = open_false;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public LocationAPIMap getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(LocationAPIMap restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public Integer getRestaurantDistance() {
        return restaurantDistance;
    }

    public void setRestaurantDistance(Integer restaurantDistance) {
        this.restaurantDistance = restaurantDistance;
    }

    public List<PhotoAttributesAPIMap> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoAttributesAPIMap> photos) {
        this.photos = photos;
    }

    public Integer getNbWorkmates() {
        return nbWorkmates;
    }

    public void setNbWorkmates(Integer nbWorkmates) {
        this.nbWorkmates = nbWorkmates;
    }
    public Boolean getOpenNow() {
        return open_false;
    }
}

