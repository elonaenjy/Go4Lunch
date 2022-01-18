package com.example.go4lunch.Activity.Main.Fragments.Workmates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.R;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    private final TextView userName = itemView.findViewById(R.id.item_workmates_name);
    private final ImageView userPicture = itemView.findViewById(R.id.item_workmates_picture);
    private final TextView userRestaurantChoice = itemView.findViewById(R.id.item_workmates_restaurant);


    public WorkmatesViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    // ------------ RecyclerView for WorkmatesFragment ---------- //

    @SuppressLint("StringFormatInvalid")
    public void updateViewWithWorkmates(User user, String TAG) {
        userName.setText(user.getUserName());

        Glide.with(context)
                .load(user.getUrlPicture())
                .apply(new RequestOptions()
                        .circleCrop())
                .into(userPicture);
        if (!TAG.equals("DetailsFragment")) {
            if (user.getChosenRestaurantId() == null || user.getChosenRestaurantId().equals("")) {
                userRestaurantChoice.setText(R.string.item_workmates_restaurant_text_null);
                userRestaurantChoice.setTextColor(context.getResources().getColor(R.color.quantum_grey));
            } else {
                String restaurantName = user.getChosenRestaurantName();
                userRestaurantChoice.setText(context.getString(R.string.item_workmates_restaurant_text, restaurantName));
            }
        } else {
            userRestaurantChoice.setText(context.getString((R.string.item_workmates_joigning_text)));
        }
    }
}
