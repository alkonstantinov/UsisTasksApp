package com.ipltd_bg.usistasksapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.model.TaskListItem;

import java.util.ArrayList;

/**
 * Created by alkon on 22-May-17.
 */

public class MainTaskAdapter extends android.widget.ArrayAdapter<TaskListItem> {
    public MainTaskAdapter(Context context, ArrayList<TaskListItem> tasks) {
        super(context, 0, tasks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskListItem task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        //if (convertView == null) {
        if (true) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_main_tasks, parent, false);
        }
        // Lookup view for data population
        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
        String txt = task.getActionType() + "\n" + task.getPartnerEntityFrom().getName() + " - " + task.getPartnerEntityTo().getName();
        txt = txt.replace("(<>АТМ)", "");
        taskName.setText(txt);
        if (task.getTaskStatus()==4)
            convertView.setVisibility(View.INVISIBLE);
        // Return the completed view to render on screen
        return convertView;
    }
}
