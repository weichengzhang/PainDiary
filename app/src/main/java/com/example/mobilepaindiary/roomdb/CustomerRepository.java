package com.example.mobilepaindiary.roomdb;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobilepaindiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CustomerRepository {
    private CustomerDAO customerDao;
    private PainRecordDAO painRecordDAO;
    private LiveData<List<Customer>> allCustomers;

    private String[] locations;

    public CustomerRepository(Application application) {
        CustomerDatabase db = CustomerDatabase.getInstance(application);
        customerDao = db.customerDao();
        painRecordDAO = db.painRecord();
        allCustomers = customerDao.getAll();

        locations = application.getResources().getStringArray(R.array.location);
    }


    public long[] insertPainRecord(PainRecord... painRecord) {
        return painRecordDAO.insert(painRecord);
    }

    public int updatePainRecord(PainRecord... painRecord) {
        return painRecordDAO.update(painRecord);
    }


    public LiveData<List<PainRecord>> getCurrentUserAllPainRecord(String userMail) {
        return painRecordDAO.getCurrentUserAllPainRecord(userMail);
    }

    public LiveData<PainRecord> getTodayPainRecord(Date date, String userMail) {
        return painRecordDAO.findByTimeAndUserMail(date, userMail);
    }

    public LiveData<List<Integer>> getLocationCounts() {
        List<Integer> list = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        for (int i = 0; i < locations.length; i++) {
            int locationCount = painRecordDAO.findLocationCount(String.valueOf(i), currentUser.getEmail());
            list.add(locationCount);
        }
        return new MutableLiveData<>(list);
    }

    public List<PainRecord> findByTime(Date startTime, Date endTime) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return painRecordDAO.findByTime(startTime, endTime).stream().filter(painRecord -> painRecord.userMail.equals(currentUser.getEmail())).collect(Collectors.toList());
    }


    // Room executes this query on a separate thread
    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public void insert(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.insert(customer);
            }
        });
    }

    public void deleteAll() {
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.deleteAll();
            }
        });
    }

    public Customer getLatestCustomer() {
        return customerDao.getLatestCustomer();
    }

    public void delete(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.delete(customer);
            }
        });
    }

    public void updateCustomer(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.updateCustomer(customer);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Customer> findByIDFuture(final int customerId) {

        return CompletableFuture.supplyAsync(new Supplier<Customer>() {
            @Override
            public Customer get() {
                return customerDao.findByID(customerId);
            }
        }, CustomerDatabase.databaseWriteExecutor);
    }
}
