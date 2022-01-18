package com.example.go4lunch.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.Model.RestaurantLiked;
import com.example.go4lunch.Service.HelperFirestoreLikedRestaurant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserLikingRestaurantRepository {
    private MutableLiveData<List<RestaurantLiked>> listOfUsersLikingARestaurant = new MutableLiveData<>();

    HelperFirestoreLikedRestaurant helperFirestoreLikedRestaurant = new HelperFirestoreLikedRestaurant();
    Boolean likedRestaurant;
    private static final ArrayList<RestaurantLiked> listUsersLikingRestaurant = new ArrayList<>();

    private static final String COLLECTION_NAME = "likedRestaurant";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference mLikedRestaurantRef = db.collection(COLLECTION_NAME);

    //
    public MutableLiveData<List<RestaurantLiked>> getUsersLikingRestaurant() {
        // Get all the user from Firestore and cast it in ArrayList format
        listOfUsersLikingARestaurant = new MutableLiveData<>();

        mLikedRestaurantRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (!(queryDocumentSnapshots == null)) {
                listUsersLikingRestaurant.clear();
                for (DocumentSnapshot user : queryDocumentSnapshots.getDocuments()) {
                    listUsersLikingRestaurant.add(user.toObject(RestaurantLiked.class));
                }
                listOfUsersLikingARestaurant.setValue(listUsersLikingRestaurant);
            }
        });
        return listOfUsersLikingARestaurant;
    }

    public String getUserSingletonId() {
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        String uId = userSingletonRepository.getUser().getUid();
        return uId;
    }

    public Boolean getUserLike(String placeId) {
        String uId = getUserSingletonId();
        return findIfLike(placeId, uId, listUsersLikingRestaurant);
    }

    private Boolean findIfLike(String placeId, String uId, ArrayList<RestaurantLiked> listUsersLikingRestaurant) {
        likedRestaurant = false;
        if (listUsersLikingRestaurant != null && listUsersLikingRestaurant.size() != 0) {
            for (RestaurantLiked restaurantLiked : listUsersLikingRestaurant) {
                if (restaurantLiked.getUid() != null
                        && restaurantLiked.getUid().equals(uId)
                        && restaurantLiked.getPlaceId().equals(placeId)) {
                    likedRestaurant = true;
                    break;
                }
            }
        }
        return likedRestaurant;
    }

    public RestaurantLiked addRestaurantLiked(String placeId) {
        return helperFirestoreLikedRestaurant.createLikedRestaurant(placeId, getUserSingletonId());
    }

    public void deleteRestaurantLiked(String placeId) {
        // If the user already exist in Firestore, we get his data (choosen Restaurant and choosen name)
        helperFirestoreLikedRestaurant.queryGetDocumentId(placeId, getUserSingletonId()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    helperFirestoreLikedRestaurant.deleteLikedRestaurant(document.getId());
                }
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(e -> {
            //handle error
            Log.d("Error", "Error getting id documents: ", e);
        });
    }
}
