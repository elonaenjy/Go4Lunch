package com.example.go4lunch.Model;

import java.text.DateFormat;

public class RestaurantChoice {
    String userId;
    String choosenRestaurantId;
    String choosenRestaurantName;
    String choosenRestaurantAdress;
    String choosenDate;

    public RestaurantChoice(String userId, String choosenRestaurantId, String choosenRestaurantName,
                            String choosenRestaurantAdress, String choosenDate) {
        this.userId = userId;
        this.choosenRestaurantId = choosenRestaurantId;
        this.choosenRestaurantName = choosenRestaurantName;
        this.choosenRestaurantAdress = choosenRestaurantAdress;
        this.choosenDate = choosenDate;
    }

    public RestaurantChoice() {
    }

    public String getChoosenRestaurantId() {
        return choosenRestaurantId;
    }

    public void setChoosenRestaurantId(String choosenRestaurantId) {
        this.choosenRestaurantId = choosenRestaurantId;
    }

    public String getChoosenRestaurantName() {
        return choosenRestaurantName;
    }

    public void setChoosenRestaurantName(String choosenRestaurantName) {
        this.choosenRestaurantName = choosenRestaurantName;
    }

    public String getChoosenRestaurantAdress() {
        return choosenRestaurantAdress;
    }

    public void setChoosenRestaurantAdress(String choosenRestaurantAdress) {
        this.choosenRestaurantAdress = choosenRestaurantAdress;
    }

    public String getChoosenDate() {
        return choosenDate;
    }

    public void setChoosenDate(String choosenDate) {
        this.choosenDate = choosenDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
