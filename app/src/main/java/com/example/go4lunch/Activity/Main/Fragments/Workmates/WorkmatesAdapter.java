package com.example.go4lunch.Activity.Main.Fragments.Workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.Util.DetailsUtil;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {
    private final List<User> listWorkmates;
    private final FragmentActivity activity;
    private final String TAG;


    public WorkmatesAdapter(List<User> listWorkmates,FragmentActivity activity, String TAG) {
        this.listWorkmates = listWorkmates;
        this.activity = activity;
        this.TAG = TAG;
    }


    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_workmate;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        User workmate;
 //      if (TAG == null) {
            workmate = listWorkmates.get(position);
            if (workmate != null) {
                holder.updateViewWithWorkmates(workmate, TAG);
                if (!(workmate.getChosenRestaurantId() == null) && !(workmate.getChosenRestaurantId().equals(""))) {
                holder.itemView.setOnClickListener(v ->
                        DetailsUtil.openDetailsFragment(activity,
                                listWorkmates.get(position).getChosenRestaurantId()));}
            }
 //       }
    }


    @Override
    public int getItemCount() {
        return listWorkmates.size();
    }


}
