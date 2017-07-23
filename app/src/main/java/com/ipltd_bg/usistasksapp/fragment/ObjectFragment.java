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
import android.widget.TextView;
import android.widget.Toast;

import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.activity.NFCActivity;
import com.ipltd_bg.usistasksapp.activity.ScanActivity;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.CarModel;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.VehicleInfo;
import com.ipltd_bg.usistasksapp.web.ApiConnector;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObjectFragment extends Fragment {
    private ImageButton bScanObject = null;
    private ImageButton bNFCObject = null;
    private EditText tbObject = null;
    private com.rey.material.widget.FloatingActionButton bNext = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_object, null);
        final Activity activity = getActivity();
        ((MainActivity) getActivity()).ReloadTasks();

        v.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).backToFragment(new MainFragment());
            }
        });
        bNext = (com.rey.material.widget.FloatingActionButton) v.findViewById(R.id.bNext);
        bNext.setVisibility(View.INVISIBLE);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setSelectedObject(tbObject.getText().toString());
                ((MainActivity) getActivity()).gotoFragment(new ObjectListFragment());
            }
        });

        tbObject = (EditText) v.findViewById(R.id.tbObject);
        tbObject.addTextChangedListener(new TextWatcher() {
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

        bNFCObject = (ImageButton) v.findViewById(R.id.bNFCObject);
        bNFCObject.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowNFC();
                    }
                });

        bScanObject = (ImageButton) v.findViewById(R.id.bScanObject);
        bScanObject.setOnClickListener(
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
        startActivityForResult(i, 3);
        Toast.makeText(getContext(), "Моля, сканирайте обект", Toast.LENGTH_LONG).show();
    }


    private void ShowNFC() {
        Intent i = new Intent(getActivity(), NFCActivity.class);
        startActivityForResult(i, 103);
        Toast.makeText(getContext(), "Моля, сканирайте обект", Toast.LENGTH_LONG).show();
    }

    private String NormalizeResult(String code) {
        code = code.toLowerCase().replace("_u_", "");
        int i = code.indexOf("_");
        if (i > 0)
            code = code.substring(0, i);
        if(code.matches("[0-9]+")){
            code = Integer.parseInt(code) + "";
            return code;
        }
        Toast.makeText(getContext(),"Текстът трябва да съдържа само цифри",Toast.LENGTH_LONG).show();
        return "";


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                tbObject.setText(NormalizeResult(result));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
        if (requestCode == 103) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getData().toString();
                tbObject.setText(NormalizeResult(result));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }

}
