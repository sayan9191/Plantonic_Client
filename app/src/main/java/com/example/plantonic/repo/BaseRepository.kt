package com.example.plantonic.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plantonic.retrofit.RemoteApi
import com.example.plantonic.retrofit.models.auth.AuthRequestModel
import com.example.plantonic.retrofit.models.auth.AuthResponseModel
import com.example.plantonic.utils.NetworkUtils
import com.example.plantonic.utils.StorageUtil
import com.example.plantonic.utils.crypto.EasyAES
import com.google.firebase.firestore.FirebaseFirestore
import com.example.plantonic.retrofit.models.CommonErrorModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository{
    companion object{
        private var baseRepository: BaseRepository? = null
        fun getInstance(): BaseRepository{
            return if (baseRepository == null){
                baseRepository = BaseRepository()
                baseRepository as BaseRepository
            }else{
                baseRepository as BaseRepository
            }
        }
    }

    val localStorage = StorageUtil.getInstance()

    val api: RemoteApi = NetworkUtils.getRetrofitInstance().create(RemoteApi::class.java)
    val aes = EasyAES()
    val db = FirebaseFirestore.getInstance()


    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorMessage: MutableLiveData<String> = MutableLiveData()
//    val _error_message: MutableLiveData<String?> = MutableLiveData()
//    var error_message: LiveData<String?> = _error_message

//    fun GetApiCall(requestModel: T<T>){
//        T
//    }

}