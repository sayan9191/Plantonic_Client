package com.example.plantonic.ui.logInSignUp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.UserItem;
import com.example.plantonic.repo.LoginRepository;

public class SignUpViewModel extends ViewModel {

    private LoginRepository loginRepository = new LoginRepository();

    public LiveData<Boolean> registerUser(UserItem userItem){
        return loginRepository.registerNewUser(userItem);
    }
}
