package com.example.go4lunch.Model.map;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultAPIMap implements Serializable {

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private Float rating;

    @SerializedName("opening_hours")
    private OpenStatutAPIMap open_now;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("geometry")
    private GeometryAPIMap geometry;

    @SerializedName("photos")
    private List<PhotoAttributesAPIMap> photos;

    @SerializedName("types")
    private List<String> types;

    // --- GETTERS --- //

    public ResultAPIMap(String placeId, String name, Float rating, OpenStatutAPIMap open_now,
                        String vicinity, GeometryAPIMap geometry,
                        List<PhotoAttributesAPIMap> photos, List<String> types) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.open_now = open_now;
        this.vicinity = vicinity;
        this.geometry = geometry;
        this.photos = photos;
        this.types = types;
    }

    public ResultAPIMap(String placeId, String name, Float rating, OpenStatutAPIMap open_now,
                        String vicinity, LocationAPIMap restaurantLocation,
                        List<PhotoAttributesAPIMap> photos) {
    }

    public ResultAPIMap() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public Float getRating() {
        return rating;
    }

    public OpenStatutAPIMap getOpenNow() {
        return open_now;
    }

    public String getVicinity() {
        return vicinity;
    }

    public GeometryAPIMap getGeometry() {
        return geometry;
    }

    public List<PhotoAttributesAPIMap> getPhotos() {
        return photos;
    }

    public List<String> getTypes() {
        return types;
    }

    // --- SETTERS --- For test Purpose //
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
