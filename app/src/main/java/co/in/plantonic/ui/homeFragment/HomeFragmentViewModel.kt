package co.`in`.plantonic.ui.homeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import co.`in`.plantonic.firebaseClasses.ProductItem
import co.`in`.plantonic.repo.LoginRepository
import co.`in`.plantonic.repo.Repository
import co.`in`.plantonic.retrofit.models.home.HomePageBannerResponseModel

class HomeFragmentViewModel : ViewModel() {
    private val repository = Repository.instance
    private val loginRepository = LoginRepository()


    var allCategories = repository?.categories

//    private val allPopularProducts: LiveData<List<PopularProductItem>> = repository.allPopularProducts()

    val allPopularProductItems: LiveData<ArrayList<ProductItem>>? = repository?.popularProductItems()


    fun checkIfUserExists(userId: String?): LiveData<Boolean?> {
        loginRepository.checkIfUserExists(userId!!)
        return loginRepository.userExists
    }

    fun getUserToken (uid : String) : LiveData<String?> {
        return loginRepository.getJwtToken(uid)
    }

    val getAllBanners : LiveData<HomePageBannerResponseModel>? = repository?.homePageBanners
}