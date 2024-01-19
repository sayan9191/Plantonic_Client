package com.example.plantonic.ui.bottomSheet

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.plantonic.utils.constants.UrlConstants
import android.webkit.WebViewClient
import android.webkit.WebView
import com.example.plantonic.R
import com.example.plantonic.databinding.TermsconditionXmlBinding
import com.example.plantonic.ui.custom.ObservableWebView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class TermsAndConditionBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: TermsconditionXmlBinding
    private var mCurrentWebViewScrollY = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TermsconditionXmlBinding.inflate(layoutInflater)

        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

//        setupBottomSheetBehaviour()
        setupWebView()

        binding.tAndCWebView.loadUrl(UrlConstants.BaseURL + "terms-and-condition")
        binding.tAndCWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.tncProgressBar.visibility = View.GONE
            }
        }
        return binding.root
    }



//    private fun setupBottomSheetBehaviour() {
//        binding.tncBottomSheet.let { view ->
//            BottomSheetBehavior.from(view).let { behaviour ->
//                behaviour.addBottomSheetCallback(object :
//                    BottomSheetBehavior.BottomSheetCallback() {
//                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//                    }
//
//                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        if (mCurrentWebViewScrollY > 0) {
//                            // this is where we check if webView can scroll up or not and based on that we let BottomSheet close on scroll down
//                            behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                        } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                            dismiss()
//                        }
//                    }
//                })
//            }
//        }
//    }

    private fun setupWebView() {
        binding.tAndCWebView.onScrollChangedCallback = object : ObservableWebView.OnScrollChangeListener {
            override fun onScrollChanged(currentHorizontalScroll: Int, currentVerticalScroll: Int,
                                         oldHorizontalScroll: Int, oldCurrentVerticalScroll: Int) {
                mCurrentWebViewScrollY = currentVerticalScroll

                Log.d("------------------", mCurrentWebViewScrollY.toString())
            }
        }


        }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}