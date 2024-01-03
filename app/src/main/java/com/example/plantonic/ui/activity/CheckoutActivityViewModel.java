package com.example.plantonic.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.AddressItem;
import com.example.plantonic.repo.AddressRepository;
import com.example.plantonic.repo.BlueDartRepository;
import com.example.plantonic.retrofit.models.pincode.PinCodeAvailableResponseModel;

public class CheckoutActivityViewModel extends ViewModel {
    private AddressRepository addressRepository = new AddressRepository();

    public LiveData<AddressItem> currentAddress = addressRepository.userAddress;

    public void updateAddress(AddressItem addressItem){
        addressRepository.updateUserAddress(addressItem);
    }

    public void getAddress(String userId){
        addressRepository.getUserAddress(userId);
    }

    private  BlueDartRepository repo = new BlueDartRepository();
    public LiveData<Boolean> isLoading = repo.isLoading();
    public LiveData<String> errorMessage = repo.getErrorMessage();

    public LiveData<PinCodeAvailableResponseModel> isPinCodeAvailable = repo.isPinCodeAvailable();

    public void checkIfPinCodeAvailable(String pincode){
        repo.checkIsPinCodeAvailable(pincode);
    }
    
    
}
