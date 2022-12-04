package com.example.plantonic.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.UserItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<Boolean> _userExists = new MutableLiveData();
    public LiveData<Boolean> userExists = _userExists;

    public void checkIfUserExists(String userId){
        db.collection("users")
                .document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        _userExists.postValue(task.getResult().exists());
                    }
                });
    }

    public LiveData<Boolean> registerNewUser(UserItem userItem){
        db.collection("users")
                .document(userItem.getUserId())
                .set(userItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        _userExists.postValue(true);
                    }
                });
        return userExists;
    }
}
