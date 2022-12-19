package com.example.plantonic.ui.bottomSheet.proceedToCheckout

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        productId = arguments?.getString(IntentConstants.PRODUCT_ID).toString()
        quantity = arguments?.getString(IntentConstants.PRODUCT_QUANTITY).toString()


        viewModel.getProductDetailsFromId(productId)?.observe(this, Observer {
            TODO("Update UI")
        })


        return binding.root
    }
}