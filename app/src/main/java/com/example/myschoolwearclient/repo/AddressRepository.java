package com.example.myschoolwearclient.repo;

import static com.example.myschoolwearclient.utils.constants.DatabaseConstants.getUserAddressReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myschoolwearclient.firebaseClasses.AddressItem;
import com.google.android.gms.tasks.OnFailureListener;
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _userAddress.postValue(null);
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
                }else {
                    _userAddress.postValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
