package com.incomm.mahmad.weatherer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    String location;
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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                location = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), location, Toast.LENGTH_LONG).show();
            }
        });

        return view;

    }

    private void setupUi() {
        presenter.getLocationEditTextHint();
    }

    @Override
    public void setLocationEditTextHint(String hint) {
        editText.setHint(hint);
    }
}
