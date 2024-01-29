package co.`in`.plantonic.ui.activity.logInSignUp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.`in`.plantonic.firebaseClasses.UserItem
import co.`in`.plantonic.repo.LoginRepository

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()

    fun checkIfUserExists(userId: String): LiveData<Boolean?> {
        loginRepository.checkIfUserExists(userId)
        return loginRepository.userExists
    }

    fun registerUser(userItem: UserItem): LiveData<Boolean?> {
        return loginRepository.registerNewUser(userItem)
    }

    fun getUserToken (uid : String) : LiveData<String?> {
        return loginRepository.getJwtToken(uid)
    }

}