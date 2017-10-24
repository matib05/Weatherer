package com.incomm.mahmad.weatherer.View.GetLocation;

import com.incomm.mahmad.weatherer.Model.CityResponse;
import com.incomm.mahmad.weatherer.Service.RetrofitManager;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationPresenter {
    private GetLocationView locationView;
    private RetrofitManager manager;
    //private GoogleApiClient.Builder mGoogleApiClient;

    public GetLocationPresenter(GetLocationView view) {
        this.locationView = view;
        this.manager = new RetrofitManager(this);
    }

    public void getLocationEditTextHint() {
        locationView.setLocationEditTextHint("Enter City");
    }

    public void getWeather(String location) {
        if (location.isEmpty()) {
            locationView.displayError("CITY IS NULL");
            return;
        }
        manager.getWeather(location);

    }

    public void getWeatherForCityCallBack(boolean isSuccess, CityResponse response) {
        if (isSuccess) {
            String[] responseData = {
                    response.getName(),
                    response.getMain().getTemp().toString(),
                    response.getWeather().get(0).getDescription()
            };

            locationView.startDisplayWeatherActivity(responseData);
        }
        else {
            locationView.displayError("Error, Cannot process request");
        }
    }

    public Object getLocationFromGPS() {
        return null;
    }
}
