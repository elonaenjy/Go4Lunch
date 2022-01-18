package com.example.go4lunch.Repository;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.go4lunch.Model.RestaurantChoice;


public class SharedPreferencesRepository {
    public final static String PREFS_NAME = "PREFS";
    static SharedPreferences settings;
    String userId;
    String choosenRestaurantId;
    String choosenRestaurantName;
    String choosenRestaurantAdress;
    String choosenRestaurantDate;
    // ------- SAVE -------- //

    public static void saveNotifications(Context context, Boolean notifications){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("NOTIFICATIONS", notifications);
        editor.apply();
    }

    // ------ GET ----- //
    public Boolean getNotifications(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getBoolean("NOTIFICATIONS", true);
    }

    public static Float getRadius(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getFloat("RADIUS", 1000);
    }

    public void saveRadius(Context context, Float radius) {
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("RADIUS", radius);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveRestaurantChoice (Context context, RestaurantChoice restaurantChoice) {
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("USERID", restaurantChoice.getUserId());
        editor.putString("CHOOSENRESTAURANTID", restaurantChoice.getChoosenRestaurantId());
        editor.putString("CHOOSENRESTAURANTNAME", restaurantChoice.getChoosenRestaurantName());
        editor.putString("CHOOSENRESTAURANTADRESS", restaurantChoice.getChoosenRestaurantAdress());
        editor.putString("CHOOSENRESTAURANTDATE", restaurantChoice.getChoosenDate());
        editor.apply();
    }



    public RestaurantChoice getRestaurantChoice(Context appContext) {
        settings = appContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        RestaurantChoice restaurantchoice = new RestaurantChoice();
        restaurantchoice.setUserId((settings.getString("USERID", userId)));
        restaurantchoice.setChoosenRestaurantId(settings.getString("CHOOSENRESTAURANTID", choosenRestaurantId));
        restaurantchoice.setChoosenRestaurantId(settings.getString("CHOOSENRESTAURANTID", choosenRestaurantId));
        restaurantchoice.setChoosenRestaurantName(settings.getString("CHOOSENRESTAURANTNAME", choosenRestaurantName));
        restaurantchoice.setChoosenRestaurantAdress(settings.getString("CHOOSENRESTAURANTADRESS", choosenRestaurantAdress));
        restaurantchoice.setChoosenDate(settings.getString("CHOOSENRESTAURANTDATE", choosenRestaurantDate));
        return restaurantchoice;
    }

    }

