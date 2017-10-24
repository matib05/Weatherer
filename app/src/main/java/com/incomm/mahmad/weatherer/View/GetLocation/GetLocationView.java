package com.incomm.mahmad.weatherer.View.GetLocation;

import com.incomm.mahmad.weatherer.Model.CityResponse;

/**
 * Created by mahmad on 10/13/2017.
 */

public interface GetLocationView {
    void setLocationEditTextHint(String hint);

    void displayError(String errorMsg);

    void startDisplayWeatherActivity(String[] responseData);
}
