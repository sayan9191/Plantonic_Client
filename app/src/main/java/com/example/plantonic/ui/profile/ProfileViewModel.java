package com.example.plantonic.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.UserItem;
import com.example.plantonic.repo.ProfileRepository;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository = new ProfileRepository();

    public LiveData<UserItem> getUser(String userId){

        profileRepository.getUser(userId);
        return profileRepository.currentUser;
    }
}
