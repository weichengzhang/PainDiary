package com.example.mobilepaindiary.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepaindiary.MainActivity;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.ActicityLogupBinding;
import com.example.mobilepaindiary.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogupActivity extends AppCompatActivity {
    private ActicityLogupBinding activityLogupBinding;

    private FirebaseAuth auth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogupBinding = ActicityLogupBinding.inflate(getLayoutInflater());
        View view = activityLogupBinding.getRoot();
        auth = FirebaseAuth.getInstance();
        setContentView(view);
        activityLogupBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogupActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            } });
        activityLogupBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = activityLogupBinding.username.getText().toString();
                String password_txt = activityLogupBinding.password.getText().toString();
                if (!isUserNameValid(email_txt)) {
                    activityLogupBinding.username.setError(getString(R.string.invalid_username));}
                else {
                    auth.createUserWithEmailAndPassword(email_txt, password_txt).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // do something, e.g. start the other activity
                                        finish();
                                    } else {
                                        //do something, e.g. give a message
                                        Toast.makeText(getApplicationContext(), getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }


    private boolean isUserNameValid (String username){

        // check upper case username.equals(username.toLowerCase())
        if (username != null && username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false;
        }
    }

    private boolean isPasswordValid (String password){
        return password != null && password.trim().length() > 5;
    }
}






