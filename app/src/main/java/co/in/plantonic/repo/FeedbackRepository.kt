package co.`in`.plantonic.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.`in`.plantonic.retrofit.models.CommonErrorModel
import co.`in`.plantonic.retrofit.models.feedback.FeedbackRequestModel
import co.`in`.plantonic.retrofit.models.feedback.FeedbackResponseModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackRepository : BaseRepository() {

    val _feedbackResponse : MutableLiveData<FeedbackResponseModel> = MutableLiveData()

    fun postFeedback(feedback: String) {
        isLoading.postValue(true)
        api.postFeedback("Bearer " + localStorage.token, FeedbackRequestModel(feedback, "")).enqueue(object : Callback<FeedbackResponseModel>{
            override fun onResponse(
                call: Call<FeedbackResponseModel>,
                response: Response<FeedbackResponseModel>
            ) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    errorMessage.postValue("")
                    response.body()?.let {
                        _feedbackResponse.postValue(it)
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

            override fun onFailure(call: Call<FeedbackResponseModel>, t: Throwable) {
                Log.d("Request Failed. Error: ", t.message.toString())
                isLoading.postValue(false)
                errorMessage.postValue("Something went wrong")
            }

        })
    }
}