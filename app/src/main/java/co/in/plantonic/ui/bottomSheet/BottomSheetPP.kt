package co.`in`.plantonic.ui.bottomSheet

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import co.`in`.plantonic.R
import co.`in`.plantonic.databinding.PrivatepolicyXmlBinding
import co.`in`.plantonic.utils.constants.UrlConstants

class BottomSheetPP : BottomSheetDialogFragment() {
    lateinit var binding: PrivatepolicyXmlBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PrivatepolicyXmlBinding.inflate(layoutInflater, container, false)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        binding.ppWebView.loadUrl(UrlConstants.BaseURL + "privacy-policy")
        binding.ppWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.ppProgressBar.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}