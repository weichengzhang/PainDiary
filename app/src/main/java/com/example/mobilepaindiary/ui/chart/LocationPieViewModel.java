package com.example.mobilepaindiary.ui.chart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilepaindiary.SingleLiveEvent;
import com.example.mobilepaindiary.roomdb.CustomerRepository;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/9 14:34
 */
public class LocationPieViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;

    public LiveData<List<PainRecord>> allPainRecord;
    public LiveData<List<Integer>> locationCounts;

    public LocationPieViewModel(@NonNull @NotNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
    }

    public void getAllRecord() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        allPainRecord = customerRepository.getCurrentUserAllPainRecord(currentUser.getEmail());
    }

    public void getLocationCount() {
        locationCounts = customerRepository.getLocationCounts();
    }

}
