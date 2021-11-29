package com.example.mobilepaindiary.data;

import android.util.Log;
import android.widget.Toast;

import com.example.mobilepaindiary.MyApplication;
import com.example.mobilepaindiary.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import static com.example.mobilepaindiary.MyApplication.getContext;


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
//    private SharedPreferenceUtil sp;
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;
    private final FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> userLiveData;


    private final String TAG = "Author Process: ";


    // private constructor : singleton access
    private LoginRepository() {
//        sp = SharedPreferenceUtil.getInstance(MyApplication.getContext());
//        sp.putString("123@163.com","123456");
        this.mAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }
//
//    public void logout() {
//        user = null;
//    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser getLoggedInUser() {
        return user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

//    public String login(String username, String password) {
//        // handle login
//        LoggedInUser tempUser = loginProcess(username, password);
//        if (!tempUser.getUserId().isEmpty()) {
//            setLoggedInUser(tempUser);
//            return tempUser.getDisplayName();
//        }
//        return LOGIN_ERROR;
//    }

    // Perform API call in the firebase login request
//    public String loginProcess(String email, String password) {
//        // handle loggedInUser authentication
//        LoggedInUser tempUser = new LoggedInUser("", "");
//        try {
//            if (email != null && password != null) {
////                if (password.equals(sp.getString(username))){
////                    tempUser = new LoggedInUser(
////                            java.util.UUID.randomUUID().toString(),
////                            username);
////                }
//            }
//
//        } catch (Exception e) {
//            Log.e(LOGIN, LOGIN_ERROR);
//            return LOGIN_ERROR;
//        }
//        return tempUser.getDisplayName();
//    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {// delete `this`, first param
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(mAuth.getCurrentUser());
                            setLoggedInUser(new LoggedInUser(
                                    mAuth.getCurrentUser().getUid(),
                                    mAuth.getCurrentUser().getEmail()));

                        } else {
                            Toast.makeText(getContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(getContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logOut() {
        mAuth.signOut();
        user = null;
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public static String LOGIN = "Login: ";

    public static String LOGIN_ERROR = "Error logging in";
}