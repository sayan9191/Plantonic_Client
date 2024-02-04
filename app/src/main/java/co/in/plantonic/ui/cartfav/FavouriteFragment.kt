package co.`in`.plantonic.ui.cartfav

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import co.`in`.plantonic.Adapter.FavouriteRecyclerViewAdapter
import co.`in`.plantonic.Adapter.listeners.FavouriteListener
import co.`in`.plantonic.R

import co.`in`.plantonic.databinding.FragmentFavouriteBinding
import co.`in`.plantonic.firebaseClasses.ProductItem
import co.`in`.plantonic.ui.activity.home.HomeActivity
import co.`in`.plantonic.ui.productDetailsScreen.ProductViewFragment
import co.`in`.plantonic.utils.CartUtil
import co.`in`.plantonic.utils.FavUtil
import co.`in`.plantonic.utils.constants.IntentConstants
import com.google.firebase.auth.FirebaseAuth

class FavouriteFragment : Fragment(), FavouriteListener {
    lateinit var binding: FragmentFavouriteBinding
    lateinit var viewModel: FavouriteViewModel
    private lateinit var favouriteRecyclerViewAdapter: FavouriteRecyclerViewAdapter
    private var currentPosition = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        // Initialize viewModel
        viewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]

        //favouriteRecyclerView adapter
        favouriteRecyclerViewAdapter =
           FavouriteRecyclerViewAdapter(requireContext(), this, viewModel)
        binding.favouriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favouriteRecyclerView.adapter = favouriteRecyclerViewAdapter
        binding.noFavView.visibility = View.GONE
        binding.favProgressBar.visibility = View.VISIBLE


        // Get all fav Items
        viewModel.getAllFavItems(FirebaseAuth.getInstance().uid)
            .observe(viewLifecycleOwner) { favouriteItems ->
                favouriteRecyclerViewAdapter.updateAllFavItems(favouriteItems)
                binding.favProgressBar.visibility = View.GONE
                if (favouriteItems.size == 0) {
                    binding.noFavView.visibility = View.VISIBLE
                } else {
                    binding.noFavView.visibility = View.GONE
                }
            }
        binding.favBackBtn.setOnClickListener { requireActivity().onBackPressed() }


        viewModel.isAddedToCart.observe(viewLifecycleOwner) { it ->
            it?.let {
                if (it && currentPosition != -1){
                    favouriteRecyclerViewAdapter.notifyItemChanged(currentPosition)
                }
            }
        }

        return binding.root
    }

    override fun onGoToCartBtnClicked(productId: String) {
//        val manager = requireActivity().supportFragmentManager

//        if (Objects.equals(FavUtil.lastFragment, "cart")){
//            manager.popBackStackImmediate();
//            FavUtil.lastFragment = "";
//        }
        CartUtil.lastFragment = "fav"
        findNavController(binding.root).navigate(
            R.id.cartFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.favouriteFragment, true).build()
        )
    }

    override fun onProductClicked(productItem: ProductItem) {
        val productViewFragment = ProductViewFragment()
        val bundle = Bundle()
        bundle.putString(IntentConstants.PRODUCT_ID, productItem.productId)
        productViewFragment.arguments = bundle
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction
            .setReorderingAllowed(true)
            .addToBackStack("detailsScreenFromFav")
            .replace(R.id.fragmentContainerView, productViewFragment)
        fragmentTransaction.commit()
        FavUtil.lastFragment = "product"
    }

    override fun onAddToCartClicked(productItem: ProductItem, position: Int) {
        currentPosition = position
        viewModel.addToCart(
            FirebaseAuth.getInstance().uid,
            productItem.getProductId()
        )
    }

    override fun onStart() {
        super.onStart()
        if (FavUtil.lastFragment == "cart") {
            binding.favBackBtn.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).hideBottomNavBar()
        } else {
            binding.favBackBtn.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    //    @Override
    //    public void onPause() {
    //        super.onPause();
    //        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    //
    //    }
    //backspaced backstack
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val manager = activity!!.supportFragmentManager
                if (FavUtil.lastFragment == "cart" || CartUtil.lastFragment == "fav") {
                    manager.popBackStackImmediate()
                }
                while (manager.backStackEntryCount > 1 && FavUtil.lastFragment != "") {
                    manager.popBackStackImmediate()
                    FavUtil.lastFragment = ""
                }
                val navController: NavController =
                    findNavController(requireActivity(), R.id.fragmentContainerView)
                navController.navigate(
                    R.id.homeFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.favouriteFragment, true).build()
                )
                manager.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}