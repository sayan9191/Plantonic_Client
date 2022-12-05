package com.example.plantonic.ui.logInSignUp.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.airbnb.lottie.L;
import com.example.plantonic.repo.LoginRepository;

public class LoginViewModel extends ViewModel {
    private final LoginRepository loginRepository = new LoginRepository();

    public LiveData<Boolean> checkIfUserExists(String userId){
        loginRepository.checkIfUserExists(userId);
        return loginRepository.userExists;
    }
}
