package com.incomm.mahmad.weatherer;

/**
 * Created by mahmad on 10/13/2017.
 */

public class GetLocationPresenter {
    private GetLocationView locationView;

    public GetLocationPresenter(GetLocationView view) {
        this.locationView = view;
    }

    public void getLocationEditTextHint() {
        locationView.setLocationEditTextHint("City, State");
    }
}
