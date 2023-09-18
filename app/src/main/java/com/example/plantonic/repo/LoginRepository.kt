package com.example.plantonic.repo

import android.util.Log
import com.example.plantonic.utils.NetworkUtils.Companion.getRetrofitInstance
import com.example.plantonic.retrofit.RemoteApi
import com.example.plantonic.utils.NetworkUtils
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.example.plantonic.firebaseClasses.UserItem
import com.example.plantonic.utils.crypto.EasyAES
import com.example.plantonic.retrofit.models.AuthRequestModel
import com.example.plantonic.retrofit.models.AuthResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    private val aes = EasyAES()

    private val api = getRetrofitInstance().create(RemoteApi::class.java)
    private val db = FirebaseFirestore.getInstance()

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
        api.getUserToken(AuthRequestModel(aes.encrypt(userId)))
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
                        Log.d("Login: ", "failed")
                    }
                }

                override fun onFailure(call: Call<AuthResponseModel?>, t: Throwable) {
                    _userToken.postValue(null)
                    Log.d("Login: ", "failed")
                    t.stackTrace
                }
            })

        return userToken
    }
}