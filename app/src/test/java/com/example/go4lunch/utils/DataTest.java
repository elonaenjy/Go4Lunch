package com.example.go4lunch.utils;


import com.example.go4lunch.Model.RestaurantLiked;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.Model.map.GeometryAPIMap;
import com.example.go4lunch.Model.map.Location;
import com.example.go4lunch.Model.map.LocationAPIMap;
import com.example.go4lunch.Model.map.OpenStatutAPIMap;
import com.example.go4lunch.Model.map.PhotoAttributesAPIMap;
import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.ViewModel.RestaurantStateItem;

import java.util.ArrayList;
import java.util.List;

public class DataTest {

    private static final String sRestaurantNameTest = "Restaurant name test";
    private static final String sRestaurantAddressTest = "Restaurant address test";
    private static final boolean sRestaurantOpeningTest = true;
    private static final double sRestaurantRatingTest = 3;
    private static final String sPlaceIdTest = "123456789azertyuiop";
    private static final String sPhoneNumberTest = "0123456789";
    private static final String sRestaurantWebsiteTest = "www.restaurant.com";

    public static User generateUserTest() {
        User user = new User(
                "Current User Id",
                "Current User Name",
                "currentusermail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704d",
                "Choosen Restaurant Id",
                "Choosen Restaurant Name",
                "Choosen Restaurant Adress",
                "10/01/2022");
        return user;
    }

    public static double generateLatitudeFromNearbySearchTest() {
        return 46.578361;
    }

    public static double generateLongitudeFromNearbySearchTest() {
        return 0.339655;
    }

    public static String generateRestaurantPlaceIdTest() {
        return sPlaceIdTest;
    }

    public static Location generateCurrentPositionTest() {
        Location userLocation = new Location();
        userLocation.setLat(48.5244883);
        userLocation.setLng(2.3842167);
        return userLocation;
    }

    public static List<User> generateCoworkerTest() {
        List<User> fakeCoworker = new ArrayList<>();
        User harry = new User(
                "Harry Id",
                "Harry Name",
                "harrymail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704d",
                "Restaurant 1",
                "Restaurant 1 name",
                "adress Restaurant 1",
                "10/01/2022");
        fakeCoworker.add(harry);

        User hermione = new User(
                "Hermione Id",
                "Hermione Name",
                "hermionemail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704e",
                "Choosen Restaurant Id Hermione",
                "Choosen Restaurant Name Hermione",
                "Choosen Restaurant Adress Hermione",
                "10/01/2022");
        fakeCoworker.add(hermione);

        User ron = new User(
                "Ron Id",
                "Ron Name",
                "ronmail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704f",
                "Choosen Restaurant Id Ron",
                "Choosen Restaurant Name Ron",
                "Choosen Restaurant Adress Ron",
                "10/01/2022");
        fakeCoworker.add(ron);

        return fakeCoworker;
    }

    public static String getRestaurantNameTest() {
        return sRestaurantNameTest;
    }

    public static String getRestaurantAddressTest() {
        return sRestaurantAddressTest;
    }

    public static boolean getRestaurantOpeningTest() {
        return sRestaurantOpeningTest;
    }

    public static float getRestaurantRatingTest() {
        return (float) sRestaurantRatingTest;
    }

    public static String getRestaurantWebsiteTest() {
        return sRestaurantWebsiteTest;
    }

    public static String getPhoneNumberTest() {
        return sPhoneNumberTest;
    }

    public static List<RestaurantLiked> getFavoriteRestaurantTest() {
        List<RestaurantLiked> favoriteRestaurantList = new ArrayList<>();
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 1", "Ron Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 1", "Hermione Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 2", "Harry Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 3", "Harry Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 4", "Ron Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 5", "Harry"));

        return favoriteRestaurantList;
    }

    public static List<RestaurantStateItem> generateListRestaurantTest() {
        List<RestaurantStateItem> lRestaurantStateItem = new ArrayList<>();
        lRestaurantStateItem.add(new RestaurantStateItem("Restaurant 1",
                                                        "Restaurant 1 name",
                                                         3F,
                                                        true,
                                                        "adress Restaurant 1",
                                                        getLocationRestaurant1(),
                                                        24,
                                                        getPhotosAttributes1(),
                                                        0));
        lRestaurantStateItem.add(new RestaurantStateItem("Restaurant 2",
                "Restaurant 2 name",
                1F,
                false,
                "adress Restaurant 2",
                getLocationRestaurant2(),
                31,
                getPhotosAttributes1(),
                5));

        lRestaurantStateItem.add(new RestaurantStateItem("Restaurant 3",
                "Restaurant 3 name",
                5F,
                true,
                "adress Restaurant 3",
                getLocationRestaurant3(),
                44,
                getPhotosAttributes1(),
                0));

        lRestaurantStateItem.add(new RestaurantStateItem("Restaurant 4",
                "Restaurant 4 name",
                1F,
                true,
                "adress Restaurant 4",
                getLocationRestaurant4(),
                54,
                getPhotosAttributes1(),
                9));
        return lRestaurantStateItem;
    }

