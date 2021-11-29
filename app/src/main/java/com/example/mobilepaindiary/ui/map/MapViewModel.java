package com.example.mobilepaindiary.ui.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilepaindiary.data.model.SearchAddress;
import com.example.mobilepaindiary.retrofit.RetrofitClient;
import com.example.mobilepaindiary.retrofit.RetrofitInterface;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {
    private MutableLiveData<List<LatLng>> liveDataAddList;
    private Context context;
    private List<LatLng> addList;
    private RetrofitInterface retrofitService;

    public MutableLiveData<List<SearchAddress>> addressList = new MutableLiveData<>();

    public MapViewModel() {
        liveDataAddList = new MutableLiveData<>();
    }

    public void init(Context context) {
        this.context = context;
        this.addList = new ArrayList<>();
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public LiveData<List<LatLng>> getLiveDataAddList() {
        return liveDataAddList;
    }


    // use android geocode to generate lat and lng
    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    public void search(String address) {
        Call<List<SearchAddress>> call = retrofitService.getAddress("http://open.mapquestapi.com/nominatim/v1/search.php?key=6b4N4rotbg2fRoIv1xAzB7OBCUUWBitT&format=json", address);
        call.enqueue(new Callback<List<SearchAddress>>() {
            @Override
            public void onResponse(Call<List<SearchAddress>> call, Response<List<SearchAddress>> response) {
                if (response.isSuccessful()) {
                    addressList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SearchAddress>> call, Throwable t) {

            }
        });
    }
}
