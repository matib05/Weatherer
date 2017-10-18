package com.incomm.mahmad.weatherer.Service;

import com.incomm.mahmad.weatherer.GetLocationView;
import com.incomm.mahmad.weatherer.Model.CityWeather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mahmad on 10/17/2017.
 */

public class RetrofitManager {

    private GetLocationView locationView;

    public RetrofitManager(GetLocationView locationView) {
        this.locationView = locationView;
    }

    public void getWeather(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("api.openweathermap.org/data/2.5/weather")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherClient client = retrofit.create(WeatherClient.class);

        Call<List<CityWeather>> call = client.getWeatherForCity(city);
        call.enqueue(new Callback<List<CityWeather>>() {
            @Override
            public void onResponse(Call<List<CityWeather>> call, Response<List<CityWeather>> response) {
                List<CityWeather> resp = response.body();
                if (resp != null) {
                    locationView.displayWeather(resp);
                }
            }

            @Override
            public void onFailure(Call<List<CityWeather>> call, Throwable t) {

            }
        });
    }




}
