package com.example.myschoolwearclient.ui.categoryItemFragment

import android.content.Context
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
import com.example.myschoolwearclient.Adapter.PopularItemAdapter
import com.example.myschoolwearclient.Adapter.listeners.OnProductListener
import com.example.myschoolwearclient.firebaseClasses.ProductItem
import com.example.myschoolwearclient.ui.productDetailsScreen.ProductViewFragment
import com.example.myschoolwearclient.utils.constants.IntentConstants
import com.example.plantonic.R
import com.example.plantonic.databinding.FragmentCategoryItemsBinding

class CategoryItemsFragment : Fragment(), OnProductListener {
    private lateinit var binding : FragmentCategoryItemsBinding
    private lateinit var viewModel : CategoryItemsFragmentViewModel
    private lateinit var categoryId : String
    private lateinit var adapter: PopularItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryItemsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[CategoryItemsFragmentViewModel::class.java]
        categoryId = arguments?.getString(IntentConstants.CATEGORY_ID).toString()
        adapter = PopularItemAdapter(requireContext(), this)

        //Initializing recyclerView
        binding.categoryItemsRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        //set adapter
        binding.categoryItemsRecyclerView.adapter = this.adapter
        viewModel.getCategoriesItems(categoryId)

        viewModel.allCategories.observe(viewLifecycleOwner, Observer {
            Log.d("categoryItems", it.toString())
            adapter.updatePopularProducts(ArrayList(it))
        })


        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })


        viewModel.status.observe(viewLifecycleOwner, Observer {
            binding.statusTextView.text = it
        })


        return binding.root
    }


    //backspaced backstack
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val manager = activity!!.supportFragmentManager
                manager.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onProductClick(productItem: ProductItem?) {
        val productViewFragment = ProductViewFragment()
        val bundle = Bundle()
        bundle.putString(IntentConstants.PRODUCT_ID, productItem?.productId)
        productViewFragment.arguments = bundle
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction
            .setReorderingAllowed(true)
            .addToBackStack("detailsScreen")
            .replace(R.id.fragmentContainerView, productViewFragment)
        fragmentTransaction.commit()
    }
}