package co.`in`.plantonic.retrofit
import co.`in`.plantonic.retrofit.models.auth.AuthRequestModel
import co.`in`.plantonic.retrofit.models.auth.AuthResponseModel
import co.`in`.plantonic.retrofit.models.feedback.FeedbackRequestModel
import co.`in`.plantonic.retrofit.models.feedback.FeedbackResponseModel
import co.`in`.plantonic.retrofit.models.home.HomePageBannerResponseModel
import co.`in`.plantonic.retrofit.models.order.GetOrdersResponseModel
import co.`in`.plantonic.retrofit.models.order.PlaceOrderRequestModel
import co.`in`.plantonic.retrofit.models.order.PlaceOrderResponseModel
import co.`in`.plantonic.retrofit.models.pincode.PinCodeAvailableResponseModel
import co.`in`.plantonic.retrofit.models.track.TrackOrderRequestModel
import co.`in`.plantonic.retrofit.models.track.TrackOrderResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApi {

    @POST("auth/")
    fun getUserToken(
        @Body body : AuthRequestModel
    ): Call<AuthResponseModel>

    @GET("deliver/check_pin_code")
    fun checkPinCodeAvailability(
        @Header("Authorization") token: String,
        @Query("pincode") pincode: String
    ) : Call<PinCodeAvailableResponseModel>

    @GET("home/get_banners")
    fun getAllHomePageBanners() : Call<HomePageBannerResponseModel>

    @POST("deliver/place_order")
    fun placeOrder(@Header("Authorization") token: String,
                   @Body body: PlaceOrderRequestModel
    ) : Call<PlaceOrderResponseModel>

    @GET("deliver/orders")
    fun getOrders(@Header("Authorization") token: String,
                  @Query("page") page: Int) : Call<GetOrdersResponseModel>

    @POST("deliver/track_order")
    fun trackOrder(@Header("Authorization") token: String,
                   @Body body: TrackOrderRequestModel) : Call<TrackOrderResponseModel>

    @POST("profile/feedback")
    fun postFeedback(@Header("Authorization") token: String,
                     @Body body : FeedbackRequestModel) : Call<FeedbackResponseModel>
}