package com.example.go4lunch;

import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.Util.SortRestaurantsUtil;
import com.example.go4lunch.ViewModel.RestaurantStateItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortRestaurantsUnitTest {

    final RestaurantStateItem restaurantStateItem1 = new RestaurantStateItem();
    final RestaurantStateItem restaurantStateItem2 = new RestaurantStateItem();
    final RestaurantStateItem restaurantStateItem3 = new RestaurantStateItem();
    final RestaurantStateItem restaurantStateItem4 = new RestaurantStateItem();

    final ArrayList<RestaurantStateItem> listToSort = new ArrayList<>();

    final ArrayList<RestaurantStateItem> listSortByNameAZ = new ArrayList<>();
    final ArrayList<RestaurantStateItem> listSortByNameZA = new ArrayList<>();
    final ArrayList<RestaurantStateItem> listSortByRating = new ArrayList<>();
    final ArrayList<RestaurantStateItem> listSortByNbWorkmates = new ArrayList<>();
    final ArrayList<RestaurantStateItem> listSortByDistanceAsc = new ArrayList<>();

    @Before
    public void setUp() {
        // alim restaurantStateItem1
        restaurantStateItem1.setPlaceId("placeId1");
        restaurantStateItem1.setName("name1");
        restaurantStateItem1.setRating(3F);
        restaurantStateItem1.setRestaurantDistance(100);
        restaurantStateItem1.setRestaurantLocation(null);
        restaurantStateItem1.setNbWorkmates(1);
        restaurantStateItem1.setVicinity(null);
        restaurantStateItem1.setPhotos(null);
        restaurantStateItem1.setOpen_false(true);

        // alim restaurantStateItem2
        restaurantStateItem2.setPlaceId("placeId2");
        restaurantStateItem2.setName("name2");
        restaurantStateItem2.setRating(2F);
        restaurantStateItem2.setRestaurantDistance(250);
        restaurantStateItem2.setRestaurantLocation(null);
        restaurantStateItem2.setNbWorkmates(19);
        restaurantStateItem2.setVicinity(null);
        restaurantStateItem2.setPhotos(null);
        restaurantStateItem2.setOpen_false(true);

        // alim restaurantStateItem3
        restaurantStateItem3.setPlaceId("placeId3");
        restaurantStateItem3.setName("name3");
        restaurantStateItem3.setRating(5F);
        restaurantStateItem3.setRestaurantDistance(3250);
        restaurantStateItem3.setRestaurantLocation(null);
        restaurantStateItem3.setNbWorkmates(5);
        restaurantStateItem3.setVicinity(null);
        restaurantStateItem3.setPhotos(null);
        restaurantStateItem3.setOpen_false(true);

        // alim restaurantStateItem4
        restaurantStateItem4.setPlaceId("placeId4");
        restaurantStateItem4.setName("name4");
        restaurantStateItem4.setRating(0F);
        restaurantStateItem4.setRestaurantDistance(81);
        restaurantStateItem4.setRestaurantLocation(null);
        restaurantStateItem4.setNbWorkmates(0);
        restaurantStateItem4.setVicinity(null);
        restaurantStateItem4.setPhotos(null);
        restaurantStateItem4.setOpen_false(true);

        setUpLists();
    }

    private void setUpLists() {
        // init listToSort
        listToSort.add(restaurantStateItem1);
        listToSort.add(restaurantStateItem2);
        listToSort.add(restaurantStateItem3);
        listToSort.add(restaurantStateItem4);

        // init listSortByNameAZ
        listSortByNameAZ.add(restaurantStateItem1);
        listSortByNameAZ.add(restaurantStateItem2);
        listSortByNameAZ.add(restaurantStateItem3);
        listSortByNameAZ.add(restaurantStateItem4);

        // init listSortByNameZA
        listSortByNameZA.add(restaurantStateItem4);
        listSortByNameZA.add(restaurantStateItem3);
        listSortByNameZA.add(restaurantStateItem2);
        listSortByNameZA.add(restaurantStateItem1);

        // init listSortByDistance
        listSortByDistanceAsc.add(restaurantStateItem4);
        listSortByDistanceAsc.add(restaurantStateItem1);
        listSortByDistanceAsc.add(restaurantStateItem2);
        listSortByDistanceAsc.add(restaurantStateItem3);

        // init listSortByNbWorkmates
        listSortByNbWorkmates.add(restaurantStateItem2);
        listSortByNbWorkmates.add(restaurantStateItem3);
        listSortByNbWorkmates.add(restaurantStateItem1);
        listSortByNbWorkmates.add(restaurantStateItem4);

        // init listSortByNRating
        listSortByRating.add(restaurantStateItem3);
        listSortByRating.add(restaurantStateItem1);
        listSortByRating.add(restaurantStateItem2);
        listSortByRating.add(restaurantStateItem4);
    }

    @Test
    public void testSortZA() {
        ArrayList<RestaurantStateItem> sortedList = SortRestaurantsUtil.sortZA(listToSort);
        Assert.assertEquals(sortedList, listSortByNameZA);
    }
    @Test
    public void testSortAZ() {
        ArrayList<RestaurantStateItem> sortedList = SortRestaurantsUtil.sortAZ(listToSort);
        Assert.assertEquals(sortedList, listSortByNameAZ);
    }
    @Test
    public void testSortByDistance() {
        ArrayList<RestaurantStateItem> sortedList = SortRestaurantsUtil.sortDistanceAsc(listToSort);
        Assert.assertEquals(sortedList, listSortByDistanceAsc);
    }
    @Test
    public void testSortByNbWorkmates() {
        ArrayList<RestaurantStateItem> sortedList = SortRestaurantsUtil.sortWorkmatesDesc(listToSort);
        Assert.assertEquals(sortedList, listSortByNbWorkmates);
    }
    @Test
    public void testSortByRatings() {
        ArrayList<RestaurantStateItem> sortedList = SortRestaurantsUtil.sortRatingDesc(listToSort);
        Assert.assertEquals(sortedList, listSortByRating);
    }

}
