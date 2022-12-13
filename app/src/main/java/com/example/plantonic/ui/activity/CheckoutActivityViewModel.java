package com.example.plantonic.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.AddressItem;
import com.example.plantonic.repo.AddressRepository;

public class CheckoutActivityViewModel extends ViewModel {

    private AddressRepository addressRepository = new AddressRepository();

    public LiveData<AddressItem> currentAddress = addressRepository.userAddress;

    public void updateAddress(AddressItem addressItem){
        addressRepository.updateUserAddress(addressItem);
    }

    public void getAddress(String userId){
        addressRepository.getUserAddress(userId);
    }

}
