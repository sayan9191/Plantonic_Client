package com.example.plantonic.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.plantonic.firebaseClasses.UserItem
import com.example.plantonic.retrofit.models.auth.AuthRequestModel
import com.example.plantonic.retrofit.models.auth.AuthResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepository : BaseRepository() {
    private val _userExists: MutableLiveData<Boolean?> = MutableLiveData()
    val userExists: LiveData<Boolean?> = _userExists

    private val _userToken: MutableLiveData<String?> = MutableLiveData()
    var userToken: LiveData<String?> = _userToken


    fun checkIfUserExists(userId: String) {
        db.collection("users")
            .document(userId).get()
            .addOnCompleteListener { task -> _userExists.postValue(task.result.exists()) }
    }

    fun registerNewUser(userItem: UserItem): LiveData<Boolean?> {
        db.collection("users")
            .document(userItem.userId)
            .set(userItem).addOnCompleteListener { _userExists.postValue(true) }
        return userExists
    }

    fun getJwtToken(userId: String?): LiveData<String?> {
        val uid = aes.encrypt(userId)
        Log.d("UID: ", uid)
        api.getUserToken(AuthRequestModel(uid))
            .enqueue(object : Callback<AuthResponseModel?> {
                override fun onResponse(
                    call: Call<AuthResponseModel?>,
                    response: Response<AuthResponseModel?>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            _userToken.postValue(loginResponse.access_token)
                            Log.d("Login: ", "success")
                        }
                    } else {
                        _userToken.postValue(null)
                        Log.d("Login: ", "failed ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<AuthResponseModel?>, t: Throwable) {
                    _userToken.postValue(null)
                    Log.d("Login: ", "failed" + t.message)
                    t.stackTrace
                }
            })

        return userToken
    }
}