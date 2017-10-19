package com.incomm.mahmad.weatherer.View.DisplayWeather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.incomm.mahmad.weatherer.R;

import butterknife.BindView;

/**
 * Created by mahmad on 10/19/2017.
 */

public class DisplayWeatherFragment extends Fragment {
    @BindView(R.id.city)
    TextView cityName;
    @BindView(R.id.weather_main)
    TextView weatherDescription;
    @BindView(R.id.main_temp)
    TextView temperature;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_weather, container, false);
        if (savedInstanceState != null) {
            String[] response  = savedInstanceState.getStringArray("weatherData");
            cityName.setText(response[0]);
            temperature.setText(response[1]);
            weatherDescription.setText(response[2]);
        }
        else {
            String data = "no data";
            cityName.setText(data);
            temperature.setText(data);
            weatherDescription.setText(data);
        }

        return view;
    }
}
