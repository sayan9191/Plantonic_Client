package com.example.plantonic.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plantonic.retrofit.models.CommonErrorModel
import com.example.plantonic.retrofit.models.pincode.PinCodeAvailableResponseModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlueDartRepository : BaseRepository(){

    private val _isPinCodeAvailable : MutableLiveData<PinCodeAvailableResponseModel?> = MutableLiveData()
    val isPinCodeAvailable : LiveData<PinCodeAvailableResponseModel?> = _isPinCodeAvailable


    fun checkIsPinCodeAvailable(pincode: String){
        isLoading.postValue(true)
        api.checkPinCodeAvailability("Bearer " + localStorage.token, pincode).enqueue(object: Callback<PinCodeAvailableResponseModel>{
            override fun onResponse(
                call: Call<PinCodeAvailableResponseModel>,
                response: Response<PinCodeAvailableResponseModel>
            ) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    errorMessage.postValue("")
                    response.body()?.let {
                        _isPinCodeAvailable.postValue(it)
                    }
                } else {
                    isLoading.postValue(false)
                    response.errorBody()?.let { errorBody ->
                        errorBody.string().let {
                            Log.e("Error: ", it)
                            val errorResponse: CommonErrorModel =
                                Gson().fromJson(it, CommonErrorModel::class.java)
                            errorMessage.postValue(errorResponse.detail)
                            Log.e("Error: ", errorResponse.detail)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PinCodeAvailableResponseModel>, t: Throwable) {
                Log.d("Request Failed. Error: ", t.message.toString())
                isLoading.postValue(false)
                errorMessage.postValue("Something went wrong")
            }

        })
    }
}