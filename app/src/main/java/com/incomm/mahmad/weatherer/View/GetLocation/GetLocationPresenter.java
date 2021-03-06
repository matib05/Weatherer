package com.incomm.mahmad.weatherer.View.GetLocation;

import android.util.Log;

import com.incomm.mahmad.weatherer.Model.CityResponse;
import com.incomm.mahmad.weatherer.Service.RetrofitManager;

import static android.content.ContentValues.TAG;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationPresenter {
    private GetLocationView locationView;
    private RetrofitManager manager;

    public GetLocationPresenter(GetLocationView view) {
        this.locationView = view;
        this.manager = new RetrofitManager(this);
    }

    public void getLocationEditTextHint() {
        locationView.setLocationEditTextHint("Enter City");
    }

    public void getWeatherForCity(String location) {
        if (location.isEmpty()) {
            locationView.displayError("CITY IS NULL");
            return;
        }
        manager.getWeatherForCity(location);

    }

    public void getWeatherForCoordinates(double[] coordinates) {
        if (coordinates.length == 0) {
            locationView.displayError("COORDINATES ARE EMPTY");
            return;
        }
        manager.getWeatherForCoordinates(coordinates);
    }

    public void getWeatherForCityCallBack(boolean isSuccess, CityResponse response) {
        if (isSuccess) {
            String[] responseData = {
                    response.getName(),
                    response.getMain().getTemp().toString(),
                    response.getWeather().get(0).getDescription()
            };
            Log.d(TAG, "getWeatherForCityCallBack: " + response.toString());
            locationView.saveDataToSharedPreferences(responseData);
        }
        else {
            locationView.displayError("Error, Cannot process request");
        }
    }

    public void getWeatherForCoordinatesCallBack(boolean isSuccess, CityResponse response) {
        if (isSuccess) {
            String[] responseData = {
                    response.getName(),
                    response.getMain().getTemp().toString(),
                    response.getWeather().get(0).getMain()
            };
            locationView.saveDataToSharedPreferences(responseData);
        } else {
            locationView.displayError("Error, Cannot process request");
        }
    }
}
