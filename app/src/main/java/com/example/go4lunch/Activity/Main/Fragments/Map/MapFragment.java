package com.example.go4lunch.Activity.Main.Fragments.Map;

import static com.example.go4lunch.Activity.Main.MainActivity.RADIUS_INIT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.Model.map.Location;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.ListRestaurantViewModel;
import com.example.go4lunch.ViewModel.RestaurantStateItem;
import com.example.go4lunch.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapFragment";
    private static final float INITIAL_ZOOM = 14;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float LOCATION_CHANGE_THRESHOLD = 25;

    private ListRestaurantViewModel listRestaurantViewModel;

    // The entry point to the Fused Location Provider.
    public static Location userLocationOld;
    public static LatLng userLatLng;
    public static String userLocationStr;
    public LatLng restauLatLng;
    public LatLng mapLatLng;

    private GoogleMap mMap;

    private SupportMapFragment mMapFragment;

    public MapFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        listRestaurantViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ListRestaurantViewModel.class);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mMapFragment).commit();
        }

        mMapFragment.getMapAsync(this);

        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.setGroupVisible(R.id.main_activity_restaurants_group_sort, false);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // UI MAP //
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        /*
          Manipulates the map once available.
          This callback is triggered when the map is ready to be used.
          This is where we can add markers or lines, add listeners or move the camera.
          In this case, we just add a marker near Sydney, Australia.
          If Google Play services is not installed on the device, the user will be prompted to
          install it inside the SupportMapFragment. This method will only be triggered once the
          user has installed Google Play services and returned to the app.
         */
        mMap = googleMap;

        // Show the button to zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        View mapView = mMapFragment.getView();
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0
        );

        listRestaurantViewModel.getGpsMessageLiveData().observe(getViewLifecycleOwner(),
                userLocationNew -> {
                    if (userLocationNew != null) {
                        userLatLng = new LatLng(userLocationNew.getLat(), userLocationNew.getLng());
//                        if (userLocationOld == null || (userLocationNew.distanceTo(userLocationOld) >= LOCATION_CHANGE_THRESHOLD)) {
                            if (userLocationOld == null) {
                            userLocationOld = userLocationNew;
                            userLocationStr = userLocationNew.getLat() + "," + userLocationNew.getLng();
                            listRestaurantViewModel.initListViewMutableLiveData(userLocationStr);
                        }
                    }
                    userLocationOld = userLocationNew;
                    alimMap(mapView);
                }
        );
    }

    private void alimMap(View mapView) {

        enableCompassButton();
        moveCompassButton(mapView);
        getBaseList();

        // Handle click on marker info
        mMapSetUpClickListener();
    }
    private void getBaseList() {
        listRestaurantViewModel.getListRestaurants().observe(requireActivity(),
                changedListRestaurantsStateItem -> {
                    //Do something with the changed value
                    if (changedListRestaurantsStateItem != null) {
                        if (changedListRestaurantsStateItem.isEmpty()) {
                            extendRadiusDialog();
                        } else {
                            addMarkerResult(changedListRestaurantsStateItem);
                            if (mMap != null && userLatLng != null) {
                                if (changedListRestaurantsStateItem.size() == 1) {
                                    mapLatLng = restauLatLng;
                                } else {
                                    mapLatLng = userLatLng;
                                }

                                MapFragment.this.zoomOnCurrentPosition(mMap);
                            }
                        }
                    } else {
                        extendRadiusDialog();
                    }
                });
    }

    private void zoomOnCurrentPosition(GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, INITIAL_ZOOM));
    }

    // Handle click on marker info
    private void mMapSetUpClickListener() {
        mMap.setOnInfoWindowClickListener(marker -> Log.d(TAG, "Click on marker " + marker.getTag()));
    }

    @SuppressLint("StringFormatInvalid")
    private void extendRadiusDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(getString(R.string.alertdialog_extendradius_message, RADIUS_INIT))
                .setTitle(R.string.alertdialog_extendradius_title);
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            RADIUS_INIT += 1000;
            listRestaurantViewModel.initListViewMutableLiveData(userLocationStr);
            dialog1.dismiss();
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialog.show();
    }

    // + show the marker of the restaurant on the map
    private void addMarkerResult(List<RestaurantStateItem> results) {
        mMap.clear();
        for (RestaurantStateItem result : results) {
            boolean bChosen = false;
            if (result.getNbWorkmates() != 0) {
                bChosen = true;
            }

            Marker markerRestaurant;
            if (bChosen) {
                markerRestaurant = showChosenRestaurantMarker(result);
            } else {
                markerRestaurant = showRegularRestaurantMarker(result);
            }
            restauLatLng = new LatLng(result.getRestaurantLocation().getLat(), result.getRestaurantLocation().getLng());
        }
    }

    private Marker showRegularRestaurantMarker(RestaurantStateItem result) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        result.getRestaurantLocation().getLat(),
                        result.getRestaurantLocation().getLng()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(result.getName())
                .snippet(result.getVicinity()));
    }

    private Marker showChosenRestaurantMarker(RestaurantStateItem result) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        result.getRestaurantLocation().getLat(),
                        result.getRestaurantLocation().getLng()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(result.getName())
                .snippet(result.getVicinity()));
    }

    // ---------------------- COMPASS BUTTON ----------------------//
    @SuppressLint("MissingPermission") // Permission managed before this call
    private void enableCompassButton() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        zoomOnCurrentPosition(mMap);
        return false;
    }

    // UI COMPASS BUTTON location
    private void moveCompassButton(View mapView) {
        try {
            assert mapView != null; // skip this if the mapView has not been set yet
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.setMargins(0, 0, 150, 40);

            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } catch (Exception ex) {
            Log.e(TAG, "MoveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



}