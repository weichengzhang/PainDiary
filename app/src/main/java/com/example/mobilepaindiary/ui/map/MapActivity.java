package com.example.mobilepaindiary.ui.map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.data.model.SearchAddress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel viewModel;
    private AutoCompleteTextView mKeyWordsView = null;
    private ListView addressList = null;
    private List<SearchAddress> searchAddresses;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        viewModel.init(this);
        mKeyWordsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        addressList = (ListView) findViewById(R.id.list);
        addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addressList.setVisibility(View.GONE);
                SearchAddress searchAddress = searchAddresses.get(position);
                LatLng monashClayton = new LatLng(Double.parseDouble(searchAddress.getLat()), Double.parseDouble(searchAddress.getLon()));

                if (markerOptions == null) {
                    markerOptions = new MarkerOptions();
                }
                markerOptions.position(monashClayton)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))// color
                        .title(searchAddress.getDisplayName())
                        .snippet("Latitude: " + monashClayton.latitude + ", Longitude: " + monashClayton.longitude);

                mMap.addMarker(markerOptions);
                // 调整屏幕的大小级别,数字越大，地址越详细
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                //把屏幕对焦到节点
                mMap.moveCamera(CameraUpdateFactory.newLatLng(monashClayton));

            }
        });
        mKeyWordsView.setThreshold(1);

        // 当输入关键字变化时，动态更新建议列表
        mKeyWordsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() <= 0) {
                    addressList.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    addressList.setVisibility(View.GONE);
                    return;
                }

                viewModel.search(cs.toString());
            }
        });
        viewModel.addressList.observe(this, new Observer<List<SearchAddress>>() {
            @Override
            public void onChanged(List<SearchAddress> searchAddresses) {
                MapActivity.this.searchAddresses = searchAddresses;
                List<HashMap<String, String>> collect = searchAddresses.stream().map(new Function<SearchAddress,  HashMap<String, String>>() {
                    @Override
                    public  HashMap<String, String> apply(SearchAddress searchAddress) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("key", searchAddress.getDisplayName());
                        return map;
                    }
                }).collect(Collectors.toList());
                addressList.setVisibility(View.VISIBLE);
                Log.e("addressList", "onChanged: "+ searchAddresses.size() );
                SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),
                        collect,
                        R.layout.item_layout,
                        new String[]{"key"},
                        new int[]{R.id.item_tv});

                addressList.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near melbourne, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in melbourne and move the camera
//        LatLng melbourne = new LatLng(-37.813629, 144.963058);
//        LatLng monashClayton = new LatLng(-37.9161872, 145.140213);
//        mMap.addMarker(new MarkerOptions()
//                .position(monashClayton)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))// color
//                .title("Marker in Monash Clayton")
//                .snippet("Latitude: " + monashClayton.latitude + ", Longitude: " + monashClayton.longitude));
//        mMap.addMarker(new MarkerOptions().position(melbourne).title("Marker in Melbourne"));
//
//        // 调整屏幕的大小级别,数字越大，地址越详细
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//        //把屏幕对焦到节点
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));// user address

    }


}
