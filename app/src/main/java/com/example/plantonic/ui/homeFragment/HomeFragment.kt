package com.example.plantonic.ui.homeFragment

import android.content.Context

import com.example.plantonic.Adapter.listeners.OnProductListener
import com.denzcoskun.imageslider.ImageSlider
import androidx.recyclerview.widget.RecyclerView

import com.example.plantonic.Adapter.CategoryAdapter
import com.example.plantonic.Adapter.PopularItemAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.plantonic.R
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager

import com.example.plantonic.ui.search.SearchFragment
import com.example.plantonic.utils.HomeUtil
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment
import com.example.plantonic.utils.constants.IntentConstants
import com.example.plantonic.utils.CartUtil
import com.example.plantonic.utils.FavUtil
import com.example.plantonic.ui.activity.HomeActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import java.util.ArrayList

class HomeFragment : Fragment(), OnProductListener {
    private lateinit var imageSlider: ImageSlider
    lateinit var recyclerView1: RecyclerView
    lateinit var recyclerView2: RecyclerView
    lateinit var searchBtn: ImageView
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var popularItemAdapter: PopularItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        imageSlider = view.findViewById(R.id.imageSlider)
        recyclerView1 = view.findViewById(R.id.recyclerView1)
        recyclerView2 = view.findViewById(R.id.searchResultRecyclerView)
        searchBtn = view.findViewById(R.id.searchBtn)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)


        //Slider of offers
        val slideModels = ArrayList<SlideModel>()
        slideModels.add(
            SlideModel(
                "https://cdn.pixabay.com/photo/2014/02/27/16/10/flowers-276014__480.jpg",
                ScaleTypes.FIT
            )
        )
        slideModels.add(
            SlideModel(
                "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__480.jpg",
                ScaleTypes.FIT
            )
        )
        slideModels.add(
            SlideModel(
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg",
                ScaleTypes.FIT
            )
        )
        slideModels.add(
            SlideModel(
                "https://cdn.pixabay.com/photo/2014/04/14/20/11/pink-324175__480.jpg",
                ScaleTypes.FIT
            )
        )
        imageSlider.setImageList(slideModels, ScaleTypes.FIT)

        //Adapters
        categoryAdapter = CategoryAdapter(this.context)
        popularItemAdapter = PopularItemAdapter(this.context, this)
        recyclerView1.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView1.adapter = categoryAdapter
        recyclerView2.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView2.adapter = popularItemAdapter
        viewModel.allCategories.observe(viewLifecycleOwner) { categoryItems ->
            categoryAdapter.updateCategories(categoryItems)
            Log.d("-----------", categoryItems[0].categoryName)
        }

        //Search Button of products
        searchBtn.setOnClickListener(View.OnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("searchFragment")
                .setReorderingAllowed(true).replace(R.id.fragmentContainerView, SearchFragment())
            fragmentTransaction.commit()
            HomeUtil.lastFragment = "search"
        })
        viewModel.allPopularProductItems.observe(this.viewLifecycleOwner) { popularProductItems ->
            popularItemAdapter.updatePopularProducts(
                popularProductItems
            )
        }
        return view
    }

    //Click on products
    override fun onProductClick(productItem: ProductItem) {
        val productViewFragment = ProductViewFragment()
        val bundle = Bundle()
        bundle.putString(IntentConstants.PRODUCT_ID, productItem.productId)
        productViewFragment.arguments = bundle
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction
            .setReorderingAllowed(true)
            .addToBackStack("detailsScreen")
            .replace(R.id.fragmentContainerView, productViewFragment)
        fragmentTransaction.commit()
        HomeUtil.lastFragment = "product"
    }

    override fun onResume() {
        super.onResume()
        HomeUtil.lastFragment = ""
        CartUtil.lastFragment = ""
        FavUtil.lastFragment = ""
        (requireActivity() as HomeActivity).showBottomNavBar()
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
}