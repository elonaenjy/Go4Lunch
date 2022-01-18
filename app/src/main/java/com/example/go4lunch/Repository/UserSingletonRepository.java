package com.example.go4lunch.Repository;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.Model.User;

import java.text.DateFormat;

public class UserSingletonRepository {
    User mUser;

    private static final UserSingletonRepository userInstance = new UserSingletonRepository();
    public static UserSingletonRepository getInstance() {
        return userInstance;
    }

    public UserSingletonRepository() { }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public User getUser() {
        return mUser;
    }

    public void updateRestaurantChoice(String chosenRestaurantId, String chosenRestaurantName,
                                       String chosenRestaurantAdress, String chosenRestaurantDate) {
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        User userSingleton = getUser();

        userSingleton.setChosenRestaurantId(chosenRestaurantId);
        userSingleton.setChosenRestaurantName(chosenRestaurantName);
        userSingleton.setChosenRestaurantAdress(chosenRestaurantAdress);
        userSingleton.setChosenRestaurantDate(chosenRestaurantDate);
        setUser(userSingleton);




            }


}
