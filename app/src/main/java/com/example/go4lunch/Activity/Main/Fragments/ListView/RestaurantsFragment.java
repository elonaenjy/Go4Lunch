package com.example.go4lunch.Activity.Main.Fragments.ListView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.RestaurantStateItem;
import com.example.go4lunch.ViewModel.ListRestaurantViewModel;
import com.example.go4lunch.ViewModelFactory;

import java.util.List;

public class RestaurantsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListRestaurantViewModel listRestaurantViewModel;
    public static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;

    // UI parameters
    private ProgressBar progressBar;
    private TextView noRestaurants;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        listRestaurantViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance()).get(ListRestaurantViewModel.class);

        noRestaurants = view.findViewById(R.id.restaurant_List_no_restaurants_to_show);
        progressBar = view.findViewById(R.id.restaurants_progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.restaurants_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Show the progressBar
        progressBar.setVisibility(View.VISIBLE);

        getRestaurants();

        return view;
    }

    private void getRestaurants() {
        listRestaurantViewModel.getListRestaurants().observe(
                requireActivity(),
                changedListRestaurantsStateItem -> {
                    //Do something with the changed value
                    if (changedListRestaurantsStateItem != null) {
                        sendResultsToAdapter(changedListRestaurantsStateItem);
                    } else {
                        noRestaurantsToShow();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.setGroupVisible(R.id.main_activity_restaurants_group_sort, true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // SORT restaurants
        int id = item.getItemId();
        final int main_activity_restaurants_sort_name_asc = R.id.main_activity_restaurants_sort_name_asc;
        final int main_activity_restaurants_sort_name_desc = R.id.main_activity_restaurants_sort_name_desc;
        final int main_activity_restaurants_sort_rating_desc = R.id.main_activity_restaurants_sort_rating_desc;
        final int main_activity_restaurants_sort_workmates_desc = R.id.main_activity_restaurants_sort_workmates_desc;
        final int main_activity_restaurants_sort_distance_asc = R.id.main_activity_restaurants_sort_distance_asc;

        switch (id) {
            // Sort AZ
            case main_activity_restaurants_sort_name_asc:
                listRestaurantViewModel.sortAZ().observe(
                        requireActivity(),
                        changedListRestaurantsStateItem -> {
                            //Do something with the changed value
                            if (changedListRestaurantsStateItem != null) {
                                sendResultsToAdapter(changedListRestaurantsStateItem);
                            } else {
                                noRestaurantsToShow();
                            }
                        });
                return true;
                // Sort ZA
            case main_activity_restaurants_sort_name_desc:
                listRestaurantViewModel.sortZA().observe(
                        requireActivity(),
                        changedListRestaurantsStateItem -> {
                            //Do something with the changed value
                            if (changedListRestaurantsStateItem != null) {
                                sendResultsToAdapter(changedListRestaurantsStateItem);
                            } else {
                                noRestaurantsToShow();
                            }
                        });
                return true;

            // Sort Rating
            case main_activity_restaurants_sort_rating_desc:
                listRestaurantViewModel.sortRatingDesc().observe(
                        requireActivity(),
                        changedListRestaurantsStateItem -> {
                            //Do something with the changed value
                            if (changedListRestaurantsStateItem != null) {
                                sendResultsToAdapter(changedListRestaurantsStateItem);
                            } else {
                                noRestaurantsToShow();
                            }
                        });
                return true;

            // Sort Workmates
            case main_activity_restaurants_sort_workmates_desc:
                listRestaurantViewModel.sortNbWorkmatesDesc().observe(
                        requireActivity(),
                        changedListRestaurantsStateItem -> {
                            //Do something with the changed value
                            if (changedListRestaurantsStateItem != null) {
                                sendResultsToAdapter(changedListRestaurantsStateItem);
                            } else {
                                noRestaurantsToShow();
                            }
                        });
                return true;

            // Sort Distance
            case main_activity_restaurants_sort_distance_asc:
                listRestaurantViewModel.getListRestaurants().observe(
                        requireActivity(),
                        changedListRestaurantsStateItem -> {
                            //Do something with the changed value
                            if (changedListRestaurantsStateItem != null) {
                                sendResultsToAdapter(changedListRestaurantsStateItem);
                            } else {
                                noRestaurantsToShow();
                            }
                        });
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void noRestaurantsToShow() {
        progressBar.setVisibility(View.GONE);
        noRestaurants.setText(R.string.restaurant_list_no_restaurants_to_show);
        noRestaurants.setVisibility(View.VISIBLE);
    }

    private void sendResultsToAdapter(List<RestaurantStateItem> results) {
        if (results == null || results.isEmpty()) {
            noRestaurantsToShow();
        } else {
  //          Location userLocation = listRestaurantViewModel.getGpsMessageLiveData().getValue();
            recyclerView.setAdapter(new RestaurantsAdapter(results, this.getActivity()));
        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}