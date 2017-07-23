package com.ipltd_bg.usistasksapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class StartDayFragment extends Fragment {
    private ArrayList<LoginResponse.CITRoute> routes;
    private android.widget.Spinner spinner = null;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_start_day, null);
        final Activity activity = getActivity();

        TextView userTv = (TextView) v.findViewById(R.id.username_field);

        LoginResponse lr = ((MainActivity) getActivity()).getLoginResponse();
        String names = lr.getData().getFirstName() + " " + lr.getData().getLastName();
        userTv.setText("Потребител:" + names);

        TextView passTv = (TextView) v.findViewById(R.id.status_field);
        StringBuilder sb = new StringBuilder();
        sb.append("Карта ID:" + lr.getData().getCardID() + "\n");
        sb.append("Град:" + lr.getData().getEmployeeCity() + "\n");
        sb.append("Регион:" + lr.getData().getRegion() + "\n");
        sb.append("Код служител:" + lr.getData().getEmplyeeCode() + "\n");
        sb.append("Компания:" + lr.getData().getCompanyCard() + "\n");

        passTv.setText(sb.toString());

        routes = lr.getData().getCitRoutes();


        ((MainActivity) getActivity()).setCurrentRoute(routes.size() > 0 ? routes.get(0) : null);

        String routeDefault = routes.size() > 0 ? routes.get(0).getCitRouteName() : "няма маршрут";


        spinner = (android.widget.Spinner) v.findViewById(R.id.select_team);
        spinner.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return routes.size();
            }

            @Override
            public Object getItem(int i) {
                return routes;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView tv = new TextView(getActivity());
                tv.setPadding(0, 10, 0, 10);
                tv.setTextSize(17.0f);
                tv.setText(routes.get(i).getCitRouteName());
                return tv;
            }
        });


        v.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null) {
                    if (getArguments().getBoolean("gotoMain"))
                        ((MainActivity) getActivity()).backToFragment(new MainFragment());
                    else {
                        ((MainActivity) getActivity()).Logoff();
                        ((MainActivity) getActivity()).backToFragment(new LoginFragment());
                    }
                } else
                {
                    ((MainActivity) getActivity()).Logoff();
                    ((MainActivity) getActivity()).backToFragment(new LoginFragment());
                }


            }
        });
        v.findViewById(R.id.bNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setCurrentRoute(routes.size() > 0 ? routes.get(spinner.getSelectedItemPosition()) : null);
                //Toast.makeText(getContext(), ((MainActivity) getActivity()).getCurrentRoute().getId() + "", Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).getStorage().StoreRoute(MainActivity.getCurrentRoute());
                if (getArguments() != null) {
                    if (getArguments().getBoolean("gotoMain"))
                        ((MainActivity) getActivity()).gotoFragment(new MainFragment());
                    else
                        ((MainActivity) getActivity()).gotoFragment(new CarFragment());
                } else
                    ((MainActivity) getActivity()).gotoFragment(new CarFragment());

            }
        });


        return v;
    }



}
