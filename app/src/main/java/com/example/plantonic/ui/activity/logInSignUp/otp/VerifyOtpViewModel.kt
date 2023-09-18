package com.example.plantonic.ui.activity.logInSignUp.otp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.UserItem;
import com.example.plantonic.repo.LoginRepository;

public class VerifyOtpViewModel extends ViewModel {

    LoginRepository loginRepository = new LoginRepository();

    public LiveData<Boolean> checkIfUserExists(String userId){
        loginRepository.checkIfUserExists(userId);
        return loginRepository.userExists;
    }

    public LiveData<Boolean> registerUser(UserItem userItem){
        return loginRepository.registerNewUser(userItem);
    }
}
