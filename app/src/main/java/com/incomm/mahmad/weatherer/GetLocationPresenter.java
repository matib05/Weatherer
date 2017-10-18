package com.incomm.mahmad.weatherer;

import com.incomm.mahmad.weatherer.Service.RetrofitManager;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationPresenter {
    private GetLocationView locationView;
    private RetrofitManager manager;

    public GetLocationPresenter(GetLocationView view) {
        this.locationView = view;
        this.manager = new RetrofitManager(view);
    }

    public void getLocationEditTextHint() {
        locationView.setLocationEditTextHint("City, State");
    }

    public void getWeather(String city) {
        if (!city.isEmpty()) {
            locationView.displayError("CITY IS NULL");
        }
        manager.getWeather(city);

    }
}
