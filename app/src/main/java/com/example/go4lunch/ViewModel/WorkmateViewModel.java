package com.example.go4lunch.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Model.User;
import com.example.go4lunch.Model.map.ResultAPIMap;
import com.example.go4lunch.Repository.UserRepository;

import java.util.List;

public class WorkmateViewModel extends ViewModel {
    UserRepository userRepository;

    private static MutableLiveData<List<User>> lWorkmatesExceptCurrentUser = new MutableLiveData<>();

    public WorkmateViewModel(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<List<User>> getWorkmatesExceptCurrentUser() {
        lWorkmatesExceptCurrentUser = userRepository.getAllUsersExceptCurrentUser();
        return lWorkmatesExceptCurrentUser;
    }

}