    private static LocationAPIMap getLocationRestaurant1() {
        LocationAPIMap location1 = new LocationAPIMap();
        location1.setLat(48.5242751);
        location1.setLng(2.3842851);
        return location1;
    }

    private static List<PhotoAttributesAPIMap> getPhotosAttributes1() {
        ArrayList<PhotoAttributesAPIMap> lPhoto1 = new ArrayList<>();
        PhotoAttributesAPIMap photo1 = new PhotoAttributesAPIMap();
        photo1.setPhoto_reference("Photo Restaurant");
        photo1.setHeight(360);
        photo1.setWidth(3000);
        photo1.setHtml_attributions(null);
        lPhoto1.add(photo1);
    return lPhoto1;
    }


    private static LocationAPIMap getLocationRestaurant2() {
        LocationAPIMap location1 = new LocationAPIMap();
        location1.setLat(48.52426);
        location1.setLng(2.3844617);
        return location1;
    }
    private static LocationAPIMap getLocationRestaurant3() {
        LocationAPIMap location1 = new LocationAPIMap();
        location1.setLat(48.5240897);
        location1.setLng(2.3841729);
        return location1;
    }
    private static LocationAPIMap getLocationRestaurant4() {
        LocationAPIMap location1 = new LocationAPIMap();
        location1.setLat(48.5244272);
        location1.setLng(2.383495);
        return location1;
    }

    public static List<ResultAPIMap> generateListRestaurantAPITest() {
        ArrayList<ResultAPIMap> lRestaurantNearByPlace = new ArrayList();
            lRestaurantNearByPlace.add(new ResultAPIMap("Restaurant 1",
                "Restaurant 1 name",
                3F,
                getOpenNow(),
                    "adress Restaurant 1",
                getGeometryRestaurant1(),
                getPhotosAttributes1(),
                getListType()
                ));

        lRestaurantNearByPlace.add(new ResultAPIMap("Restaurant 2",
                "Restaurant 2 name",
                1F,
                getOpenNow(),
                "adress Restaurant 2",
                getGeometryRestaurant2(),
                getPhotosAttributes1(),
                getListType()
        ));
        lRestaurantNearByPlace.add(new ResultAPIMap("Restaurant 3",
                "Restaurant 3 name",
                5F,
                getOpenNow(),
                "adress Restaurant 3",
                getGeometryRestaurant3(),
                getPhotosAttributes1(),
                getListType()
        ));
        lRestaurantNearByPlace.add(new ResultAPIMap("Restaurant 4",
                "Restaurant 4 name",
                1F,
                getOpenNow(),
                "adress Restaurant 4",
                getGeometryRestaurant1(),
                getPhotosAttributes1(),
                getListType()
        ));

        return lRestaurantNearByPlace;
    }

    private static GeometryAPIMap getGeometryRestaurant1() {
        GeometryAPIMap geometryAPIMap1 = new GeometryAPIMap();
        geometryAPIMap1.setLocation(getLocationRestaurant1());
        geometryAPIMap1.setViewport(null);
        return geometryAPIMap1;
    }

    private static GeometryAPIMap getGeometryRestaurant2() {
        GeometryAPIMap geometryAPIMap1 = new GeometryAPIMap();
        geometryAPIMap1.setLocation(getLocationRestaurant2());
        geometryAPIMap1.setViewport(null);
        return geometryAPIMap1;
    }
    private static GeometryAPIMap getGeometryRestaurant3() {
        GeometryAPIMap geometryAPIMap1 = new GeometryAPIMap();
        geometryAPIMap1.setLocation(getLocationRestaurant3());
        geometryAPIMap1.setViewport(null);
        return geometryAPIMap1;
    }

    private static OpenStatutAPIMap getOpenNow() {
        OpenStatutAPIMap openstatut = new OpenStatutAPIMap();
        openstatut.setOpen_now(true);
        return openstatut;
    }
    private static ArrayList<String> getListType() {
    ArrayList<String> lType = new ArrayList<>();
    lType.add("restaurant");
    return lType;

    }


}

