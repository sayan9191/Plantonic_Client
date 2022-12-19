package com.example.plantonic.ui.bottomSheet.proceedToCheckout

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.plantonic.R
import com.example.plantonic.databinding.ProceedToBottomSheetBinding
import com.example.plantonic.utils.constants.IntentConstants
import kotlin.properties.Delegates

class ProceedToBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding : ProceedToBottomSheetBinding
    private lateinit var viewModel : ProceedToBottomSheetViewModel
    lateinit var productId : String
    lateinit var quantity : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProceedToBottomSheetBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[ProceedToBottomSheetViewModel::class.java]
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        productId = arguments?.getString(IntentConstants.PRODUCT_ID).toString()
        quantity = arguments?.getString(IntentConstants.PRODUCT_QUANTITY).toString()

        binding.proceedToProductItem.summaryProductDeliveryAmount.visibility = View.GONE
        binding.proceedToProductItem.summaryOrderProductOffer.visibility = View.GONE


        viewModel.getProductDetailsFromId(productId)?.observe(this, Observer {
            Glide.with(requireContext()).load(it?.imageUrl1).into(binding.proceedToProductItem.summaryOrderProductImage)
            binding.proceedToProductItem.summaryOrderProductName.text = it?.productName
            binding.proceedToProductItem.summaryOrderQuantity.text = quantity
            binding.proceedToProductItem.summaryOrderProductPrice.text = it?.listedPrice
            binding.proceedToProductItem.summaryOrderActualPrice.text = it?.actualPrice
        })


        return binding.root

    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}