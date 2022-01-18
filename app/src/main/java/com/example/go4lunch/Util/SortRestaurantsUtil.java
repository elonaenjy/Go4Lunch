package com.example.go4lunch.Util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.ViewModel.RestaurantStateItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SortRestaurantsUtil {
    // Data
    public static final HashMap<String, Integer> workmatesCountHashMap = new HashMap<>();
    public static final HashMap<String, Integer> distanceHashMap = new HashMap<>();


    public static ArrayList<RestaurantStateItem> sortAZ(ArrayList<RestaurantStateItem> listRestaurants) {
        ArrayList<RestaurantStateItem> sortedList = new ArrayList<>(listRestaurants);
        Collections.sort(sortedList, (obj1, obj2) -> obj1.getName().compareToIgnoreCase(obj2.getName()));
        return sortedList;
    }

    public static ArrayList<RestaurantStateItem> sortZA(List<RestaurantStateItem> listRestaurants) {
        ArrayList<RestaurantStateItem> sortedList = new ArrayList<>(listRestaurants);
        Collections.sort(sortedList, (obj1, obj2) -> obj2.getName().compareToIgnoreCase(obj1.getName()));
        return sortedList;
    }

    public static ArrayList<RestaurantStateItem> sortRatingDesc(List<RestaurantStateItem> listRestaurants) {
        ArrayList<RestaurantStateItem> sortedList = new ArrayList<>(listRestaurants);
        Collections.sort(sortedList, (obj1, obj2) ->
                obj2.getRating().compareTo(obj1.getRating()));
        return sortedList;
    }

    public static ArrayList<RestaurantStateItem> sortWorkmatesDesc(List<RestaurantStateItem> listRestaurants) {
        ArrayList<RestaurantStateItem> sortedList = new ArrayList<>(listRestaurants);
        Collections.sort(sortedList, (obj1, obj2) ->
                obj2.getNbWorkmates().compareTo(obj1.getNbWorkmates()));
        return sortedList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<RestaurantStateItem> sortDistanceAsc(List<RestaurantStateItem> listRestaurants) {
        List<RestaurantStateItem> sortedList = new ArrayList<>(listRestaurants);
        Collections.sort(sortedList, Comparator.comparing(RestaurantStateItem::getRestaurantDistance));
        return (ArrayList<RestaurantStateItem>) sortedList;
    }


}