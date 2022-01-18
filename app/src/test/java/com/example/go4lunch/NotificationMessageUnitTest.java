package com.example.go4lunch;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.go4lunch.Model.RestaurantChoice;
import com.example.go4lunch.Model.User;
import com.example.go4lunch.Util.NotificationWorker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class NotificationMessageUnitTest {
    final RestaurantChoice restaurantNoCoWorker = new RestaurantChoice();
    final RestaurantChoice restaurantOneCoWorker = new RestaurantChoice();
    final RestaurantChoice restaurantMoreThanOneCoWorker = new RestaurantChoice();

    final User user1 = new User();
    final User user2 = new User();
    final User user3 = new User();
    final User user4 = new User();

    final User user5 = new User();
    final User user6 = new User();
    final User user7 = new User();

    final ArrayList<User> users = new ArrayList<>();


    @Before
    public void setUp() {
        // alim restaurantChoicenoCoWorker
        restaurantNoCoWorker.setChoosenDate("2022/01/03");
        restaurantNoCoWorker.setChoosenRestaurantAdress("adress no coworker");
        restaurantNoCoWorker.setChoosenRestaurantId("id no coworker");
        restaurantNoCoWorker.setChoosenRestaurantName("restaurant name no coworker");
        restaurantNoCoWorker.setUserId("id no coworker");

        // alim restaurantOneCoWorker
        restaurantOneCoWorker.setChoosenDate("2022/01/23");
        restaurantOneCoWorker.setChoosenRestaurantAdress("adress one coworker");
        restaurantOneCoWorker.setChoosenRestaurantId("id one coworker");
        restaurantOneCoWorker.setChoosenRestaurantName("restaurant name one coworker");
        restaurantOneCoWorker.setUserId("User id one coWorker");

        // alim restaurantMoreThanOneCoWorker
        restaurantMoreThanOneCoWorker.setChoosenDate("2022/01/23");
        restaurantMoreThanOneCoWorker.setChoosenRestaurantAdress("adress > one coworker");
        restaurantMoreThanOneCoWorker.setChoosenRestaurantId("Id eating with more than one CoWorker");
        restaurantMoreThanOneCoWorker.setChoosenRestaurantName("restaurant name > one coworker");
        restaurantMoreThanOneCoWorker.setUserId("User4");

        // alim listeUser
        user1.setUid("id no coworker");
        user1.setUserName("User noChoice");
        user1.setUserMail(null);
        user1.setChosenRestaurantAdress("id no coWorker");
        user1.setChosenRestaurantId("id no coworker");
        user1.setChosenRestaurantDate("2022/11/23");

        user2.setUid("User id one coWorker");
        user2.setUserName("User eating with one coWorker");
        user2.setUserMail(null);
        user2.setChosenRestaurantAdress("adress > one coworker");
        user2.setChosenRestaurantId("id one coworker");
        user2.setChosenRestaurantDate("2022/01/23");

        user3.setUid("User3");
        user3.setUserName("User eating with one coWorker");
        user3.setUserMail(null);
        user3.setChosenRestaurantAdress("Adress eating with one CoWorker");
        user3.setChosenRestaurantId("id one coworker");
        user3.setChosenRestaurantDate("2022/01/23");

        user4.setUid("User4");
        user4.setUserName("User4");
        user4.setUserMail(null);
        user4.setChosenRestaurantAdress("Adress eating with one CoWorker");
        user4.setChosenRestaurantId("Id eating with more than one CoWorker");
        user4.setChosenRestaurantDate("2022/01/23");

        user5.setUid("User5");
        user5.setUserName("User5");
        user5.setUserMail(null);
        user5.setChosenRestaurantAdress("Adress eating with more one CoWorker");
        user5.setChosenRestaurantId("Id eating with more than one CoWorker");
        user5.setChosenRestaurantDate("2022/01/23");

        user6.setUid("User6");
        user6.setUserName("User6");
        user6.setUserMail(null);
        user6.setChosenRestaurantAdress("Adress eating with more one CoWorker");
        user6.setChosenRestaurantId("Id eating with more than one CoWorker");
        user6.setChosenRestaurantDate("2022/01/23");

        user7.setUid("User7");
        user7.setUserName("User7");
        user7.setUserMail(null);
        user7.setChosenRestaurantAdress("Adress eating with more one CoWorker");
        user7.setChosenRestaurantId("Id eating with more  one CoWorker");
        user7.setChosenRestaurantDate("2022/01/23");

        setUpLists();
    }

    private void setUpLists() {
        // init users
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
    }
    @Test
    public void testMessageAlone() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String message = NotificationWorker.setUpMessageWithUsersData(users, restaurantNoCoWorker, appContext);

        String wmessage = " You chose the restaurant: "+ restaurantNoCoWorker.getChoosenRestaurantName()+ ".\n"  +
                "located at " + restaurantNoCoWorker.getChoosenRestaurantAdress() + "\n" +
                "Unfortunately, you will eat alone…";
        Assert.assertEquals(message, wmessage);
    }
    @Test
    public void testMessageWithOneWorkmate() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String message = NotificationWorker.setUpMessageWithUsersData(users, restaurantOneCoWorker, appContext);

        String wmessage = "You chose the restaurant: " + restaurantOneCoWorker.getChoosenRestaurantName()+ " \n"  +
                "located at " + restaurantOneCoWorker.getChoosenRestaurantAdress() + " \n" +
                "You will lunch with one workmate: " + users.get(1).getUserName() + "." ;
        Assert.assertEquals(message, wmessage);
    }

    @Test
    public void testMessageWithMoreThanOneWorkmate() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String message = NotificationWorker.setUpMessageWithUsersData(users, restaurantMoreThanOneCoWorker, appContext);

        String wmessage = "You chose the restaurant: " + restaurantMoreThanOneCoWorker.getChoosenRestaurantName()+ " \n"  +
                " located at " + restaurantMoreThanOneCoWorker.getChoosenRestaurantAdress() + ".\n" +
                " You will lunch with 2 workmates: " + users.get(4).getUserName() + " and " + users.get(5).getUserName() + "." ;
        Assert.assertEquals(message, wmessage);
    }


}
