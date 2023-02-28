package com.example.myschoolwearclient.ui.logInSignUp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myschoolwearclient.firebaseClasses.UserItem;
import com.example.myschoolwearclient.repo.LoginRepository;

public class SignUpViewModel extends ViewModel {

    private LoginRepository loginRepository = new LoginRepository();

    public LiveData<Boolean> registerUser(UserItem userItem){
        return loginRepository.registerNewUser(userItem);
    }
}
