package com.incomm.mahmad.weatherer.View.GetLocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.incomm.mahmad.weatherer.R;
import com.incomm.mahmad.weatherer.View.DisplayWeather.DisplayWeatherActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationFragment extends Fragment implements GetLocationView {
    @BindView(R.id.search_location)
    TextView textView;
    @BindView(R.id.search_box)
    EditText editText;
    @BindView(R.id.get_weather_button)
    Button button;
    @BindView(R.id.get_location)
    Button getLocationButton;

    private GetLocationPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GetLocationPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_location, container, false);
        ButterKnife.bind(this, view);
        setupUi();

        return view;
    }

    private void setupUi() {
        presenter.getLocationEditTextHint();
        presenter.getLocationFromGPS();
    }

    @Override
    public void setLocationEditTextHint(String hint) {
        editText.setHint(hint);
    }

    @OnClick(R.id.get_weather_button)
    public void onClickWeather() {
        presenter.getWeather(editText.getText().toString().trim());
    }

    @OnClick(R.id.get_location)
    public void onClickLocation() {

    }

    public void displayError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startDisplayWeatherActivity(String[] responseData) {
        Intent intent = DisplayWeatherActivity.newIntent(getActivity(), responseData);
        startActivity(intent);
        getActivity().finish();
    }
}
