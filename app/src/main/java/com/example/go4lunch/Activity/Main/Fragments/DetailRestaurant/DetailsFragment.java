package com.example.go4lunch.Activity.Main.Fragments.DetailRestaurant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.BuildConfig;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Activity.Main.Fragments.Workmates.WorkmatesAdapter;
import com.example.go4lunch.Repository.UserSingletonRepository;
import com.example.go4lunch.Util.RatingUtil;
import com.example.go4lunch.ViewModel.DetailRestaurantStateItem;
import com.example.go4lunch.ViewModel.DetailRestaurantViewModel;
import com.example.go4lunch.ViewModelFactory;

import java.util.List;
import java.util.Objects;

public class DetailsFragment extends Fragment {
    public static final String TAG = "DetailsFragment";

    public String placeId;
    private DetailRestaurantViewModel detailRestaurantViewModel;

    private TextView restaurantName;
    private ImageView restaurantPicture;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageButton buttonBack;
    private ImageButton buttonRestaurantChoice;
    private Button buttonLike;

    private Button buttonPhone;
    private Button buttonWebsite;
    private RecyclerView recyclerView;

    private String chosenRestaurantId;
    String chosenRestaurantName;
    String chosenRestaurantAdress;

    public DetailsFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        placeId = requireArguments().getString("placeId");
        detailRestaurantViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.initDetailViewMutableLiveData(placeId);

        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);

        getDetailRestaurantStateItem(view);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDetailRestaurantStateItem(View view) {
        detailRestaurantViewModel.getMediatorLiveData().observe(
                getViewLifecycleOwner(),
                detailRestaurantStateItem -> {
                    hideActivityViews();
                    configureView(view, detailRestaurantStateItem);
                    setPicture(view, detailRestaurantStateItem);
                    restaurantChoiceLayout(detailRestaurantStateItem);
                    restaurantLikeLayout(detailRestaurantStateItem);
                    populateWorkmates(detailRestaurantStateItem.getPlaceId());
                    RatingUtil.showRating(detailRestaurantStateItem.getRating(), star1, star2, star3);
                    setClickableFunctionality(detailRestaurantStateItem);
                }
        );
    }

    // -------------------- CONFIG User Interface LAYOUT ------------------ //
    @SuppressLint("SetTextI18n")
    private void configureView(View view, DetailRestaurantStateItem detailRestaurantStateItem) {
        // View
        restaurantName = view.findViewById(R.id.restaurant_details_name);
        TextView restaurantAdress = view.findViewById(R.id.restaurant_details_address);
        restaurantPicture = view.findViewById(R.id.restaurant_details_picture);
        buttonPhone = view.findViewById(R.id.restaurant_details_phone_call);
        buttonLike = view.findViewById(R.id.restaurant_details_like);
        buttonWebsite = view.findViewById(R.id.restaurant_details_website);
        buttonBack = view.findViewById(R.id.fragment_restaurant_details_button_backpress);
        buttonRestaurantChoice = view.findViewById(R.id.fragment_restaurant_details_button_restaurant_choice);

        buttonPhone.setText(R.string.restaurant_details_phone_call);
        buttonLike.setText("LIKE");
        buttonWebsite.setText(R.string.restaurant_details_website);

        restaurantName.setText(detailRestaurantStateItem.getName());
        restaurantAdress.setText(detailRestaurantStateItem.getFormatted_address());

        star1 = view.findViewById(R.id.restaurant_details_star1);
        star2 = view.findViewById(R.id.restaurant_details_star2);
        star3 = view.findViewById(R.id.restaurant_details_star3);
        recyclerView = view.findViewById(R.id.restaurant_details_workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setPicture(View view, DetailRestaurantStateItem detailRestaurantStateItem) {
        if (detailRestaurantStateItem.getPhotos() == null) {
            restaurantPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            Glide.with(view)
                    .load(detailRestaurantStateItem.getPhotos().get(0).getPhoto_URL() + BuildConfig.GOOGLE_MAPS_KEY)
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(restaurantPicture);
            restaurantPicture.setVisibility(View.VISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText("");
            noPhoto.setVisibility(View.GONE);
        }
    }

    // Handles DetailsFragment custom user choices layout
    private void restaurantChoiceLayout(DetailRestaurantStateItem detailRestaurantStateItem) {
        chosenRestaurantId = detailRestaurantStateItem.getChoosenRestaurantId();
        chosenRestaurantName = detailRestaurantStateItem.getChoosenRestaurantName();
        chosenRestaurantAdress = detailRestaurantStateItem.getChoosenRestaurantAdress();
        if (chosenRestaurantId != null && chosenRestaurantId.equals(placeId)) {
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
        } else {
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_add_30);
        }
    }


    // Handles DetailsFragment custom user choices layout
    private void restaurantLikeLayout(DetailRestaurantStateItem detailRestaurantStateItem ) {
        if (detailRestaurantStateItem.getUserLike() != null && detailRestaurantStateItem.getUserLike()) {
            buttonLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24, 0, 0);
        } else {
            buttonLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_border_24, 0, 0);
        }
    }

    // Populates Workmates who choose this restaurant
    private void populateWorkmates(String placeId) {
        detailRestaurantViewModel.getWorkmatesExceptCurrentUserForARestaurant(placeId).observe(
                requireActivity(),
                changedListWorkmates -> {
                    //Do something with the changed value
                    if (changedListWorkmates != null) {
                        sendResultsToAdapter(changedListWorkmates);
                    }
                });
    }

    private void sendResultsToAdapter(List<User> lWorkmatesExceptCurrentUser) {
        recyclerView.setAdapter(new WorkmatesAdapter(lWorkmatesExceptCurrentUser, this.getActivity(), TAG));
        recyclerView.setVisibility(View.VISIBLE);
    }

    // -------------- Handles USER INTERACTIONS -------------- //
    // CONFIG CLICK LISTENERS
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setClickableFunctionality(DetailRestaurantStateItem detailRestaurantStateItem) {
        buttonPhone.setOnClickListener(v -> {
            if (detailRestaurantStateItem.getInternationalPhoneNumber() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_phone_number, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + detailRestaurantStateItem.getInternationalPhoneNumber().trim()));
                startActivity(intent);
            }
        });

        buttonWebsite.setOnClickListener(v -> {
            if (detailRestaurantStateItem.getWebsite() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_website, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(detailRestaurantStateItem.getWebsite()));
                startActivity(openURL);
            }
        });

        buttonLike.setOnClickListener(v -> buttonLikeResponse(detailRestaurantStateItem));

        buttonBack.setOnClickListener(v -> requireActivity().onBackPressed());

        buttonRestaurantChoice.setOnClickListener(v -> buttonRestaurantChoiceResponse(detailRestaurantStateItem));
    }

    // RESTAURANT LIKE BUTTON
    private void buttonLikeResponse(DetailRestaurantStateItem detailRestaurantStateItem) {
        if (detailRestaurantStateItem.getUserLike()) {
            // if restaurant is liked
            detailRestaurantViewModel.deleteRestaurantLiked(detailRestaurantStateItem.getPlaceId());
         } else {
            detailRestaurantViewModel.addRestaurantLiked(detailRestaurantStateItem.getPlaceId());
        }
    }

    // RESTAURANT CHOICE BUTTON
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buttonRestaurantChoiceResponse(DetailRestaurantStateItem detailRestaurantStateItem) {
        // chosenRestaurantId and chosenRestaurantName is initialized with the userSingleton value and
        // contains the choice already made by the user
        User mUser;
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        mUser = userSingletonRepository.getUser();
        if (mUser.getChosenRestaurantId() == null || mUser.getChosenRestaurantId().equals("")) {
                // if no choice has already been made
                saveRestaurantChoice(detailRestaurantStateItem.getPlaceId(), detailRestaurantStateItem.getName(),
                        detailRestaurantStateItem.getFormatted_address());
                buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
                Toast.makeText(getContext(), getString(R.string.restaurant_Chosen, restaurantName.getText()), Toast.LENGTH_SHORT).show();
            } else
            // if the user want to cancel the chosenRestaurant
            if (chosenRestaurantId.equals(detailRestaurantStateItem.getPlaceId())) {
                alertRestaurantCancel();
            } else
                // if the user want to change the chosenRestaurant
                if (!chosenRestaurantId.equals(detailRestaurantStateItem.getPlaceId())) {
                    alertRestaurantChange(detailRestaurantStateItem);
            }
        }


    // Action on buttonRestaurantChoice click
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void alertRestaurantCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_message)
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_title);
        AlertDialog dialogRestaurantChosen = builder.create();
        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
                    saveRestaurantChoice(null,
                    null, null);
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_add_30);

        });

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();
    }

    // Action on buttonRestaurantChoice click -- if restaurant chosen != restaurant details
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("StringFormatInvalid")
    private void alertRestaurantChange(DetailRestaurantStateItem detailRestaurantStateItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(requireContext().getString(R.string.details_fragment_alertdialog_restaurant_choice_change_message, detailRestaurantStateItem.getName()))
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_change_title);

        AlertDialog dialogRestaurantChosen = builder.create();

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES),
                (dialog1, which) -> {
                    saveRestaurantChoice(detailRestaurantStateItem.getPlaceId(), detailRestaurantStateItem.getName(),
                            detailRestaurantStateItem.getFormatted_address());
                    buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
                });
        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveRestaurantChoice(String chosenRestaurantId, String chosenRestaurantName, String chosenRestaurantAdress) {
        detailRestaurantViewModel.updateRestaurantChoice(requireContext(), chosenRestaurantId,
                chosenRestaurantName, chosenRestaurantAdress);
      }

    // ---------------- HIDE TOOLBAR AND NAVIGATION BAR ------------------ //
    @Override
    public void onResume() {
        super.onResume();
        hideActivityViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        showActivityViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showActivityViews();
    }

    private void hideActivityViews() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().findViewById(R.id.navbar).setVisibility(View.INVISIBLE);
    }

    private void showActivityViews() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        requireActivity().findViewById(R.id.navbar).setVisibility(View.VISIBLE);
    }
}
