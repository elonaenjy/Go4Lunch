package com.example.go4lunch.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.go4lunch.Model.RestaurantChoice;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Repository.SharedPreferencesRepository;
import com.example.go4lunch.Service.HelperFirestoreUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NotificationWorker extends Worker {

    String message = "";

    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Do the work : here send a notification when notification choice is "ON" and time is 12:00
        Context applicationContext = getApplicationContext();
        CompleteMessageNotification(applicationContext);
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    private Void CompleteMessageNotification(Context applicationContext) {
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository();
        RestaurantChoice restaurantChoice = sharedPreferencesRepository.getRestaurantChoice(applicationContext);
        if (restaurantChoice.getChoosenRestaurantId() == null) {
            message = applicationContext.getString(R.string.notification_no_choice);
            SendNotification(message, applicationContext);
        } else if (CheckConnectivity.isConnected(applicationContext)) {
            HelperFirestoreUser helperFirestoreUser = new HelperFirestoreUser();
            final Task<QuerySnapshot> querySnapshotTask = helperFirestoreUser.getAllUsers()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ArrayList<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                for (DocumentSnapshot workmate : task.getResult()) {
                                    users.add(workmate.toObject(User.class));
                                }
                            }
                            message = setUpMessageWithUsersData(users, restaurantChoice, applicationContext);
                            SendNotification(message, applicationContext);
                        } else {
                            System.out.println("Error getting documents: " + task.getException());
                        }
                    })
                    .addOnFailureListener(e ->
                    {
                        message = applicationContext.getString(R.string.notifications_firebase_failure);
                        SendNotification(message, applicationContext);
                    });
        } else {
            // INTERNET DISABLED
            message = applicationContext.getString(R.string.notifications_no_internet_connection);
            SendNotification(message, applicationContext);
        }
        return null;
    }

    private void SendNotification(String message, Context applicationContext) {
        String title = "Go4Lunch";
        NotificationManager mNotificationManager =
                (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "android",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("WorkManger");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationContext, "1")
                .setSmallIcon(R.mipmap.ic_launcher_g4l) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true) // clear notification after click
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);

        // Show the notification
        NotificationManagerCompat.from(applicationContext).notify(1, mBuilder.build());
    }

    public static String setUpMessageWithUsersData(ArrayList<User> users, RestaurantChoice restaurantChoice, Context appContext) {
        ArrayList<User> usersWithoutCurrentUser = new ArrayList<>();
        String chosenRestaurantName = restaurantChoice.getChoosenRestaurantName();
        String userId = restaurantChoice.getUserId();
        String chosenRestaurantAddress = restaurantChoice.getChoosenRestaurantAdress();
        String chosenRestaurantId = restaurantChoice.getChoosenRestaurantId();
        String wmessage;
        for (User user : users) {
            if (!user.getUid().equals(userId)) {
                usersWithoutCurrentUser.add(user);
            }
        }
        ArrayList<String> lunchWorkmates = getWorkmates(usersWithoutCurrentUser, chosenRestaurantId);
        if (lunchWorkmates.size() > 0) {
            // At least one another workmate lunching with currentUser
            if (lunchWorkmates.size() == 1) {
                // if 1 workmate joining
                wmessage =
                        appContext.getString(
                                R.string.notification_lunch_with_one_workmate,
                                chosenRestaurantName,
                                chosenRestaurantAddress,
                                lunchWorkmates.get(0));
                return wmessage;

            } else {
                // if 1+ workmates joining
                StringBuilder workmatesString = new StringBuilder();
                for (String workmate : lunchWorkmates) {
                    if (workmate.equals(lunchWorkmates.get(lunchWorkmates.size() - 1))) {
                        workmatesString.append(workmate);
                    } else if (workmate.equals(lunchWorkmates.get(lunchWorkmates.size() - 2))) {
                        workmatesString.append(workmate);
                        workmatesString.append(appContext.getString(R.string.notification_workmatesstring_builder_and));
                    } else {
                        workmatesString.append(workmate);
                        workmatesString.append(", ");
                    }
                }
                return appContext.getString(
                        R.string.notification_lunch_with_few_workmate,
                        chosenRestaurantName,
                        chosenRestaurantAddress,
                        lunchWorkmates.size(),
                        workmatesString.toString());
            }
        } else {
            // No workmate lunching with currentUser
            return appContext.getString(R.string.notification_lunch_alone, chosenRestaurantName, chosenRestaurantAddress);
        }
    }

    // provides workmates who are joining the same restaurant
    private static ArrayList<String> getWorkmates(ArrayList<User> users, String restaurantId) {
        ArrayList<String> lunchWorkmates = new ArrayList<>();
        for (User user : users) {
            if ((user.getChosenRestaurantId() != null) && (user.getChosenRestaurantId().equals(restaurantId))) {
                lunchWorkmates.add(user.getUserName());
            }
        }
        return lunchWorkmates;
    }
}