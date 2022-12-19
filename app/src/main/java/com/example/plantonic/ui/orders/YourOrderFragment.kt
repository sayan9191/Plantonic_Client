package com.example.plantonic.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantonic.Adapter.YourOrderRecyclerViewAdapter
import com.example.plantonic.databinding.FragmentYourOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class YourOrderFragment : Fragment() {

    private lateinit var binding: FragmentYourOrderBinding
    lateinit var viewModel: OrdersViewModel
    lateinit var adapter: YourOrderRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYourOrderBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
        adapter = YourOrderRecyclerViewAdapter(requireContext())

        binding.yourOrderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.yourOrderRecyclerView.adapter = this.adapter

        viewModel.getAllOrders(userId = FirebaseAuth.getInstance().uid.toString()).observe(viewLifecycleOwner, Observer {
            adapter.updateAllOrders(it)
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
}