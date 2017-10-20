package com.incomm.mahmad.weatherer.View.DisplayWeather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.incomm.mahmad.weatherer.R;

/**
 * Created by mahmad on 10/20/2017.
 */

public class DisplayWeatherActivity extends AppCompatActivity {
    private static final String EXTRA_RESPONSE_DATA = "com.incomm.mahmad.weatherer.View.DisplayWeather.responseData";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.weather_container);

        if (fragment == null) {
            String[] responseData = (String[]) getIntent().getSerializableExtra(EXTRA_RESPONSE_DATA);
            fragment = DisplayWeatherFragment.newInstance(responseData);
            fragmentManager.beginTransaction()
                    .add(R.id.weather_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, String[] responseData) {
        Intent intent = new Intent(packageContext, DisplayWeatherActivity.class);
        intent.putExtra(EXTRA_RESPONSE_DATA, responseData);
        return intent;
    }
}
