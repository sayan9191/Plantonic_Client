package com.example.plantonic.retrofit
import com.example.plantonic.retrofit.models.auth.AuthRequestModel
import com.example.plantonic.retrofit.models.auth.AuthResponseModel
import com.example.plantonic.retrofit.models.pincode.PinCodeAvailableResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApi {

    @POST("auth/")
    fun getUserToken(
        @Body body : AuthRequestModel
    ): Call<AuthResponseModel>

    @GET("deliver/check_pin_code")
    fun checkPinCodeAvailability(
        @Query("pincode") pincode: String
    ) : Call<PinCodeAvailableResponseModel>

}