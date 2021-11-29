package com.example.mobilepaindiary.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    // weather api official website https://openweathermap.org/current
    private static final String WEATHER_API_URL ="http://api.openweathermap.org/data/2.5/";// must end with '/'
    public static RetrofitInterface getRetrofitService(){
        retrofit=new Retrofit.Builder()
                .baseUrl(WEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitInterface.class);
    }
}
