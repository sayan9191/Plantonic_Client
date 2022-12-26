package com.example.plantonic.ui.bottomSheet.proceedToCheckout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantonic.Adapter.ProceedToRVAdapter
import com.example.plantonic.R
import com.example.plantonic.databinding.ProceedToBottomSheetBinding
import com.example.plantonic.firebaseClasses.CartItem
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.utils.CartUtil
import com.example.plantonic.utils.FavUtil
import com.example.plantonic.utils.ProductUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class ProceedToBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : ProceedToBottomSheetBinding
    private lateinit var viewModel : ProceedToBottomSheetViewModel
    lateinit var adapter : ProceedToRVAdapter
//    lateinit var productId : String
//    lateinit var quantity : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProceedToBottomSheetBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[ProceedToBottomSheetViewModel::class.java]
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)


        adapter = ProceedToRVAdapter((requireContext()))
        binding.proceedToRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.proceedToRecyclerView.adapter = this.adapter


        // Get summary details
        // Get summary details
        val list: Array<LiveData<*>>? =
            FirebaseAuth.getInstance().uid?.let { viewModel.getAllCartItems(it) }

        val cartItems = list?.get(0) as LiveData<List<CartItem>>
        val productItems = list[1] as LiveData<List<ProductItem>>



        cartItems.observe(
            this
        ) { cartItems ->
            adapter.updateAllCartItems(cartItems)
        }

        productItems.observe(
            this
        ) { productItems ->
            adapter.updateAllCartProductItems(productItems)

        }


        binding.proceedToPaymentBtn.setOnClickListener {
            if (FavUtil.lastFragment == "product" || FavUtil.lastFragment == "cart") {
                CartUtil.lastFragment = "product"
                FavUtil.lastFragment = "cart"
            } else if (ProductUtil.lastFragment == "cart") {
                ProductUtil.lastFragment = ""
            } else {
                CartUtil.lastFragment = "home"
            }

            NavHostFragment.findNavController(this).navigate(
                R.id.cartFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.cartFragment, true).build()
            )
            this.dismiss()
        }


        return binding.root

    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}