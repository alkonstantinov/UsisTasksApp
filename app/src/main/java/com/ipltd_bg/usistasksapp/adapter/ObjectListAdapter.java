package com.ipltd_bg.usistasksapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.model.TaskListItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateItem;
import com.ipltd_bg.usistasksapp.web.ApiConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkon on 22-May-17.
 */

public class ObjectListAdapter extends android.widget.ArrayAdapter<TaskListItem> {
    public ObjectListAdapter(Context context, List<TaskListItem> tasks) {
        super(context, 0, tasks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TaskListItem task = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        //if (convertView == null) {
        if (true) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.object_tasks, parent, false);
        }
        final View v = convertView;
        // Lookup view for data population
        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
        String txt = task.getActionType() + "\n" + task.getPartnerEntityFrom().getName() + " - " + task.getPartnerEntityTo().getName();
        txt = txt.replace("(<>АТМ)", "");
        taskName.setText(txt);
        // Return the completed view to render on screen

        final com.rey.material.widget.Button btnProcess = (com.rey.material.widget.Button) convertView.findViewById(R.id.bTaskDo);
        switch (task.getTaskStatus()) {
            case 0:
            case 3:
            case 4:
                btnProcess.setText("Стартирай");
                btnProcess.setTextColor(Color.BLACK);
                break;
            case 1:
                btnProcess.setText("Завърши");
                btnProcess.setTextColor(Color.YELLOW);
                break;
            case 2:
                v.setVisibility(View.INVISIBLE);
                v.getLayoutParams().height = 1;
                break;
        }

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newStatus = 0;
                TaskUpdateItem tui = new TaskUpdateItem();
                tui.setTaskId(task.getTaskId());
                tui.setObjectFinished(false);
                switch (task.getTaskStatus()) {
                    case 0:
                        if (Integer.parseInt(MainActivity.getSelectedObject()) != task.getPartnerEntityFrom().getPartnerEntityCode()) {
                            Toast.makeText((Activity) view.getContext(), "Задачата може да бъде стартирана само в обект " + task.getPartnerEntityFrom().getPartnerEntityCode(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }


                        tui.setTaskStatus(1);
                        tui.setTaskOffset(0);
                        tui.setEnterTime(MainActivity.getEnterTime());
                        btnProcess.setText("Завърши");
                        btnProcess.setTextColor(Color.YELLOW);
                        break;
                    case 1:
                        if (Integer.parseInt(MainActivity.getSelectedObject()) != task.getPartnerEntityTo().getPartnerEntityCode()) {
                            Toast.makeText((Activity) view.getContext(), "Задачата може да бъде завършена само в обект " + task.getPartnerEntityTo().getPartnerEntityCode(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        tui.setTaskStatus(4);
                        tui.setEnterTime(MainActivity.getEnterTime());
                        tui.setTaskOffset(0);
                        v.setVisibility(View.INVISIBLE);
                        break;
                }
                task.setTaskStatus(tui.getTaskStatus());
                MainActivity.getStorage().StoreTasks(MainActivity.getTasks());
                MainActivity.getUpdatedTasks().add(task);
                MainActivity.updateCommLock.lock();
                try {

                    MainActivity.getStorage().StoreTaskUpdate(MainActivity.getSelectedObject(), tui);
                }
                finally {
                    MainActivity.updateCommLock.unlock();
                }
            }
        });

        final String routeCodeCurrentlySelected = MainActivity.getCurrentRoute().getCitRouteCode();
        if (!task.getCitRouteCode().equals(routeCodeCurrentlySelected))
            taskName.setBackgroundColor(Color.LTGRAY);
        return convertView;
    }
}
