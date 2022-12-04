package com.example.plantonic.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.UserItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    MutableLiveData<UserItem> _currentUser = new MutableLiveData<>();
    public LiveData<UserItem> currentUser = _currentUser;


    public void getUser(String userId){
        db.collection("users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {

                        if (snapshot != null && snapshot.exists()) {
                            _currentUser.postValue(snapshot.toObject(UserItem.class));
                        }
                    }
                });
    }


    public void updateUser(UserItem userItem){
        db.collection("users")
                .document(userItem.getUserId())
                .set(userItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            _currentUser.postValue(userItem);
                        }
                    }
                });
    }

}
