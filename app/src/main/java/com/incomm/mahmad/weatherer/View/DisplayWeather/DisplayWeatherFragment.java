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
    String weatherDescriptionText;
    String temperatureText;
    String cityText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() !=  null) {
            String[] response = (String[]) getArguments().getSerializable(ARG_RESPONSE_DATA);
            cityText  = "The name of the city is " + response[0];
            temperatureText = "Temperature (F): " + ((int) (1.8 *(Integer.parseInt(response[1]) -273) +32));
            weatherDescriptionText = "We will have" + response[2] + " today";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_weather, container, false);
        ButterKnife.bind(this, view);
        cityName.setText((cityText.isEmpty()) ? "City null" : cityText);
        temperature.setText((cityText.isEmpty()) ? "temperature null" : temperatureText);
        weatherDescription.setText((cityText.isEmpty()) ? "weather null" : weatherDescriptionText);

        return view;
    }

    public static DisplayWeatherFragment newInstance(String[] responseData) {
        DisplayWeatherFragment fragment = new DisplayWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE_DATA, responseData);
        fragment.setArguments(args);
        return fragment;
    }
}
