package com.example.go4lunch.ViewModel;

import com.example.go4lunch.Model.details.OpeningHoursAPIDetails;
import com.example.go4lunch.Model.map.PhotoAttributesAPIMap;

import java.util.List;

public class DetailRestaurantStateItem {
    private String placeId;
    private String name;
    private OpeningHoursAPIDetails openingHours;
    private Float rating;
    private String website;
    private String internationalPhoneNumber;
    private String formatted_address;
    private List<PhotoAttributesAPIMap> photos;
    private Boolean userLike;
    private String choosenRestaurantName;
    private String choosenRestaurantId;
    private String choosenRestaurantAdress;

    public DetailRestaurantStateItem(String placeId, String name, OpeningHoursAPIDetails openingHours, Float rating,
                                     String website, String internationalPhoneNumber, String formatted_address,
                                     List<PhotoAttributesAPIMap> photos,
                                     boolean b, Boolean userLike, String choosenRestaurantName, String choosenRestaurantId,
                                     String choosenRestaurantAdress) {
        this.placeId = placeId;
        this.name = name;
        this.openingHours = openingHours;
        this.rating = rating;
        this.website = website;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.formatted_address = formatted_address;
        this.photos = photos;
        this.userLike = userLike;
        this.choosenRestaurantName = choosenRestaurantName;
        this.choosenRestaurantId = choosenRestaurantId;
        this.choosenRestaurantAdress = choosenRestaurantAdress;
    }

    public DetailRestaurantStateItem() {
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

    public OpeningHoursAPIDetails getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHoursAPIDetails openingHours) {
        this.openingHours = openingHours;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public List<PhotoAttributesAPIMap> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoAttributesAPIMap> photos) {
        this.photos = photos;
    }

    public Boolean getUserLike() {
        return userLike;
    }

    public void setUserLike(Boolean userLike) {
        this.userLike = userLike;
    }

    public String getChoosenRestaurantName() {
        return choosenRestaurantName;
    }

    public void setChoosenRestaurantName(String choosenRestaurantName) {
        this.choosenRestaurantName = choosenRestaurantName;
    }

    public String getChoosenRestaurantId() {
        return choosenRestaurantId;
    }

    public void setChoosenRestaurantId(String choosenRestaurantId) {
        this.choosenRestaurantId = choosenRestaurantId;
    }

    public String getChoosenRestaurantAdress() {
        return choosenRestaurantAdress;
    }

    public void setChoosenRestaurantAdress(String choosenRestaurantAdress) {
        this.choosenRestaurantAdress = choosenRestaurantAdress;
    }
}
