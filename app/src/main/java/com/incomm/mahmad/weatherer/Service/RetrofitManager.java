package com.incomm.mahmad.weatherer.Service;

import android.util.Log;

import com.incomm.mahmad.weatherer.View.GetLocation.GetLocationPresenter;
import com.incomm.mahmad.weatherer.Model.CityResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by mahmad on 10/17/2017.
 */

public class RetrofitManager {

    private GetLocationPresenter presenter;
    private String url = "http://api.openweathermap.org/data/2.5/";
    private String apiKey = "49f151dd5d4461bcf4246b385b0cc075";
            //"31576d96eb3acb7333670187bc45f085";

    public RetrofitManager(GetLocationPresenter presenter) {
        this.presenter = presenter;
    }

    public void getWeatherForCity(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherClient client = retrofit.create(WeatherClient.class);

        Call<CityResponse> call = client.getWeatherForCity(city,apiKey);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                Log.i("GetWeather", "Response Code: " + response.code() + "\n" + "Response: " + response.raw());
                if (response.isSuccessful() && response.code() == 200) {
                    CityResponse resp = response.body();
                    presenter.getWeatherForCityCallBack(true, resp);
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Log.d(TAG, "onFailure:" +  t.getStackTrace());
                t.printStackTrace();
            }
        });
    }

    public void getWeatherForCoordinates(double[] coordinates) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherClient client = retrofit.create(WeatherClient.class);

        Call<CityResponse> call = client.getWeatherForCoordinates(coordinates[0], coordinates[1], apiKey);
            Log.d(TAG, "getWeatherForCity: " + call.toString());
            call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                Log.i("GetWeather", "Response Code: " + response.code() + "\n" + "Response: " + response.raw());
                if (response.isSuccessful() && response.code() == 200) {
                    CityResponse resp = response.body();
                    presenter.getWeatherForCoordinatesCallBack(true, resp);
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Log.d(TAG, "onFailure:" +  t.getStackTrace());
                t.printStackTrace();
            }
        });
    }
}
