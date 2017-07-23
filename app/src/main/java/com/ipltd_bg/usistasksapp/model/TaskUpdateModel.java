package com.ipltd_bg.usistasksapp.model;

import java.util.ArrayList;

/**
 * Created by alkon on 23-May-17.
 */

public class TaskUpdateModel extends LoginModel {

    private ArrayList<TaskUpdateItem> items;

    public ArrayList<TaskUpdateItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TaskUpdateItem> items) {
        this.items = items;
    }






    public TaskUpdateModel(){
        items = new ArrayList<TaskUpdateItem>();

    }

}
