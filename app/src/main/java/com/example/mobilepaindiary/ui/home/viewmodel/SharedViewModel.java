package com.example.mobilepaindiary.ui.home.viewmodel;

import android.util.Log;
import android.widget.Toast;

import com.example.mobilepaindiary.data.model.weathermodel.WeatherResponse;
import com.example.mobilepaindiary.retrofit.RetrofitClient;
import com.example.mobilepaindiary.retrofit.RetrofitInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mobilepaindiary.MyApplication.getContext;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private RetrofitInterface retrofitInterface;

    public SharedViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
        retrofitInterface = RetrofitClient.getRetrofitService();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setMessage(String text) {
        mText.setValue(text);
    }

    public void getCurrentWeather(){
        Call<WeatherResponse> callAsync =
                retrofitInterface.getWeatherByCity("guangzhou,China");
        //makes an async request & invokes callback methods when the response returns
        callAsync.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {
                Log.d("Weather Response ",response.body().toString());
                if (response.isSuccessful()) {
//                    List<Items> list = response.body().items;
                    WeatherResponse weatherResponse = response.body();
                    String result= response.body().toString();


                    mText.setValue("Current location is : " + weatherResponse.name
                            + "\n"
                            + "Current temperature is : " + weatherResponse.main.temp);
                }
                else {
                    Log.i("Error ","Response failed");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("Error ","Weather API failed: " + t);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }


//    private class GetCredentialTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            return retrofitInterface.getWeatherByCity("guangzhou,China");
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            try {
//                Log.d("Weather Response ",result);
//                JSONObject jsonObject = new JSONObject(result);
//                mText.setValue(result);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}