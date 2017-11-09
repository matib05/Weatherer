package com.incomm.mahmad.weatherer.View.GetLocation;


import com.incomm.mahmad.weatherer.Model.CityResponse;

import java.io.FileInputStream;

/**
 * Created by mahmad on 10/13/2017.
 */

public interface GetLocationView {
    void setLocationEditTextHint(String hint);

    void displayError(String errorMsg);

    void startDisplayWeatherActivity(String[] responseData);

    void setDegrees(String[] responseData);

    void saveDataToSharedPreferences(String[] responseData);
}
