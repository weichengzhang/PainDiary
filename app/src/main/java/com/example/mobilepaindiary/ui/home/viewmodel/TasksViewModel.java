package com.example.mobilepaindiary.ui.home.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilepaindiary.SingleLiveEvent;
import com.example.mobilepaindiary.data.model.weathermodel.Main;
import com.example.mobilepaindiary.data.model.weathermodel.WeatherResponse;
import com.example.mobilepaindiary.retrofit.RetrofitClient;
import com.example.mobilepaindiary.retrofit.RetrofitInterface;
import com.example.mobilepaindiary.roomdb.CustomerRepository;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksViewModel extends AndroidViewModel {

    private static final String TAG = "TasksViewModel";

    private RetrofitInterface retrofitInterface;

    private CustomerRepository customerRepository;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public SingleLiveEvent<WeatherResponse> weatherLiveDate = new SingleLiveEvent<>();

    public SingleLiveEvent<Integer> updatePainRecordLiveDate = new SingleLiveEvent<>();

    public SingleLiveEvent<long[]> insertPainRecordLiveDate = new SingleLiveEvent<>();

    public LiveData<List<PainRecord>> allPainRecord;
    public LiveData<PainRecord> todayPainRecord;

    public TasksViewModel(@NonNull @NotNull Application application) {
        super(application);
        retrofitInterface = RetrofitClient.getRetrofitService();
        customerRepository = new CustomerRepository(application);
    }

    public void getCurrentWeather() {
        Call<WeatherResponse> callAsync =
                retrofitInterface.getWeatherByCity("guangzhou,China");
        //makes an async request & invokes callback methods when the response returns
        callAsync.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NotNull Call<WeatherResponse> call,
                                   @NotNull Response<WeatherResponse> response) {

                Log.e(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
                    weatherLiveDate.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });
    }

    public void insertPainRecord(PainRecord... painRecord) {
        new Thread(() -> {
            Call<WeatherResponse> callAsync =
                    retrofitInterface.getWeatherByCity("guangzhou,China");
            try {
                Response<WeatherResponse> execute = callAsync.execute();
                if (execute.isSuccessful()) {
                    PainRecord data = painRecord[0];
                    assert execute.body() != null;
                    Main main = execute.body().main;
                    float temp = main.temp;
                    float humidity = main.humidity;
                    float pressure = main.pressure;
                    data.currentHumidity = String.valueOf(humidity);
                    data.currentPressure = String.valueOf(pressure);
                    data.currentTemperature = String.valueOf(temp);
                    insertPainRecordLiveDate.postValue(customerRepository.insertPainRecord(data));
                } else {
                    insertPainRecordLiveDate.postValue(new long[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
                insertPainRecordLiveDate.postValue(new long[0]);
            }
        }).start();

    }

    public void getCurrentUserAllPainRecord() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        allPainRecord = customerRepository.getCurrentUserAllPainRecord(currentUser.getEmail());
    }

    public void getTodayPainRecord() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        todayPainRecord = customerRepository.getTodayPainRecord(new Date(), currentUser.getEmail());
    }

    public void updatePainRecord(PainRecord... record) {
        updatePainRecordLiveDate.postValue(customerRepository.updatePainRecord(record));
    }
}
