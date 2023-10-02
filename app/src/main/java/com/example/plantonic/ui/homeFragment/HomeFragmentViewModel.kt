package com.example.plantonic.ui.homeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.plantonic.firebaseClasses.CategoryItem
import com.example.plantonic.firebaseClasses.PopularProductItem
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.repo.LoginRepository
import com.example.plantonic.repo.Repository

class HomeFragmentViewModel : ViewModel() {
    var repository = Repository.getInstance()
    private val loginRepository = LoginRepository()


    var allCategories = repository.categories

    private val allPopularProducts: LiveData<List<PopularProductItem>> = repository.aLlPopularProducts

    var allPopularProductItems: LiveData<ArrayList<ProductItem>> = repository.popularProductItems


    fun checkIfUserExists(userId: String?): LiveData<Boolean?> {
        loginRepository.checkIfUserExists(userId!!)
        return loginRepository.userExists
    }

    fun getUserToken (uid : String) : LiveData<String?> {
        return loginRepository.getJwtToken(uid)
    }
}