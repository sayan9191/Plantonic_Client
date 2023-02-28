package com.example.myschoolwearclient.ui.categoryItemFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myschoolwearclient.firebaseClasses.ProductItem
import com.example.myschoolwearclient.repo.CategoriesRepository

class CategoryItemsFragmentViewModel : ViewModel() {
    private val categoriesRepository : CategoriesRepository = CategoriesRepository()
    val allCategories : LiveData<List<ProductItem>> = categoriesRepository.categoriesItems
    val isLoading : LiveData<Boolean> = categoriesRepository.isLoading
    val status : LiveData<String> = categoriesRepository.status

    fun getCategoriesItems (categoryId : String) {
        categoriesRepository.getCategoryProductItems(categoryId)
    }
}