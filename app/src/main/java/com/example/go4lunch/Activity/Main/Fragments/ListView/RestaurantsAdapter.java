package com.example.go4lunch.Activity.Main.Fragments.ListView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.Util.DetailsUtil;
import com.example.go4lunch.ViewModel.RestaurantStateItem;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private final List<RestaurantStateItem> results;
    private final FragmentActivity activity;

    public RestaurantsAdapter(List<RestaurantStateItem> results, FragmentActivity activity) {
        this.results = results;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_restaurant;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        view.getContext();
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v ->
                DetailsUtil.openDetailsFragment(
                        activity,
                        results.get(position).getPlaceId()));
        holder.createViewWithRestaurants(results.get(position));
    }


    @Override
    public int getItemCount() {
        return results.size();
    }
}
