package com.incomm.mahmad.weatherer.View.GetLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.incomm.mahmad.weatherer.R;
import com.incomm.mahmad.weatherer.View.DisplayWeather.DisplayWeatherActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationFragment extends Fragment implements GetLocationView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.get_location_fragment)
    LinearLayout layout;
    @BindView(R.id.search_location)
    TextView degrees;
    @BindView(R.id.search_box)
    EditText editText;
    @BindView(R.id.get_weather_button)
    Button button;
    @BindView(R.id.get_location)
    Button getLocationButton;
    @BindView(R.id.city)
    TextView cityText;
    @BindView(R.id.weather_description)
    TextView weatherDescription;
    @BindView(R.id.last_updated)
    TextView lastUpdated;

    private static final String TAG = GetLocationFragment.class.getSimpleName();
    private static final int PERMISSIONS_CODE = 123;
    double[] coordinates;

    private GetLocationPresenter presenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private String RESPONSE_DATA = "WeatherResponseDataFile";
    private SharedPreferences settings;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GetLocationPresenter(this);
        settings = getActivity().getSharedPreferences(RESPONSE_DATA, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_location, container, false);
        ButterKnife.bind(this, view);
        createGoogleApiClient();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(100000)
                .setFastestInterval(10000);
        setupUi();
        return view;
    }

    private void setupUi() {
        presenter.getLocationEditTextHint();
        setDegrees();
        setCity();
        setWeatherDescriptionAndBackground();
        setLastUpdated();
    }

    private void setDegrees() {
        String degreeInFahrenheit = String.valueOf((int) (1.8 *(Integer.parseInt(settings.getString("temperature", "255")) -273) +32)) + "°F";
        this.degrees.setText(degreeInFahrenheit);
    }

    private void setCity() {
        String cityText = settings.getString("city", "city") + " |";
        this.cityText.setText(cityText.toUpperCase());
    }

    @SuppressLint("NewApi")
    private void setWeatherDescriptionAndBackground() {
        String description = settings.getString("description", "normal").toLowerCase();

        if (description.contains("mist")) {
            layout.setBackground(getResources().getDrawable(R.drawable.misty_skies));
        } else if (description.contains("rain") || description.contains("drizzle")) {
            layout.setBackground(getResources().getDrawable(R.drawable.rainy_skies));
        } else if (description.contains("cloud") || description.contains("overcast")) {
            layout.setBackground(getResources().getDrawable(R.drawable.cloud_skies));
        } else if (description.contains("haze")) {
            layout.setBackground(getResources().getDrawable(R.drawable.haze_skies));
        } else {
            layout.setBackground(getResources().getDrawable(R.drawable.clear_skies));
        }

        weatherDescription.setText(settings.getString("description", "description").toUpperCase());
    }

    private void setLastUpdated() {
        this.lastUpdated.setText(settings.getString("lastUpdated", "Last Updated"));
    }

    @Override
    public void setLocationEditTextHint(String hint) {
        editText.setHint(hint);
    }

    @OnClick(R.id.get_weather_button)
    public void onClickWeather() {
        presenter.getWeatherForCity(editText.getText().toString().trim());
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.get_location)
    public void onClickLocation() {
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        requestPermissions();
        while (mLastLocation == null) {
            Log.d(TAG, "getLocation: requestPermissions Called");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLastLocation = location;
                    handleNewLocation();
                }
            });
            if (mLastLocation != null) {
                Log.d(TAG, "getLocation: mLastLocation is NOT NULL");
                break;
                /*String toastMessage = "Location is not set.\nPlease turn on Location and click \"Get Location\"";
                Toast.makeText(getContext(), toastMessage, Toast.LENGTH_LONG).show();*/
            }
        }
        coordinates = new double[] {
                mLastLocation.getLatitude(),
                mLastLocation.getLongitude()
        };
        presenter.getWeatherForCoordinates(coordinates);
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

    @Override
    public void setDegrees(String[] responseData) {
        if (responseData[1] != null) {
            Integer degree = ((int) (1.8 *(Integer.parseInt(responseData[1]) -273) +32));
            degrees.setText(degree + "°F");
        }
    }

    private void createGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .enableAutoManage(getActivity(), this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected!");
        requestPermissions();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        handleNewLocation();
    }

    private void handleNewLocation() {
        if (mLastLocation != null) {
            Log.d(TAG, "handleNewLocation: " + mLastLocation.toString());
            coordinates = new double[] {
                    mLastLocation.getLatitude(),
                    mLastLocation.getLongitude()
            };
            presenter.getWeatherForCoordinates(coordinates);
        } else {
            Log.d(TAG, "handleNewLocation: mLastLocation is null");
        }

    }

    @Override
    public void saveDataToSharedPreferences(String[] responseData) {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MM/dd hh:mm");
        String lastUpdated = formatter.format(date);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("city", responseData[0]);
        setCity();
        editor.putString("temperature", responseData[1]);
        setDegrees();
        editor.putString("description", responseData[2]);
        setWeatherDescriptionAndBackground();
        editor.putString("lastUpdated", lastUpdated);
        setLastUpdated();
        editor.apply();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: Suspended, calling connect...");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: Failed with code: " + connectionResult.getErrorCode()
                +  "and message: " + connectionResult.getErrorMessage());
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSIONS_CODE);
            Log.d(TAG, "requestPermissions: CREATED PERMISSIONS, is Location Services Enabled?...");
            enablingLocationServices();
        }
    }

    public  void enablingLocationServices() {
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        if(!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(getContext().getResources().getString(R.string.gps_network_not_enabled_message));
            dialog.setPositiveButton(getContext().getResources().getString(R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Log.d(TAG, "onClick: setPositiveButton clicked");
                    Intent ActionLocationSourceSettings = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(ActionLocationSourceSettings);
                }
            });
            dialog.setNegativeButton(getContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Log.d(TAG, "onClick: setNegativeButton clicked");
                }
            });
            dialog.show();
        }
    }
}