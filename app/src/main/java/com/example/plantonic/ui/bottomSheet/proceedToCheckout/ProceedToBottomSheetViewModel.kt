package com.example.plantonic.ui.bottomSheet.proceedToCheckout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.repo.CartRepository
import com.example.plantonic.repo.ProductDetailsRepo

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