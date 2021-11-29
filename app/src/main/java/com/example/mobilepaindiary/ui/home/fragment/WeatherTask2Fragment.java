package com.example.mobilepaindiary.ui.home.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.AlarmBroadcastReceiver;
import com.example.mobilepaindiary.data.model.weathermodel.Main;
import com.example.mobilepaindiary.databinding.FragmentWeatherBinding;
import com.example.mobilepaindiary.ui.home.viewmodel.TasksViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class WeatherTask2Fragment extends Fragment {
    private static final String TAG = "WeatherTask2Fragment";
    private TasksViewModel viewModel;
    private FragmentWeatherBinding binding;

    public WeatherTask2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        viewModel.getCurrentWeather();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(getLayoutInflater(), container, false);
        binding.timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        binding.tvSetAlarm.setOnClickListener(v -> binding.llTimePicker.setVisibility(View.VISIBLE));
        binding.btnShowTimePicker.setOnClickListener(v -> {
            binding.llTimePicker.setVisibility(View.INVISIBLE);
            int hour = binding.timePicker.getHour();
            int minute = binding.timePicker.getMinute();
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date time = calendar.getTime();
            String format = new SimpleDateFormat("HH:mm").format(time);
            binding.tvSetAlarm.setText(format);
            sendAlarmEveryday(calendar);
        });
        startObserve();
        return binding.getRoot();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

//        getLocation();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            getAddress(location);
                        }
                    }
                });
    }

    private void startObserve() {
        viewModel.weatherLiveDate.observe(getViewLifecycleOwner(), weatherResponse -> {
            Main main = weatherResponse.main;
            binding.tvHumidity.setText(String.valueOf(main.humidity));
            binding.tvPressure.setText(String.valueOf(main.pressure));
            binding.tvTemperature.setText(String.valueOf(main.temp));
        });
    }

    private void sendAlarmEveryday(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmBroadcastReceiver.class);
        intent.setAction("alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static boolean isIgnoringBatteryOptimizations(Activity activity) {
        String packageName = activity.getPackageName();
        PowerManager pm = (PowerManager) activity
                .getSystemService(Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            return true;
        } else {
            return false;
        }
    }
    private FusedLocationProviderClient fusedLocationClient;
    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;
    private void getLocation(){
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else {
            locationProvider = LocationManager.PASSIVE_PROVIDER;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location!=null){
                    getAddress(location);

                }else{
                    locationManager.requestLocationUpdates(locationProvider, 3000, 1,locationListener);
                }
            }
        } else {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location!=null){
                getAddress(location);

            }else{
                locationManager.requestLocationUpdates(locationProvider, 3000, 1,locationListener);
            }
        }
    }

    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(requireContext(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                binding.tvLocation.setText(result.get(0).getLocality());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }
    };

}