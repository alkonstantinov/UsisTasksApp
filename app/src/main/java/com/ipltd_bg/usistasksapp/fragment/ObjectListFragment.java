package com.ipltd_bg.usistasksapp.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.adapter.MainTaskAdapter;
import com.ipltd_bg.usistasksapp.adapter.ObjectListAdapter;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.TaskListItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateModel;
import com.ipltd_bg.usistasksapp.web.ApiConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObjectListFragment extends Fragment {

    private ListView listView = null;
    private boolean showAllTasks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setShowAllTasks(false);
        ((MainActivity) getActivity()).ReloadTasks();
        MainActivity.getUpdatedTasks().clear();
        MainActivity.setEnterTime(System.currentTimeMillis());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_object_list, container, false);
        listView = (ListView) v.findViewById(R.id.lvTasks);
        v.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).backToFragment(new MainFragment());
            }
        });
        LoadList();
        final ObjectListFragment fragment = this;
        v.findViewById(R.id.toggleShowAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.setShowAllTasks(!fragment.isShowAllTasks());
                fragment.LoadList();
            }
        });

        v.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle("Напускане на обект").setMessage("Искате ли да напуснете обекта?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        MainActivity.updateCommLock.lock();
                        try {

                            ArrayList<TaskUpdateItem> arr = MainActivity.getStorage().GetUpdatesInObject(MainActivity.getSelectedObject());
                            for (TaskListItem tli : MainActivity.getUpdatedTasks()) {
                                TaskUpdateItem tui = new TaskUpdateItem();
                                tui.setTaskId(tli.getTaskId());
                                switch (tli.getTaskStatus())
                                {
                                    case 1:
                                        tui.setTaskOffset(0);
                                        tui.setTaskStatus(3);
                                        break;
                                    case 4:
                                        tui.setTaskOffset((System.currentTimeMillis() - tui.getEnterTime()) / 1000);
                                        tui.setTaskStatus(2);
                                        break;
                                }
                                //tui.setTaskStatus(tli.getTaskStatus());

                                arr.add(tui);
                            }
                            MainActivity.getStorage().StoreAllObjectUpdates(MainActivity.getSelectedObject(), arr);
                            ((MainActivity) getActivity()).gotoFragment(new MainFragment());
                        } finally {
                            MainActivity.updateCommLock.unlock();
                        }
                    }
                }).setNegativeButton("Не", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });


        return v;
    }

    public boolean isShowAllTasks() {
        return showAllTasks;
    }

    public void setShowAllTasks(boolean showAllTasks) {
        this.showAllTasks = showAllTasks;
    }

    private void LoadList() {
        final String routeCodeCurrentlySelected = MainActivity.getCurrentRoute().getCitRouteCode();
        List<TaskListItem> listFiltered = new LinkedList<TaskListItem>();
        for (TaskListItem t : MainActivity.getTasks()) {
            if (t.getCitRouteCode().equals(routeCodeCurrentlySelected)) {
                if (t.getTaskStatus() == 1 && Integer.parseInt(MainActivity.getSelectedObject()) == t.getPartnerEntityTo().getPartnerEntityCode()) {
                    listFiltered.add(t); //Задачата е започната и трябва да свърши в текущия обект
                } else if (t.getTaskStatus() == 0 && Integer.parseInt(MainActivity.getSelectedObject()) == t.getPartnerEntityFrom().getPartnerEntityCode()) {
                    listFiltered.add(t); //Задачата е започната и трябва да свърши в текущия обект
                } else if (t.getTaskStatus() == 0 && Integer.parseInt(MainActivity.getSelectedObject()) == t.getPartnerEntityTo().getPartnerEntityCode() && this.showAllTasks) {
                    listFiltered.add(t); //Задачата не е започната и трябва да свърши в текущия обект и е казано покажи вс. задачи
                }



            }
            else{
                if (t.getTaskStatus() == 0 && Integer.parseInt(MainActivity.getSelectedObject()) == t.getPartnerEntityTo().getPartnerEntityCode() && this.showAllTasks) {
                    listFiltered.add(t); //Задачата не е започната и трябва да свърши в текущия обект и е казано покажи вс. задачи
                }
            }

        }


//        {
//             || t.getCitRouteCode().equals(routeCodeCurrentlySelected)) {
//                if (t.getTaskStatus() != 4) {
//                    if (t.getTaskStatus() == 0 && Integer.parseInt(MainActivity.getSelectedObject()) != t.getPartnerEntityFrom().getPartnerEntityCode()) {
//                        Log.d("tasks", "filter not started task: " + t.getTaskId());
//                    } else {
//
//                    }
//
//                } else {
//                    Log.d("tasks", "filter finished task: " + t.getTaskId());
//                }
//
//            }
//        }


        final List<TaskListItem> list = listFiltered;
        Collections.sort(list, new Comparator<TaskListItem>()

        {
            @Override
            public int compare(TaskListItem taskListItem, TaskListItem taskListItem2) {
                String c1 = taskListItem.getCitRouteCode();
                String c2 = taskListItem2.getCitRouteCode();
                if (c1.equals(routeCodeCurrentlySelected) && !c2.equals(routeCodeCurrentlySelected)) {
                    return -1;
                }
                if (!c1.equals(routeCodeCurrentlySelected) && c2.equals(routeCodeCurrentlySelected)) {
                    return 1;
                }
                return taskListItem.getTaskId() < taskListItem2.getTaskId() ? -1 : 1;
            }
        });
        if (list == null || list.size() == 0)

        {
            Toast.makeText(getActivity(), "Няма открити задачи", Toast.LENGTH_LONG).show();
        }

        ObjectListAdapter adapter = new ObjectListAdapter(getContext(), list);

        listView.setAdapter(adapter);

    }
}
