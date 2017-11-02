package com.incomm.mahmad.weatherer.View.GetLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.incomm.mahmad.weatherer.R;
import com.incomm.mahmad.weatherer.View.DisplayWeather.DisplayWeatherActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationFragment extends Fragment implements GetLocationView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.search_location)
    TextView textView;
    @BindView(R.id.search_box)
    EditText editText;
    @BindView(R.id.get_weather_button)
    Button button;
    @BindView(R.id.get_location)
    Button getLocationButton;
    @BindView(R.id.latitude)
    TextView latitude;
    @BindView(R.id.longitude)
    TextView longitude;

    private static final String TAG = GetLocationFragment.class.getSimpleName();
    private static final int PERMISSIONS_CODE = 123;

    private GetLocationPresenter presenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

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
        createGoogleApiClient();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(10000)
                .setFastestInterval(5000);
        setupUi();

        return view;
    }

    private void setupUi() {
        presenter.getLocationEditTextHint();
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
        if (mLastLocation == null) {
            requestPermissions();
            Log.d(TAG, "onClickLocation: requestPermissions Called");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLastLocation = location;
                    handleNewLocation();
                }
            });
            if (mLastLocation == null) {
                Log.d(TAG, "onClickLocation: STILL NULLLLLLLLLLLLLLLLLLLLLLLLLL");
                return;
            }
            latitude.setText(String.valueOf(mLastLocation.getLatitude()));
            longitude.setText(String.valueOf(mLastLocation.getLongitude()));
            return;
        }
        latitude.setText(String.valueOf(mLastLocation.getLatitude()));
        longitude.setText(String.valueOf(mLastLocation.getLongitude()));
        double[] coordinates = new double[] {
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
            Log.d(TAG, "handleNewLocation: " + mLastLocation.toString().toUpperCase());
        }
        else {
            Log.d(TAG, "handleNewLocation: mLastLocation is null");
        }

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