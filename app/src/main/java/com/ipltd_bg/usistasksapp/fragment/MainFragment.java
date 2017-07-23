package com.ipltd_bg.usistasksapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.adapter.MainTaskAdapter;
import com.ipltd_bg.usistasksapp.model.TaskListItem;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        v.findViewById(R.id.bScanObject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).gotoFragment(new ObjectFragment());
            }
        });
        v.findViewById(R.id.bCar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarFragment cf = new CarFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("gotoMain", true);
                cf.setArguments(bundle);
                ((MainActivity) getActivity()).gotoFragment(cf);
            }
        });
        v.findViewById(R.id.bStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaffFragment sf = new StaffFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("gotoMain", true);
                sf.setArguments(bundle);
                ((MainActivity) getActivity()).gotoFragment(sf);
            }
        });
        v.findViewById(R.id.bLogOff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getStorage().RemoveAllStored();
                ((MainActivity) getActivity()).gotoFragment(new LoginFragment());
            }
        });


        if (MainActivity.getTasks() == null)
            return v;

        ArrayList<TaskListItem> arr = new ArrayList<TaskListItem>();

        for (Object tli : MainActivity.getTasks().toArray())
            if (((TaskListItem) tli).getCitRouteCode().equals(MainActivity.getCurrentRoute().getCitRouteCode()))
                arr.add((TaskListItem) tli);
        MainTaskAdapter adapter = new MainTaskAdapter(getContext(), arr);
        ListView listView = (ListView) v.findViewById(R.id.lvTasks);
        listView.setAdapter(adapter);

        return v;
    }


}
