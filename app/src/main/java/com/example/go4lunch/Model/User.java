package com.example.go4lunch.Model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class User {
    private String uid;
    private String userName;
    private String userMail;
    @Nullable
    private String urlPicture;
    @Nullable
    private String chosenRestaurantId;
    @Nullable
    private String chosenRestaurantName;
    @Nullable
    public String chosenRestaurantAdress;
    @Nullable
    public String chosenRestaurantDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uid, user.uid) && Objects.equals(userName, user.userName) && Objects.equals(userMail, user.userMail) && Objects.equals(urlPicture, user.urlPicture) && Objects.equals(chosenRestaurantId, user.chosenRestaurantId) && Objects.equals(chosenRestaurantName, user.chosenRestaurantName) && Objects.equals(chosenRestaurantAdress, user.chosenRestaurantAdress) && Objects.equals(chosenRestaurantDate, user.chosenRestaurantDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, userName, userMail, urlPicture, chosenRestaurantId, chosenRestaurantName, chosenRestaurantAdress, chosenRestaurantDate);
    }

    public User(String uid, String userName, String userMail, String urlPicture, String userChoosenRestaurantId,
                String userChoosenRestaurantName, String userChoosenRestaurantAdress, String chosenRestaurantDate) {
        this.uid = uid;
        this.userName = userName;
        this.userMail = userMail;
        this.urlPicture = urlPicture;
        this.chosenRestaurantId = userChoosenRestaurantId;
        this.chosenRestaurantName = userChoosenRestaurantName;
        this.chosenRestaurantAdress = userChoosenRestaurantAdress;
        this.chosenRestaurantDate = chosenRestaurantDate;
    }

      public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getChosenRestaurantId() {
        return chosenRestaurantId;
    }

    public void setChosenRestaurantId(String chosenRestaurantId) {
        this.chosenRestaurantId = chosenRestaurantId;
    }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }

    public void setChosenRestaurantName(String chosenRestaurantName) {
        this.chosenRestaurantName = chosenRestaurantName;
    }

    @Nullable
    public String getChosenRestaurantAdress() {
        return chosenRestaurantAdress;
    }

    public void setChosenRestaurantAdress(@Nullable String chosenRestaurantAdress) {
        this.chosenRestaurantAdress = chosenRestaurantAdress;
    }
    @Nullable
    public String getChosenRestaurantDate() {
        return chosenRestaurantDate;
    }

    public void setChosenRestaurantDate(@Nullable String chosenRestaurantDate) {
        this.chosenRestaurantDate = chosenRestaurantDate;
    }

}
