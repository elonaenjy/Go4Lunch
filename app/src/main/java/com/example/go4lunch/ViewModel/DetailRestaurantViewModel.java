package com.example.go4lunch.ViewModel;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Model.RestaurantChoice;
import com.example.go4lunch.Model.RestaurantLiked;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.Model.details.ResultAPIDetails;
import com.example.go4lunch.Repository.DetailRestaurantRepository;
import com.example.go4lunch.Repository.SharedPreferencesRepository;
import com.example.go4lunch.Repository.UserLikingRestaurantRepository;
import com.example.go4lunch.Repository.UserRepository;
import com.example.go4lunch.Repository.UserSingletonRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailRestaurantViewModel extends ViewModel {

    private DetailRestaurantRepository detailRestaurantRepository;
    private UserLikingRestaurantRepository userLikingRestaurantRepository;
    private UserRepository userRepository;
    private final MediatorLiveData<DetailRestaurantStateItem> mMediatorLiveData = new MediatorLiveData<>();

    public DetailRestaurantViewModel(
            DetailRestaurantRepository detailRestaurantRepository,
            UserRepository userRepository,
            UserLikingRestaurantRepository userLikingRestaurantRepository) {
        this.detailRestaurantRepository = detailRestaurantRepository;
        this.userRepository = userRepository;
        this.userLikingRestaurantRepository = userLikingRestaurantRepository;

        LiveData<ResultAPIDetails> restaurantDetails = detailRestaurantRepository.getRestaurantDetails();
        LiveData<List<RestaurantLiked>> usersLikingRestaurant = userLikingRestaurantRepository.getUsersLikingRestaurant();
        MutableLiveData<ArrayList<User>> currentUserLiveData = userRepository.getFirestoreUser();

        mMediatorLiveData.addSource(restaurantDetails, restaurantDetails1 -> combine(restaurantDetails1,
                usersLikingRestaurant.getValue(),
                currentUserLiveData.getValue()));

        mMediatorLiveData.addSource(currentUserLiveData, currentUserLiveData1 -> combine(restaurantDetails.getValue(),
                usersLikingRestaurant.getValue(),
                currentUserLiveData1));

        mMediatorLiveData.addSource(usersLikingRestaurant, restaurantLiked -> combine(restaurantDetails.getValue(),
                restaurantLiked,
                currentUserLiveData.getValue()));
    }

    private void combine(@NonNull ResultAPIDetails restaurantDetail,
                         @Nullable List<RestaurantLiked> likingRestaurant,
                         @NonNull ArrayList<User> currentUserLiveData) {
        /**
         * Main method. all the parameters allow to create a model which will dispense the views
         * At the end a view state is created with basic fields for display correct data
         * @param restaurantDetail get the information from Places API
         * @param User liking a restaurant
         */
        if (restaurantDetail != null) {
            DetailRestaurantStateItem restaurant = new DetailRestaurantStateItem(
                    restaurantDetail.getPlaceId(),
                    restaurantDetail.getName(),
                    restaurantDetail.getOpening_hours(),
                    restaurantDetail.getRating(),
                    restaurantDetail.getWebsite(),
                    restaurantDetail.getInternational_phone_number(),
                    restaurantDetail.getFormatted_address(),
                    restaurantDetail.getPhotos(),
                    false,
                    null,
                    null,
                    null,
                    null);

            if (currentUserLiveData != null) {
                restaurant.setChoosenRestaurantId(currentUserLiveData.get(0).getChosenRestaurantId());
                restaurant.setChoosenRestaurantName(currentUserLiveData.get(0).getChosenRestaurantName());
                restaurant.setChoosenRestaurantAdress(currentUserLiveData.get(0).getChosenRestaurantAdress());
            }
            if (likingRestaurant != null) {
                restaurant.setUserLike(userLikingRestaurantRepository.getUserLike(restaurant.getPlaceId()));
            }

            mMediatorLiveData.setValue(restaurant);
        }
    }

    //LiveData observed by the View
    public LiveData<DetailRestaurantStateItem> getMediatorLiveData() {
        return mMediatorLiveData;
    }

    // fin mise en place Mediator LiveData
    public MutableLiveData<List<User>> getWorkmatesExceptCurrentUserForARestaurant(String placeId) {
        return userRepository.getAllUsersExceptCurrentUserForARestaurant(placeId);
    }

    public void initDetailViewMutableLiveData(String placeId) {
        detailRestaurantRepository.getPlaceDetails(placeId);
    }

    public void addRestaurantLiked(String placeId) {
        userLikingRestaurantRepository.addRestaurantLiked(placeId);
    }

    public void deleteRestaurantLiked(String placeId) {
        userLikingRestaurantRepository.deleteRestaurantLiked(placeId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateRestaurantChoice(Context context, String chosenRestaurantId,
                                       String chosenRestaurantName, String chosenRestaurantAdress) {
        // first update User Firestore
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        User mUser = userSingletonRepository.getUser();
        mUser.setChosenRestaurantId(chosenRestaurantId);
        mUser.setChosenRestaurantName(chosenRestaurantName);
        mUser.setChosenRestaurantAdress(chosenRestaurantAdress);

        Date now = new Date();
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
        String formattedDate = dateFormatter.format(now);
        if (chosenRestaurantId != null) {
            mUser.setChosenRestaurantDate(formattedDate);
        } else {
            mUser.setChosenRestaurantDate(null);
        }
        userRepository.updateUserFirestore(mUser);

        // Second update Singleton
        userSingletonRepository.updateRestaurantChoice(chosenRestaurantId, chosenRestaurantName,
                chosenRestaurantAdress, formattedDate);

        // Third update SharedPreferences
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository();
        RestaurantChoice restaurantChoice = new RestaurantChoice();
        restaurantChoice.setUserId(mUser.getUid());
        restaurantChoice.setChoosenRestaurantAdress(mUser.getChosenRestaurantAdress());
        restaurantChoice.setChoosenRestaurantName(mUser.getChosenRestaurantName());
        restaurantChoice.setChoosenDate(mUser.getChosenRestaurantDate());
        sharedPreferencesRepository.saveRestaurantChoice(context, restaurantChoice);
    }
}