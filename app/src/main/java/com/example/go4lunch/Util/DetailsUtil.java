package com.example.go4lunch.Util;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.go4lunch.Activity.Main.Fragments.DetailRestaurant.DetailsFragment;
import com.example.go4lunch.R;

public class DetailsUtil {
    public static DetailsFragment newInstance(String placeId) {
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        DetailsFragment f = new DetailsFragment();
        f.setArguments(args);
        return f;
    }

    public static void openDetailsFragment(FragmentActivity activity, String placeId) {
        Fragment fragment = newInstance(placeId);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

