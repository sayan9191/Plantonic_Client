package co.`in`.plantonic.ui.orders

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.`in`.plantonic.Adapter.YourOrderRecyclerViewAdapter
import co.`in`.plantonic.Adapter.listeners.TrackOrderListener
import co.`in`.plantonic.R
import co.`in`.plantonic.databinding.FragmentYourOrderBinding
import co.`in`.plantonic.retrofit.models.order.Data
import co.`in`.plantonic.ui.activity.home.HomeActivity
import co.`in`.plantonic.ui.bottomSheet.BottomSheetTrackStatus
import co.`in`.plantonic.ui.dialogbox.LoadingScreen
import co.`in`.plantonic.ui.productDetailsScreen.ProductViewFragment
import co.`in`.plantonic.ui.profile.ProfileFragment
import co.`in`.plantonic.utils.OrdersUtil
import co.`in`.plantonic.utils.ProductUtil
import co.`in`.plantonic.utils.constants.IntentConstants

class YourOrderFragment : Fragment(), TrackOrderListener {

    private lateinit var binding: FragmentYourOrderBinding
    lateinit var viewModel: OrdersViewModel
    lateinit var adapter: YourOrderRecyclerViewAdapter
    private var pageNo = 1
    private var isBottomSheetOpened : Boolean = false
    lateinit var currentOrderItem : Data
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYourOrderBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
        adapter = YourOrderRecyclerViewAdapter(requireContext(), this)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.yourOrderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.yourOrderRecyclerView.adapter = this.adapter

//        viewModel.getAllOrders(userId = FirebaseAuth.getInstance().uid.toString()).observe(viewLifecycleOwner, Observer {
//            adapter.updateAllOrders(it)
//        })

        viewModel.getMyOrders(pageNo)
        viewModel.myOrders.observe(viewLifecycleOwner) {
            it?.let {
                adapter.updateAllOrders(it.data)

                // handle pagination
                binding.yourOrderRecyclerView.addOnScrollListener(object: PaginationScrollListener(binding.yourOrderRecyclerView.layoutManager as LinearLayoutManager){
                    override fun loadMoreItems() {
                        if (it.next_page != null){
                            pageNo = it.next_page
                            viewModel.getMyOrders(it.next_page)
                        }
                    }

                    override fun isLastPage(): Boolean {
                        return it.current_page == it.total_page
                    }

                    override fun isLoading(): Boolean {
                        if (viewModel.isLoading.value != null){
                            return viewModel.isLoading.value == true
                        }
                        return false
                    }

                })
            }
        }



        binding.btnBack.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction
                .setReorderingAllowed(true).addToBackStack("Your Order")
                .replace(R.id.fragmentContainerView, ProfileFragment())
            fragmentTransaction.commit()}

        viewModel.errorMessage.observe(viewLifecycleOwner) { s ->
            if (s != "") {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
                if (pageNo == 1 && s == "No recent orders found"){
                    binding.noDeliveryItemTxtView.visibility = View.VISIBLE
                }else{
                    binding.noDeliveryItemTxtView.visibility = View.GONE
                }
            }
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                LoadingScreen.showLoadingDialog(requireContext())
            } else {
                try {
                    LoadingScreen.hideLoadingDialog()
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }

        viewModel.trackOrderLiveData.observe(viewLifecycleOwner){
            it?.let { response ->
                val bottomSheet = BottomSheetTrackStatus(response, currentOrderItem)
                if (!bottomSheet.isVisible && !isBottomSheetOpened){
                    bottomSheet.show(parentFragmentManager, "track_order")
                    isBottomSheetOpened = true
                }

            }
        }


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


    override fun onTrackOrderClicked(orderItem: Data) {
        currentOrderItem = orderItem
        isBottomSheetOpened = false
        viewModel.trackOrder(orderItem.order_id)
    }

    override fun onOrderedProductClicked(productId: String) {
        navigateToProductFragment(productId)
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
        ProductUtil.lastFragment = "orders"
        OrdersUtil.lastFragment = "product"
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).hideBottomNavBar()
        if (OrdersUtil.lastFragment == "product") {
            OrdersUtil.lastFragment = ""
        }
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }



//    override fun onPause() {
//        super.onPause()
//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
//    }

//    override fun onStart() {
//        super.onStart()
//        (requireActivity() as HomeActivity).showBottomNavBar()
//    }

}