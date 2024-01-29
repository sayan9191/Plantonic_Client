package co.`in`.plantonic.ui.bottomSheet.proceedToCheckout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.`in`.plantonic.firebaseClasses.ProductItem
import co.`in`.plantonic.repo.CartRepository
import co.`in`.plantonic.repo.ProductDetailsRepo

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