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
import butterknife.ButterKnife;

/**
 * Created by mahmad on 10/19/2017.
 */

public class DisplayWeatherFragment extends Fragment {
    private static final String ARG_RESPONSE_DATA = "responseData";
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
        ButterKnife.bind(this, view);
        String[] response = (String[]) getArguments().getSerializable(ARG_RESPONSE_DATA);
        String cityText  = "The name of the city is " + response[0];
        String temperatureText = "Temperature (F): " + (1.8 *(Double.parseDouble(response[1]) -273) +32);
        String weatherDescriptionText = "It is will be " + response[2] + "y today";
        cityName.setText(cityText);
        temperature.setText(temperatureText);
        weatherDescription.setText(weatherDescriptionText);

        return view;
    }

    public static DisplayWeatherFragment newInstance(String[] responseData) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE_DATA, responseData);
        DisplayWeatherFragment fragment = new DisplayWeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
