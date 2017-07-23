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
import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.activity.ScanActivity;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.StaffModel;
import com.ipltd_bg.usistasksapp.model.TeamMembersResponse;
import com.ipltd_bg.usistasksapp.web.ApiConnector;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;

import java.util.ArrayList;

public class StaffFragment extends Fragment {
    private ImageButton bScanStaff = null;
    private ImageButton bNFCStaff = null;

    private Button bAdd = null;
    private EditText tbStaff = null;
    public ArrayList<String> alStaff = new ArrayList<String>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_staff, null);
        final Activity activity = getActivity();


        v.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null) {
                    if (getArguments().getBoolean("gotoMain"))
                        ((MainActivity) getActivity()).backToFragment(new MainFragment());
                    else
                        ((MainActivity) getActivity()).backToFragment(new CarFragment());
                } else
                    ((MainActivity) getActivity()).backToFragment(new CarFragment());


            }
        });

        v.findViewById(R.id.forward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        bAdd = (Button) v.findViewById(R.id.bAdd);
        bAdd.setEnabled(false);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alStaff.add(tbStaff.getText().toString());
                tbStaff.setText("");
            }
        });


        tbStaff = (EditText) v.findViewById(R.id.tbStaff);
        tbStaff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bAdd.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bScanStaff = (ImageButton) v.findViewById(R.id.bScanStaff);
        bScanStaff.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowScanner();
                    }
                });

        bNFCStaff = (ImageButton) v.findViewById(R.id.bNFCStaff);
        bNFCStaff.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowNFC();
                    }
                });

        ShowScanner();
        return v;
    }


    private void ShowScanner() {
        Intent i = new Intent(getActivity(), ScanActivity.class);
        startActivityForResult(i, 3);
        Toast.makeText(getContext(), "Моля, сканирайте член на екипа", Toast.LENGTH_LONG).show();
    }

    private void ShowNFC() {
        Intent i = new Intent(getActivity(), ScanActivity.class);
        startActivityForResult(i, 103);
        Toast.makeText(getContext(), "Моля, сканирайте член на екипа", Toast.LENGTH_LONG).show();
    }


    private void next() {
        ((MainActivity) getActivity()).CheckInternet();
        LoginResponse lr = ((MainActivity) getActivity()).getLoginResponse();
        StaffModel sm = new StaffModel();

        sm.setUsername(lr.getData().getCompanyCard());
        sm.setPassword(MainActivity.Password);
        sm.setAlStaff(alStaff);
        Callback cb = new Callback() {
            @Override
            public void onResult(Object result) {
                ((MainActivity) getActivity()).HideBusy();
                TeamMembersResponse tmr = (TeamMembersResponse) result;
                ((MainActivity) getActivity()).getStorage().StoreStaff(tmr);

                ((MainActivity) getActivity()).gotoFragment(new MainFragment());

            }

            @Override
            public void onError(Object error) {
                ((MainActivity) getActivity()).HideBusy();
                Toast.makeText(getContext(), "Комуникационна грешка", Toast.LENGTH_LONG).show();

            }
        };


        try {
            ((MainActivity) getActivity()).ShowBusy();
            new ApiConnector().loadTeamMembersInfo(sm, cb);
        } catch (Exception e) {
            ((MainActivity) getActivity()).HideBusy();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                alStaff.add(result);

                final Dialog moreMembersDialog = new Dialog(getContext());
                moreMembersDialog
                        .title("Добави още охранители?")
                        .positiveAction("Да")
                        .negativeAction("Не")
                        .cancelable(false)
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                moreMembersDialog.dismiss();
                                ShowScanner();
                            }
                        })
                        .negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                moreMembersDialog.dismiss();
                                next();
                            }
                        })
                        .show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
        if (requestCode == 103) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getData().toString();
                alStaff.add(result);

                final Dialog moreMembersDialog = new Dialog(getContext());
                moreMembersDialog
                        .title("Добави още охранители?")
                        .positiveAction("Да")
                        .negativeAction("Не")
                        .cancelable(false)
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                moreMembersDialog.dismiss();
                                ShowNFC();
                            }
                        })
                        .negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                moreMembersDialog.dismiss();
                                next();
                            }
                        })
                        .show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }

}
