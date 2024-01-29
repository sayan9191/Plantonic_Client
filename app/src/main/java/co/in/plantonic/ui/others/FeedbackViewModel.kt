package co.`in`.plantonic.ui.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.`in`.plantonic.repo.FeedbackRepository
import co.`in`.plantonic.retrofit.models.feedback.FeedbackResponseModel

class FeedbackViewModel: ViewModel() {

    val repo = FeedbackRepository()

    val isLoading: LiveData<Boolean> = repo.isLoading
    val errorMessage: LiveData<String> = repo.errorMessage

    val feedbackResponse : LiveData<FeedbackResponseModel> = repo._feedbackResponse

    fun postFeedback(feedback: String){
        repo.postFeedback(feedback)
    }
}