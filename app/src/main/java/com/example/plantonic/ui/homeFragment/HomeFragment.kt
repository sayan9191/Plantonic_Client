package com.example.plantonic.ui.homeFragment

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.plantonic.Adapter.CategoryAdapter
import com.example.plantonic.Adapter.PopularItemAdapter
import com.example.plantonic.Adapter.listeners.CategoryListener
import com.example.plantonic.Adapter.listeners.OnProductListener
import com.example.plantonic.R
import com.example.plantonic.databinding.FragmentHomeBinding
import com.example.plantonic.firebaseClasses.CategoryItem
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.retrofit.models.home.HomePageBannerResponseModel
import com.example.plantonic.ui.activity.home.HomeActivity
import com.example.plantonic.ui.activity.logInSignUp.login.LoginActivity
import com.example.plantonic.ui.categoryItemFragment.CategoryItemsFragment
import com.example.plantonic.ui.dialogbox.LoadingScreen
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment
import com.example.plantonic.ui.search.SearchFragment
import com.example.plantonic.utils.CartUtil
import com.example.plantonic.utils.FavUtil
import com.example.plantonic.utils.HomeUtil
import com.example.plantonic.utils.StorageUtil
import com.example.plantonic.utils.constants.IntentConstants
import com.example.plantonic.utils.crypto.EncryptUtil
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), OnProductListener, CategoryListener {
    lateinit var binding : FragmentHomeBinding

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var popularItemAdapter: PopularItemAdapter

    private val localStorage = StorageUtil.getInstance()

    var isDeeplinkNavigated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]

        // Initialize storage
        localStorage.sharedPref =
            requireContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        //Slider of offers
        val slideModels = ArrayList<SlideModel>()

        viewModel.getAllBanners?.let { liveData ->
            liveData.observe(viewLifecycleOwner
                ) { banners ->
                    banners.data.forEach {
                        slideModels.add(
                            SlideModel(it.image_link, ScaleTypes.FIT)
                        )
                    }
                binding.imageSlider.setImageList(slideModels, ScaleTypes.FIT)
                }
            }



            //Adapters
            categoryAdapter = CategoryAdapter(this.context, this)
            popularItemAdapter = PopularItemAdapter(this.context, this)
            binding.recyclerView1.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerView1.adapter = categoryAdapter
            binding.searchResultRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
            binding.searchResultRecyclerView.adapter = popularItemAdapter


            //Search Button of products
            binding.searchBtn.setOnClickListener(View.OnClickListener {
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack("searchFragment")
                    .setReorderingAllowed(true).replace(R.id.fragmentContainerView, SearchFragment())
                fragmentTransaction.commit()
                HomeUtil.lastFragment = "search"
            })

            // Observe Categories
            viewModel.allCategories?.observe(viewLifecycleOwner) { categoryItems ->
                categoryAdapter.updateCategories(categoryItems.sortedBy { it.categoryId.toInt() })
                Log.d("-----------", categoryItems[0].categoryName)
            }

            // Observe Popular items
            viewModel.allPopularProductItems?.observe(this.viewLifecycleOwner) { popularProductItems ->
                popularItemAdapter.updatePopularProducts(popularProductItems)
                if (popularProductItems.size > 0){
                    binding.popularItemProgressBar.visibility = View.GONE
                }
            }

            return binding.root
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get product id from deep link
        try {
            if (!isDeeplinkNavigated){
                arguments?.getString("id")?.let {
                    checkLogin(it)
                }
            }
        } catch (e : Exception){
            e.stackTrace
        }
    }


    //Click on products
    override fun onProductClick(productItem: ProductItem) {
        navigateToProductFragment(productItem.getProductId())
    }

    private fun navigateToProductFragment(productId : String){
        val productViewFragment = ProductViewFragment()
        val bundle = Bundle()
        bundle.putString(IntentConstants.PRODUCT_ID, productId)
        productViewFragment.arguments = bundle
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction
            .setReorderingAllowed(true)
            .addToBackStack("detailsScreen")
            .replace(R.id.fragmentContainerView, productViewFragment)
        fragmentTransaction.commit()
        HomeUtil.lastFragment = "product"
    }

    // On category item clicked
    override fun onCategoryItemClicked(categoryItem: CategoryItem) {
        val categoryFragment = CategoryItemsFragment()
        val bundle : Bundle = Bundle()
        bundle.putString(IntentConstants.CATEGORY_ID, categoryItem.categoryId)
        categoryFragment.arguments = bundle

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction
            .setReorderingAllowed(true)
            .addToBackStack("categoryItemScreen")
            .replace(R.id.fragmentContainerView, categoryFragment)
        fragmentTransaction.commit()
        HomeUtil.lastFragment = "category"
    }

    override fun onResume() {
        super.onResume()
        HomeUtil.lastFragment = ""
        CartUtil.lastFragment = ""
        FavUtil.lastFragment = ""
        (requireActivity() as HomeActivity).showBottomNavBar()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    //backspaced backstack
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val manager = activity!!.supportFragmentManager
                if (CartUtil.lastFragment == "" || FavUtil.lastFragment == "" || HomeUtil.lastFragment == "") {
                    requireActivity().finish()
                }
                manager.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun checkLogin(productId: String) {
        // SHow loading dialog box
        LoadingScreen.showLoadingDialog(requireContext())

        // check if user is logged in or not
        if (FirebaseAuth.getInstance().currentUser != null) {
            viewModel.checkIfUserExists(FirebaseAuth.getInstance().uid).observe(viewLifecycleOwner, Observer { userExists ->
                // if user exists in firebase
                if (userExists != null && userExists) {

                    // check if user exists in server and get jwt token
                    FirebaseAuth.getInstance().currentUser?.uid?.let {
                        viewModel.getUserToken(it).observe(viewLifecycleOwner) { token ->

                            if (token != null) {
                                // save token to local storage
                                localStorage.token = token

                                // Stop the loader
                                try {
                                    LoadingScreen.hideLoadingDialog()
                                }catch (e : Exception){
                                    e.stackTrace
                                }

                                // navigate to product page
                                navigateToProductFragment(EncryptUtil.decrypt(productId))
                                isDeeplinkNavigated = true

                            }else{
                                navigateToLoginScreen()
                            }
                        }
                    }
                } else {
                    navigateToLoginScreen()
                }
            })
        } else {
            navigateToLoginScreen()
        }
    }

    private fun navigateToLoginScreen () {
        try {
            LoadingScreen.hideLoadingDialog()
        }catch (e : Exception){
            e.stackTrace
        }

        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }


//    override fun onPause() {
//        super.onPause()
//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
//    }
}