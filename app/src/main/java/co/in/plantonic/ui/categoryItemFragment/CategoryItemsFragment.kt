package co.`in`.plantonic.ui.categoryItemFragment

import android.content.Context
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
import co.`in`.plantonic.Adapter.PopularItemAdapter
import co.`in`.plantonic.Adapter.listeners.OnProductListener
import co.`in`.plantonic.R
import co.`in`.plantonic.databinding.FragmentCategoryItemsBinding
import co.`in`.plantonic.firebaseClasses.ProductItem
import co.`in`.plantonic.ui.productDetailsScreen.ProductViewFragment
import co.`in`.plantonic.utils.constants.IntentConstants

class CategoryItemsFragment : Fragment(), OnProductListener {
    private lateinit var binding :  FragmentCategoryItemsBinding
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
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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


    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

//    override fun onPause() {
//        super.onPause()
//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
//    }

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