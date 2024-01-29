package co.`in`.plantonic.ui.activity.splash


import androidx.lifecycle.ViewModel
import co.`in`.plantonic.repo.LoginRepository
import androidx.lifecycle.LiveData

class SplashScreenViewModel : ViewModel() {
    private val loginRepository = LoginRepository()


    fun checkIfUserExists(userId: String?): LiveData<Boolean?> {
        loginRepository.checkIfUserExists(userId!!)
        return loginRepository.userExists
    }

    fun getUserToken (uid : String) : LiveData<String?> {
        return loginRepository.getJwtToken(uid)
    }

}