package com.incomm.mahmad.weatherer;

import com.incomm.mahmad.weatherer.Model.CityResponse;
import com.incomm.mahmad.weatherer.Service.RetrofitManager;
import com.incomm.mahmad.weatherer.View.GetLocation.GetLocationView;

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

    public void getWeather(String city) {
        if (city.isEmpty() || city == null) {
            locationView.displayError("CITY IS NULL");
            return;
        }
        manager.getWeather(city.trim());

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
}
