package com.example.plantonic.ui.homeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.plantonic.firebaseClasses.CategoryItem
import com.example.plantonic.firebaseClasses.PopularProductItem
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.repo.LoginRepository
import com.example.plantonic.repo.Repository
import com.example.plantonic.retrofit.models.home.HomePageBannerResponseModel

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