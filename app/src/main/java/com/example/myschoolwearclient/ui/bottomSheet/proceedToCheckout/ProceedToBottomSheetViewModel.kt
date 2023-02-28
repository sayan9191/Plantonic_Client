package com.example.myschoolwearclient.ui.bottomSheet.proceedToCheckout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myschoolwearclient.firebaseClasses.ProductItem
import com.example.myschoolwearclient.repo.CartRepository
import com.example.myschoolwearclient.repo.ProductDetailsRepo

class ProceedToBottomSheetViewModel : ViewModel() {
    private val productRepository = ProductDetailsRepo.getInstance()

    private val productItem = productRepository.productItem


    fun getProductDetailsFromId(id: String?): LiveData<ProductItem?>? {
        productRepository.getProductFromId(id)
        return productItem
    }


    var cartRepository = CartRepository()

    fun getAllCartItems(userId: String): Array<LiveData<*>> {
        cartRepository.getAllCartItems(userId)
        return arrayOf(cartRepository.allCartItems, cartRepository.allCartProducts)
    }
}