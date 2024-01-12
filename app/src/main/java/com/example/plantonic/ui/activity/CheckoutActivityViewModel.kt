package com.example.plantonic.ui.activity

import androidx.lifecycle.ViewModel
import com.example.plantonic.repo.AddressRepository
import androidx.lifecycle.LiveData
import com.example.plantonic.firebaseClasses.AddressItem
import com.example.plantonic.repo.BlueDartRepository

class CheckoutActivityViewModel : ViewModel() {
    private val addressRepository = AddressRepository()
    var currentAddress: LiveData<AddressItem> = addressRepository.userAddress
    fun updateAddress(addressItem: AddressItem?) {
        addressRepository.updateUserAddress(addressItem)
    }

    fun getAddress(userId: String?) {
        addressRepository.getUserAddress(userId)
    }

    private val repo = BlueDartRepository()
    val isLoading: LiveData<Boolean> = repo.isLoading
    val errorMessage: LiveData<String> = repo.errorMessage
    val isPinCodeAvailable = repo.isPinCodeAvailable
    fun checkIfPinCodeAvailable(pincode: String?) {
        repo.checkIsPinCodeAvailable(pincode!!)
    }
}