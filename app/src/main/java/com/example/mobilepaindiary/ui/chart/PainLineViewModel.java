package com.example.mobilepaindiary.ui.chart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mobilepaindiary.roomdb.CustomerRepository;
import com.example.mobilepaindiary.roomdb.PainRecord;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/9 14:35
 */
public class PainLineViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;


    public MutableLiveData<List<PainRecord>> allRecord = new MutableLiveData<>();

    public PainLineViewModel(@NonNull @NotNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
    }

    public void findByTime(Date startTime, Date endTime) {
        allRecord.postValue(customerRepository.findByTime(startTime, endTime));
    }
}
