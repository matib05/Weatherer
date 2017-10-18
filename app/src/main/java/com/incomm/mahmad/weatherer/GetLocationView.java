package com.incomm.mahmad.weatherer;

import com.incomm.mahmad.weatherer.Model.CityWeather;

import java.util.List;

/**
 * Created by mahmad on 10/13/2017.
 */

public interface GetLocationView {
    void setLocationEditTextHint(String hint);
    void displayError(String errorMsg);

    void displayWeather(List<CityWeather> resp);
}
