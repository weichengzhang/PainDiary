package com.example.mobilepaindiary.ui.chart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilepaindiary.roomdb.CustomerRepository;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/9 14:35
 */
public class StepsPieViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;
    public LiveData<PainRecord> todayPainRecord;

    public StepsPieViewModel(@NonNull @NotNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
    }

    public void getTodayPainRecord() {
        FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
        todayPainRecord = customerRepository.getTodayPainRecord(new Date(), currentUser.getEmail());
    }

}
