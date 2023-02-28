package com.example.myschoolwearclient.ui.profile.editprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myschoolwearclient.firebaseClasses.UserItem;
import com.example.myschoolwearclient.repo.ProfileRepository;

public class EditProfileViewModel extends ViewModel {

    private ProfileRepository profileRepository = new ProfileRepository();

    public LiveData<UserItem> getUser(String userId){

        profileRepository.getUser(userId);
        return profileRepository.currentUser;
    }

    public void updateUser(UserItem userItem){
        profileRepository.updateUser(userItem);
    }

}
