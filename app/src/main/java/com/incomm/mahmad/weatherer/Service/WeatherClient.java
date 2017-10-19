package com.incomm.mahmad.weatherer.Service;

import com.incomm.mahmad.weatherer.Model.CityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mahmad on 10/17/2017.
 */

public interface WeatherClient {

    @GET("weather")
    Call<CityResponse> getWeatherForCity(@Query("q") String cityName, @Query("APPID") String APIKEY);

}
