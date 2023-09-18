package com.example.plantonic.ui.activity.logInSignUp.otp

import androidx.lifecycle.ViewModel
import com.example.plantonic.repo.LoginRepository
import androidx.lifecycle.LiveData
import com.example.plantonic.firebaseClasses.UserItem

class VerifyOtpViewModel : ViewModel() {
    var loginRepository = LoginRepository()
    fun checkIfUserExists(userId: String): LiveData<Boolean?> {
        loginRepository.checkIfUserExists(userId)
        return loginRepository.userExists
    }

    fun registerUser(userItem: UserItem?): LiveData<Boolean?> {
        return loginRepository.registerNewUser(userItem!!)
    }

    fun getUserToken (uid : String) : LiveData<String?> {
        return loginRepository.getJwtToken(uid)
    }
}