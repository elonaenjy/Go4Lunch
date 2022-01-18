package com.example.go4lunch.Service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public final class HelperFirestoreUser implements OnSuccessListener<DocumentSnapshot> {

    private static final String COLLECTION_NAME = "users";
    private static final String USER_NAME_FIELD = "userName";
    private static final String USER_CHOOSEN_RESTAURANT_ID_FIELD = "chosenRestaurantId";
    private static final String USER_CHOOSEN_RESTAURANT_NAME_FIELD = "chosenRestaurantName";
    private static final String USER_CHOOSEN_RESTAURANT_ADRESS_FIELD = "chosenRestaurantAdress";
    private static final String USER_CHOOSEN_RESTAURANT_DATE_FIELD = "chosenRestaurantDate";

            // Get the Collection Reference
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }


    public Task<QuerySnapshot> getAllUsers() {
        return getUsersCollection().get();
    }

    // Create User in Firestore
    public User createUserInFirestore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            if (urlPicture == null) {
                urlPicture = "https://i.pravatar.cc/150?u=a042581f4e29026704d";
            }
            String username = user.getDisplayName();
            String uid = user.getUid();
            String userMail = user.getEmail();

            User userToCreate = new User(uid, username, userMail, urlPicture,
                    null, null,null,null);

            Task<DocumentSnapshot> userData = getUserFirestore(uid);
            // If the user already exist in Firestore, we get his data (choosen Restaurant and choosen name)
            userData.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                              @Override
                                              public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                  User userFirestore = documentSnapshot.toObject(User.class);
                                                  if (userFirestore == null) {
                                                      getUsersCollection().document(uid).set(userToCreate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void unused) {
                                                              Log.e("TAG : Success ", "Success");
                                                          }
                                                      }).addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              Log.e("TAG : error ", "error");
                                                          }
                                                      });

                                                  }
                                              }
                                          }
            );
            return userToCreate;
        }
        return null;
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserFirestore(String uid) {
                return this.getUsersCollection().document(uid).get();
        }

    // --- UPDATE ---
    public void updateRestaurantChoice(User mUser) {
        this.getUsersCollection().document(mUser.getUid()).update(USER_CHOOSEN_RESTAURANT_ID_FIELD, mUser.getChosenRestaurantId());
        this.getUsersCollection().document(mUser.getUid()).update(USER_CHOOSEN_RESTAURANT_NAME_FIELD, mUser.getChosenRestaurantName());
        this.getUsersCollection().document(mUser.getUid()).update(USER_CHOOSEN_RESTAURANT_ADRESS_FIELD, mUser.getChosenRestaurantAdress());
        this.getUsersCollection().document(mUser.getUid()).update(USER_CHOOSEN_RESTAURANT_DATE_FIELD, mUser.getChosenRestaurantDate());

            }


    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
    }
}