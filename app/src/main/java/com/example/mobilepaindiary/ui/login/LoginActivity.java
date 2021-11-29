package com.example.mobilepaindiary.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.ActivityLoginBinding;
import com.example.mobilepaindiary.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding activityLoginBinding;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        activityLoginBinding.logupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        LogupActivity.class);
                startActivity(intent);
            } });


        loginViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.welcome) + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

                    setResult(Activity.RESULT_OK);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //Complete and destroy login activity once successful
                    finish();                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUserNameValid(activityLoginBinding.username.getText().toString())){
                    activityLoginBinding.username.setError(getString(R.string.invalid_username));
                    activityLoginBinding.loginButton.setEnabled(false);}
                else{
                    activityLoginBinding.loginButton.setEnabled(true);
                }
            }
        };
        activityLoginBinding.username.addTextChangedListener(afterTextChangedListener);

        activityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(
                        activityLoginBinding.username.getText().toString(),
                        activityLoginBinding.password.getText().toString());
            }
        });
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {

        // check upper case username.equals(username.toLowerCase())
        if (username != null && username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false;
        }
    }


    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}