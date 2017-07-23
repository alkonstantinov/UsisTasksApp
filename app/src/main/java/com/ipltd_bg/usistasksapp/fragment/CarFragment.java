package com.ipltd_bg.usistasksapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.activity.NFCActivity;
import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.activity.ScanActivity;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.CarModel;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.VehicleInfo;
import com.ipltd_bg.usistasksapp.web.ApiConnector;

public class CarFragment extends Fragment {
    private ImageButton bScanCar = null;
    private ImageButton bNFCCar = null;
    private EditText tbCar = null;
    private com.rey.material.widget.FloatingActionButton bNext = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_car, null);
        final Activity activity = getActivity();


        v.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null) {
                    if (getArguments().getBoolean("gotoMain"))
                        ((MainActivity) getActivity()).backToFragment(new MainFragment());
                    else
                        ((MainActivity) getActivity()).backToFragment(new StartDayFragment());
                } else
                    ((MainActivity) getActivity()).backToFragment(new StartDayFragment());
            }
        });
        bNext = (com.rey.material.widget.FloatingActionButton) v.findViewById(R.id.bNext);
        bNext.setVisibility(View.INVISIBLE);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).CheckInternet();
                LoginResponse lr = ((MainActivity) getActivity()).getLoginResponse();
                CarModel cm = new CarModel();

                cm.setUsername(lr.getData().getCompanyCard());
                cm.setPassword(MainActivity.Password);
                cm.setPlate(tbCar.getText().toString());
                Callback cb = new Callback() {
                    @Override
                    public void onResult(Object result) {
                        ((MainActivity) getActivity()).HideBusy();
                        VehicleInfo vi = (VehicleInfo) result;
                        if (vi.isSuccess()) {
                            if (getArguments() != null) {
                                if (getArguments().getBoolean("gotoMain"))
                                    ((MainActivity) getActivity()).gotoFragment(new MainFragment());
                                else
                                    ((MainActivity) getActivity()).gotoFragment(new StaffFragment());
                            } else
                                ((MainActivity) getActivity()).gotoFragment(new StaffFragment());

                            MainActivity.setVerhicleInfo(vi);
                            ((MainActivity) getActivity()).getStorage().StoreCar(vi);
                        } else {
                            Toast.makeText(getContext(), "Автомобилът не е открит", Toast.LENGTH_LONG).show();
                            tbCar.setText("");
                        }
                    }

                    @Override
                    public void onError(Object error) {
                        ((MainActivity) getActivity()).HideBusy();
                        Toast.makeText(getContext(), "Комуникационна грешка", Toast.LENGTH_LONG).show();

                    }
                };


                try {
                    ((MainActivity) getActivity()).ShowBusy();
                    new ApiConnector().loadVehicleData(cm, cb);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((MainActivity) getActivity()).HideBusy();
                }
            }
        });

        tbCar = (EditText) v.findViewById(R.id.tbCar);
        tbCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bNext.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bNFCCar = (ImageButton) v.findViewById(R.id.bNFCCar);
        bNFCCar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowNFC();
                    }
                });

        bScanCar = (ImageButton) v.findViewById(R.id.bScanCar);
        bScanCar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowScanner();
                    }
                });

        ShowScanner();
        return v;
    }

    private void MoveToNextScreen() {

    }



    private void ShowScanner() {

        Intent i = new Intent(getActivity(), ScanActivity.class);
        startActivityForResult(i, 2);
        Toast.makeText(getContext(), "Моля, сканирайте автомобил", Toast.LENGTH_LONG).show();
    }


    private void ShowNFC() {
        Intent i = new Intent(getActivity(), NFCActivity.class);
        startActivityForResult(i, 102);
        Toast.makeText(getContext(), "Моля, сканирайте автомобил", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                tbCar.setText(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getData().toString();
                tbCar.setText(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }

}
