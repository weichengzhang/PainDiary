package com.example.mobilepaindiary.retrofit;


import com.example.mobilepaindiary.data.model.SearchAddress;
import com.example.mobilepaindiary.data.model.weathermodel.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitInterface {

    String BASE_PARAM = "weather?APPID=956c4e6782154256e6e0493caa459a28&units=metric";

    @GET(BASE_PARAM)
    Call<WeatherResponse> getWeatherByCity(
            @Query("q") String keyword);

    @GET(BASE_PARAM)
    Call<WeatherResponse> getWeatherByLatLon(
            @Query("lat") String lat,
            @Query("lon") String lon);

    @GET
    Call<List<SearchAddress>> getAddress(@Url String url, @Query("q") String lat);
}
