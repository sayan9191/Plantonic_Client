package com.example.plantonic.retrofit
import com.example.plantonic.retrofit.models.AuthRequestModel
import com.example.plantonic.retrofit.models.AuthResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RemoteApi {

    @POST("auth/")
    fun getUserToken(
        @Body body : AuthRequestModel
    ): Call<AuthResponseModel>
}