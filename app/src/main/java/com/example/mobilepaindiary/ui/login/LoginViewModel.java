package com.example.mobilepaindiary.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mobilepaindiary.data.LoginRepository;
import com.google.firebase.auth.FirebaseUser;

import static com.example.mobilepaindiary.data.LoginRepository.LOGIN;
import static com.example.mobilepaindiary.data.LoginRepository.LOGIN_ERROR;

public class LoginViewModel extends ViewModel {

//    private MutableLiveData<String> loginResult;
    private MutableLiveData<FirebaseUser> userLiveData;
    private LoginRepository loginRepository;

    public LoginViewModel() {
        this.loginRepository = LoginRepository.getInstance();
//        loginResult = new MutableLiveData<>();
        userLiveData = loginRepository.getUserLiveData();
    }


//    LiveData<String> getLoginResult() {
//        return loginResult;
//    }

    public void login(String email, String password) {
        // can be launched in a separate asynchronous job
        // Firebase can be a time consumed action
//        String result = loginRepository.login(email, password);
//        loginResult.setValue(result);

        try {
            loginRepository.login(email, password);
        } catch (Exception e) {
            Log.e(LOGIN, LOGIN_ERROR);
        }
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

}