package co.`in`.plantonic.repo

import androidx.lifecycle.MutableLiveData
import co.`in`.plantonic.retrofit.RemoteApi
import co.`in`.plantonic.utils.NetworkUtils
import co.`in`.plantonic.utils.StorageUtil
import co.`in`.plantonic.utils.crypto.EasyAES
import com.google.firebase.firestore.FirebaseFirestore

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