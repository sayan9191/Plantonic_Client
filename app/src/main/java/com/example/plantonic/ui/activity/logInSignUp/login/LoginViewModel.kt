package com.example.plantonic.ui.activity.logInSignUp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plantonic.firebaseClasses.UserItem
import com.example.plantonic.repo.LoginRepository

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()

    fun checkIfUserExists(userId: String?): LiveData<Boolean?>? {
        loginRepository.checkIfUserExists(userId)
        return loginRepository.userExists
    }

    fun registerUser(userItem: UserItem?): LiveData<Boolean?>? {
        return loginRepository.registerNewUser(userItem)
    }

}