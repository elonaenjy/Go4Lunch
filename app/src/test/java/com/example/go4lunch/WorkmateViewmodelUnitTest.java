package com.example.go4lunch;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.Model.User;
import com.example.go4lunch.Repository.UserRepository;
import com.example.go4lunch.ViewModel.WorkmateViewModel;
import com.example.go4lunch.utils.DataTest;
import com.example.go4lunch.utils.LiveDataTestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmateViewmodelUnitTest {

    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application mApplication = MainApplication.getApplication();
    private final UserRepository mUserRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<User> mCurrentUserDataTest = new MutableLiveData<>();
    private final MutableLiveData<List<User>> lWorkmatesLiveDataTest = new MutableLiveData<>();

    /**
     * For these tests, all the results of the retrofit queries
     * are simulated in the DataTest class.
     * All repository calls are mock.
     * All POJO queries are mock.
     *
     * LiveData set values from DataTest
     */
    @Before
    public void setUp() {
        //Mock access, Mockito return when method called
        lWorkmatesLiveDataTest.setValue(DataTest.generateCoworkerTest());

        Mockito.when(mUserRepository.getAllUsersExceptCurrentUser()).thenReturn(lWorkmatesLiveDataTest);
            }

    /**
     * This test verify that the ViewModel correctly combines data from LiveData.
     * If all the methods inherent in this ViewModel work, then the View State is created.
     * Assertions verify that the data passed in parameters in the LiveData,
     * are properly processed and compiled in the ViewState
     * @throws InterruptedException uses the LiveDataTestUtils class, to manage async. of LiveData
     */
    @Test
    public void findWorkmatesListExceptCurrentUser() throws InterruptedException {
        //Before
        lWorkmatesLiveDataTest.setValue(DataTest.generateCoworkerTest());

        //When
        WorkmateViewModel workmateViewModel = new WorkmateViewModel(mUserRepository);
        List<User> workmatesList = LiveDataTestUtils.getOrAwaitValue(workmateViewModel.getWorkmatesExceptCurrentUser());

        //Then
        Assert.assertEquals("Harry Name", workmatesList.get(0).getUserName());
        Assert.assertEquals("harrymail@orange.fr", workmatesList.get(0).getUserMail());
        Assert.assertEquals("https://i.pravatar.cc/150?u=a042581f4e29026704d", workmatesList.get(0).getUrlPicture());
        Assert.assertEquals("Harry Id", workmatesList.get(0).getUid());
        Assert.assertEquals("Restaurant 1", workmatesList.get(0).getChosenRestaurantId());
        Assert.assertEquals("Restaurant 1 name", workmatesList.get(0).getChosenRestaurantName());
        Assert.assertEquals("adress Restaurant 1", workmatesList.get(0).getChosenRestaurantAdress());
        Assert.assertEquals("10/01/2022",  workmatesList.get(0).getChosenRestaurantDate());

        Assert.assertEquals("Hermione Name", workmatesList.get(1).getUserName());
        Assert.assertEquals("hermionemail@orange.fr", workmatesList.get(1).getUserMail());
        Assert.assertEquals("https://i.pravatar.cc/150?u=a042581f4e29026704e", workmatesList.get(1).getUrlPicture());
        Assert.assertEquals("Hermione Id", workmatesList.get(1).getUid());
        Assert.assertEquals("Choosen Restaurant Id Hermione", workmatesList.get(1).getChosenRestaurantId());
        Assert.assertEquals("Choosen Restaurant Name Hermione", workmatesList.get(1).getChosenRestaurantName());
        Assert.assertEquals("Choosen Restaurant Adress Hermione", workmatesList.get(1).getChosenRestaurantAdress());
        Assert.assertEquals("10/01/2022",  workmatesList.get(1).getChosenRestaurantDate());

        Assert.assertEquals("Ron Name", workmatesList.get(2).getUserName());
        Assert.assertEquals("ronmail@orange.fr", workmatesList.get(2).getUserMail());
        Assert.assertEquals("https://i.pravatar.cc/150?u=a042581f4e29026704f", workmatesList.get(2).getUrlPicture());
        Assert.assertEquals("Ron Id", workmatesList.get(2).getUid());
        Assert.assertEquals("Choosen Restaurant Id Ron", workmatesList.get(2).getChosenRestaurantId());
        Assert.assertEquals("Choosen Restaurant Name Ron", workmatesList.get(2).getChosenRestaurantName());
        Assert.assertEquals("Choosen Restaurant Adress Ron", workmatesList.get(2).getChosenRestaurantAdress());
        Assert.assertEquals("10/01/2022",  workmatesList.get(2).getChosenRestaurantDate());

    }


}
