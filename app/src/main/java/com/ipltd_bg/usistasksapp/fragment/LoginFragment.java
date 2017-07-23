package com.ipltd_bg.usistasksapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.activity.NFCActivity;
import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.activity.ScanActivity;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.GetTaskListModel;
import com.ipltd_bg.usistasksapp.model.LoginModel;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.TaskListItem;
import com.ipltd_bg.usistasksapp.storage.Storage;
import com.ipltd_bg.usistasksapp.web.ApiConnector;

import java.util.List;


public class LoginFragment extends Fragment {
    ImageButton bScan = null;
    ImageButton bNFC = null;
    Button bLogin = null;
    EditText tbCode = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        final Activity activity = getActivity();
        bLogin = (Button) v.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).CheckInternet();
                final LoginModel loginData = new LoginModel();
                loginData.setPassword(MainActivity.Password);
                loginData.setUsername(tbCode.getText().toString());
                Callback cb = new Callback() {
                    @Override
                    public void onResult(Object result) {
                        ((MainActivity)getActivity()).HideBusy();
                        if (MainActivity.getLoginResponse().isSuccess())
                        {
                            MainActivity.getStorage().StoreLastGoodLogin(MainActivity.getLoginResponse());
                            ((MainActivity) getActivity()).getStorage().StoreToday();
                            ((MainActivity) getActivity()).GetTaskList();
                            ((MainActivity) getActivity()).getStorage().StoreLogin(MainActivity.getLoginResponse());
                            ((MainActivity) getActivity()).gotoFragment(new StartDayFragment());
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Неуспешен вход",Toast.LENGTH_LONG).show();
                            tbCode.setText("");
                        }
                    }

                    @Override
                    public void onError(Object error) {
                        ((MainActivity)getActivity()).HideBusy();
                        Toast.makeText(getContext(),"Комуникационна грешка",Toast.LENGTH_LONG).show();

                    }
                };


                try {
                    ((MainActivity)getActivity()).ShowBusy();
                    new ApiConnector().login(loginData, cb);
                } catch (Exception e) {
                    ((MainActivity)getActivity()).HideBusy();
                    e.printStackTrace();
                }
            }
        });


        tbCode = (EditText) v.findViewById(R.id.tbCode);
        tbCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bLogin.setEnabled(charSequence.length() > 0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bScan = (ImageButton) v.findViewById(R.id.bScan);
        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ScanActivity.class);
                startActivityForResult(i, 1);
                Toast.makeText(getContext(),"Моля, сканирайте потребител",Toast.LENGTH_LONG).show();
            }
        });
        bNFC = (ImageButton) v.findViewById(R.id.bNFC);
        bNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, NFCActivity.class);
                startActivityForResult(i, 101);
                Toast.makeText(getContext(),"Моля, сканирайте потребител",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                tbCode.setText(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getData().toString();
                tbCode.setText(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }
}
