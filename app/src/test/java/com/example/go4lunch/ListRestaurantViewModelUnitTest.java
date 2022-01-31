package com.example.go4lunch;

import static com.example.go4lunch.ViewModel.ListRestaurantViewModel.lRestaurantMutableLiveData;
import static com.example.go4lunch.ViewModel.ListRestaurantViewModel.lWorkmatesLiveData;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.Model.map.GeometryAPIMap;
import com.example.go4lunch.Model.map.Location;
import com.example.go4lunch.Model.map.LocationAPIMap;
import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.Model.map.ResultsAPIMap;
import com.example.go4lunch.Repository.AutoCompleteRepository;
import com.example.go4lunch.Repository.DetailRestaurantRepository;
import com.example.go4lunch.Repository.LocationRepository;
import com.example.go4lunch.Repository.NearByPlacesRepository;
import com.example.go4lunch.Repository.UserRepository;
import com.example.go4lunch.Service.PermissionChecker;
import com.example.go4lunch.ViewModel.ListRestaurantViewModel;
import com.example.go4lunch.ViewModel.RestaurantStateItem;
import com.example.go4lunch.utils.DataTest;
import com.example.go4lunch.utils.LiveDataTestUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;



@RunWith(MockitoJUnitRunner.class)
public class ListRestaurantViewModelUnitTest {
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application mApplication = MainApplication.getApplication();
    private final PermissionChecker permissionChecker = Mockito.mock(PermissionChecker.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final NearByPlacesRepository nearByPlacesRepository = Mockito.mock(NearByPlacesRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AutoCompleteRepository autoCompleteRepository = Mockito.mock(AutoCompleteRepository.class);
    private final DetailRestaurantRepository detailRestaurantRepository = Mockito.mock(DetailRestaurantRepository.class);

    private final ResultAPIMap mResultAPIMap = Mockito.mock(ResultAPIMap.class);
    private final ResultsAPIMap mResultsAPIMap = Mockito.mock(ResultsAPIMap.class);
    private final Location location = Mockito.mock(Location.class);
    private final GeometryAPIMap mNearbyGeometryAPIMap = Mockito.mock(GeometryAPIMap.class);
    private final LocationAPIMap mNearbyLocationAPIMap = Mockito.mock(LocationAPIMap.class);

    private final MutableLiveData<Location> userLocationLiveDataTest = new MutableLiveData<>();
    private final MutableLiveData<LatLng> mCurrentPositionMutableLiveDataTest = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantStateItem>> lRestaurantLiveDataStateItemTest = new MutableLiveData<>();
    private final MutableLiveData<List<ResultAPIMap>> lRestaurantLiveDataTest = new MutableLiveData<>();

    private ListRestaurantViewModel listRestaurantViewModel;

    /**
     * For these tests, all the results of the retrofit queries
     * are simulated in the DataTest class.
     * All repository calls are mock.
     * All POJO queries are mock.
     * LiveData set values from DataTest
     */
    @Before
    public void setUp() {
        //Mock result for nearby search
        List<ResultAPIMap> listOfNearbyResult = new ArrayList<>();

    }

    /**
     * This test verify that the ViewModel correctly combines data from LiveData.
     * If all the methods inherent in this ViewModel work, then the View State is created.
     * Assertions verify that the data passed in parameters in the LiveData,
     * are properly processed and compiled in the ViewState
     *
     * @throws InterruptedException uses the LiveDataTestUtils class, to manage async. of LiveData
     */

    @Test
    // Test the NearByPlacesRepository
    public void getNearByPlacesSortDistanceAsc() throws InterruptedException {
        //Before
        lRestaurantLiveDataTest.setValue(DataTest.generateListRestaurantAPITest());

        //When
        Mockito.when(nearByPlacesRepository.getNearbyPlacesSortDistanceASC("48.5244883,2.3842167"))
                .thenReturn(lRestaurantLiveDataTest);

        List<ResultAPIMap> listResultAPIMap = LiveDataTestUtils
                .getOrAwaitValue(nearByPlacesRepository.getNearbyPlacesSortDistanceASC("48.5244883,2.3842167"));

        //Then
        Assert.assertEquals("Restaurant 1 name", listResultAPIMap.get(0).getName());
        Assert.assertEquals("Restaurant 1", listResultAPIMap.get(0).getPlaceId());
        Assert.assertEquals(3.0, listResultAPIMap.get(0).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(0).getOpenNow().getOpen_now());
        Assert.assertEquals(48.5242751, listResultAPIMap.get(0).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3842851, listResultAPIMap.get(0).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(0).getPhotos().get(0).getPhoto_reference());
        Assert.assertEquals("adress Restaurant 1", listResultAPIMap.get(0).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(0).getTypes().get(0));

        Assert.assertEquals("Restaurant 2 name", listResultAPIMap.get(1).getName());
        Assert.assertEquals("Restaurant 2", listResultAPIMap.get(1).getPlaceId());
        Assert.assertEquals(1.0, listResultAPIMap.get(1).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(1).getOpenNow().getOpen_now());
        Assert.assertEquals(48.52426, listResultAPIMap.get(1).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3844617, listResultAPIMap.get(1).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(1).getPhotos().get(0).getPhoto_reference());
        Assert.assertEquals("adress Restaurant 2", listResultAPIMap.get(1).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(1).getTypes().get(0));

        Assert.assertEquals("Restaurant 3 name", listResultAPIMap.get(2).getName());
        Assert.assertEquals("Restaurant 3", listResultAPIMap.get(2).getPlaceId());
        Assert.assertEquals(5.0, listResultAPIMap.get(2).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(2).getOpenNow().getOpen_now());
        Assert.assertEquals(48.5240897, listResultAPIMap.get(2).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3841729, listResultAPIMap.get(2).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(2).getPhotos().get(0).getPhoto_reference());
        Assert.assertEquals("adress Restaurant 3", listResultAPIMap.get(2).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(2).getTypes().get(0));

        Assert.assertEquals("Restaurant 4 name", listResultAPIMap.get(3).getName());
        Assert.assertEquals("Restaurant 4", listResultAPIMap.get(3).getPlaceId());
        Assert.assertEquals(1.0, listResultAPIMap.get(3).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(3).getOpenNow().getOpen_now());
        Assert.assertEquals(48.5242751, listResultAPIMap.get(3).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3842851, listResultAPIMap.get(3).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(3).getPhotos().get(0).getPhoto_reference());
        Assert.assertEquals("adress Restaurant 4", listResultAPIMap.get(3).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(3).getTypes().get(0));
    }

    @Test
    // Test the LocationRepository
    public void testUserLocation() throws InterruptedException {
        //Before
        userLocationLiveDataTest.setValue(DataTest.generateCurrentPositionTest());

        //When
        Mockito.when(locationRepository.getLocationLiveData()).thenReturn(userLocationLiveDataTest);
        Location userLocation = LiveDataTestUtils.getOrAwaitValue(locationRepository.getLocationLiveData());

        //Then
        Assert.assertEquals(48.5244883, userLocation.getLat(),0.000001);
        Assert.assertEquals(2.3842167, userLocation.getLng(),0.00001);
    }

        @Test
    // Test the ViewModel - méthode : initList which initializes the viewStateItem using by the fragments Map & ListView
    public void getInitListStateItem() throws InterruptedException {
        //Before
        listRestaurantViewModel = new ListRestaurantViewModel(permissionChecker, locationRepository, nearByPlacesRepository,
                                                        userRepository, autoCompleteRepository, detailRestaurantRepository);
        // When
        lRestaurantMutableLiveData.setValue(DataTest.generateListRestaurantAPITest());
        lWorkmatesLiveData.setValue(DataTest.generateCoworkerTest());
        userLocationLiveDataTest.setValue(DataTest.generateCurrentPositionTest());

        Mockito.when(locationRepository.getLocationLiveData()).thenReturn(userLocationLiveDataTest);
        Mockito.when(userRepository.getAllUsersExceptCurrentUser()).thenReturn(lWorkmatesLiveData);

        List<RestaurantStateItem> lRestaurantStateItem = LiveDataTestUtils.getOrAwaitValue(listRestaurantViewModel.getListRestaurants());

        // Then
        Assert.assertEquals("restaurant 1 name", lRestaurantStateItem.get(0).getName());
        Assert.assertEquals("Restaurant 1", lRestaurantStateItem.get(0).getPlaceId());
        Assert.assertEquals(3.0, lRestaurantStateItem.get(0).getRating(), 0);
        Assert.assertEquals(true, lRestaurantStateItem.get(0).getOpenNow());
        Assert.assertEquals(48.5242751, lRestaurantStateItem.get(0).getRestaurantLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3842851, lRestaurantStateItem.get(0).getRestaurantLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , lRestaurantStateItem.get(0).getPhotos().get(0).getPhoto_reference());
        Assert.assertEquals("adress Restaurant 1", lRestaurantStateItem.get(0).getVicinity());
        Assert.assertEquals(1, lRestaurantStateItem.get(0).getNbWorkmates(),0);
        Assert.assertEquals(24, lRestaurantStateItem.get(0).getRestaurantDistance(), 0 );
    }

}