package com.example.go4lunch.Activity.Main.Fragments.Settings;

import static com.example.go4lunch.Activity.Main.MainActivity.RADIUS_INIT;
import static com.example.go4lunch.Activity.Main.MainActivity.RADIUS_MAX;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.SettingViewModel;
import com.example.go4lunch.ViewModelFactory;
import com.google.android.material.slider.Slider;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private TextView textRadius;
    private Slider sliderRadius;
    private ImageButton buttonBackPress;
    private SwitchCompat switchNotifications;
    private SettingViewModel settingViewModel;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        settingViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(SettingViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        configureView(view);
        setClickHandler(view);
        radiusSliderListener(view);
        backPressHandler();
        return view;
    }

    private void setClickHandler(View view) {
        //Set a CheckedChange Listener for Switch Button
        switchNotifications.setOnCheckedChangeListener((cb, notificationChoice) -> settingViewModel.saveNotifications(view.getContext(), notificationChoice));
    }

    private void radiusSliderListener(View view) {
        sliderRadius.addOnChangeListener((slider, value, fromUser) -> {
            RADIUS_INIT = (int) value;
            textRadius.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));
            settingViewModel.saveRadius(view.getContext(), value);
        });
    }

    private void backPressHandler() {
        buttonBackPress.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void configureView(View view) {
        TextView title = view.findViewById(R.id.fragment_settings_title);
        textRadius = view.findViewById(R.id.fragment_settings_radius_text);
        buttonBackPress = view.findViewById(R.id.fragment_settings_backpress);
        switchNotifications = view.findViewById(R.id.fragment_settings_update_notifications_switch);
        sliderRadius = view.findViewById(R.id.fragment_settings_radius_slider);

        int CHOOSEN_RADIUS = Math.round(settingViewModel.getRadius(requireContext()));
        title.setText(R.string.fragment_settings_title);
        textRadius.setText(getString(R.string.fragment_settings_radius_search, CHOOSEN_RADIUS));

        sliderRadius.setValue(CHOOSEN_RADIUS);
        sliderRadius.setValueTo(RADIUS_MAX);
        // MAX Radius distance in meters
        int RADIUS_MIN = 1000;
        sliderRadius.setValueFrom(RADIUS_MIN);
        sliderRadius.setLabelFormatter(value -> String.valueOf(CHOOSEN_RADIUS));
        // STEP Radius for slider
        int RADIUS_STEP = 500;
        sliderRadius.setStepSize(RADIUS_STEP);

        switchNotifications.setChecked(settingViewModel.getNotifications(requireContext()));
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
