package com.example.plantonic.ui.activity.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.repo.LoginRepository;

public class SplashScreenViewModel extends ViewModel {
    private final LoginRepository loginRepository = new LoginRepository();

    public LiveData<Boolean> checkIfUserExists(String userId){
        loginRepository.checkIfUserExists(userId);
        return loginRepository.userExists;
    }
}
