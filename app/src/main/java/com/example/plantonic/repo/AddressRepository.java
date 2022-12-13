package com.example.plantonic.repo;

import static com.example.plantonic.utils.constants.DatabaseConstants.getUserAddressReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.AddressItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AddressRepository {

    private MutableLiveData<AddressItem> _userAddress = new MutableLiveData<AddressItem>();
    public LiveData<AddressItem> userAddress = _userAddress;

    public void updateUserAddress(AddressItem addressItem) {
        getUserAddressReference(addressItem.getUserId()).setValue(addressItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        _userAddress.postValue(addressItem);
                    }
                });
    }

    public void getUserAddress(String userId) {
        getUserAddressReference(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AddressItem address = snapshot.getValue(AddressItem.class);
                    if (address != null){
                        _userAddress.postValue(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
