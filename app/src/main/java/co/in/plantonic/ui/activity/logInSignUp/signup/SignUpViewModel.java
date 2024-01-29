package co.in.plantonic.ui.activity.logInSignUp.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import co.in.plantonic.firebaseClasses.UserItem;
import co.in.plantonic.repo.LoginRepository;

public class SignUpViewModel extends ViewModel {

    private LoginRepository loginRepository = new LoginRepository();

    public LiveData<Boolean> registerUser(UserItem userItem){
        return loginRepository.registerNewUser(userItem);
    }
}
