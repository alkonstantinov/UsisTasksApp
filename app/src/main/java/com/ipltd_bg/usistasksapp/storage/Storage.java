package com.ipltd_bg.usistasksapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.TaskListItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateModel;
import com.ipltd_bg.usistasksapp.model.TeamMembersResponse;
import com.ipltd_bg.usistasksapp.model.VehicleInfo;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by alkon on 20-May-17.
 */

public class Storage {
    private SharedPreferences prefs() {
        return MainActivity.app.getSharedPreferences("Storage", Context.MODE_PRIVATE);
    }

    public void StoreToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = sdf.format(new Date());
        prefs().edit().putString("today", today).commit();
        ;
    }


    public boolean IsDataToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = sdf.format(new Date());
        return prefs().getString("today", "").equals(today);
    }

    public void RemoveAllStored() {
        prefs().edit().remove("login").commit();
        prefs().edit().remove("routeId").commit();
        prefs().edit().remove("car").commit();
        prefs().edit().remove("staff").commit();
        prefs().edit().remove("today").commit();


    }

    public void StoreLogin(LoginResponse resp) {
        String json = new Gson().toJson(resp);
        prefs().edit().putString("login", json).commit();
        ;
    }

    public LoginResponse GetLogin() {
        String json = prefs().getString("login", null);
        return json == null ? null : new Gson().fromJson(json, LoginResponse.class);
    }


    public void StoreRoute(LoginResponse.CITRoute route) {
        String json = new Gson().toJson(route);
        prefs().edit().putString("route", json).commit();
    }

    public LoginResponse.CITRoute GetRoute() {

        String json = prefs().getString("route", null);
        return json == null ? null : new Gson().fromJson(json, LoginResponse.CITRoute.class);

    }

    public void StoreCar(VehicleInfo vi) {
        String json = new Gson().toJson(vi);
        prefs().edit().putString("car", json).commit();
        ;
    }

    public VehicleInfo GetCar() {
        String json = prefs().getString("car", null);
        return json == null ? null : new Gson().fromJson(json, VehicleInfo.class);
    }


    public void StoreStaff(TeamMembersResponse tmr) {
        String json = new Gson().toJson(tmr);
        prefs().edit().putString("staff", json).commit();
        ;
    }

    public TeamMembersResponse GetStaff() {
        String json = prefs().getString("staff", null);
        return json == null ? null : new Gson().fromJson(json, TeamMembersResponse.class);
    }


    public void StoreTasks(List<TaskListItem> tasks) {
        String json = new Gson().toJson(tasks.toArray());
        prefs().edit().putString("tasks", json).commit();
        ;

    }

    public List<TaskListItem> GetTasks() {
        String json = prefs().getString("tasks", null);
        if (json == null)
            return null;
        else {
            TaskListItem[] arr = new Gson().fromJson(json, TaskListItem[].class);
            return Arrays.asList(arr);

        }

    }


//    public ArrayList<TaskUpdateModel> GetTaskUpdates()
//    {
//        String json = prefs().getString("taskupdates", null);
//        if (json == null)
//            return new ArrayList<TaskUpdateModel>();
//        TaskUpdateModel[] arr = new Gson().fromJson(json, TaskUpdateModel[].class);
//        ArrayList<TaskUpdateModel> result = new ArrayList<TaskUpdateModel>();
//        result.addAll(Arrays.asList(arr));
//        return result;
//    }
//
//    public void AppendTaskUpdate(TaskUpdateModel model) {
//        ArrayList<TaskUpdateModel> result = GetTaskUpdates();
//        result.add(model);
//
//        String json = new Gson().toJson(result.toArray());
//        prefs().edit().putString("taskupdates", json).commit();
//    }
//
//
//
//
//    public void ClearTaskUpdates() {
//        prefs().edit().putString("taskupdates", null).commit();
//    }





    //Извлича структурата от базата
    public Map<String, ArrayList<TaskUpdateItem>> GetUpdatesStructure() {
        Map<String, ArrayList<TaskUpdateItem>> mUpdates = null;
        String json = prefs().getString("taskupdates", null);
        if (json == null) {
            mUpdates = new HashMap<String, ArrayList<TaskUpdateItem>>();
        } else {
            Type typeOfHashMap = new TypeToken<Map<String, ArrayList<TaskUpdateItem>>>() {
            }.getType();
            mUpdates = new Gson().fromJson(json, typeOfHashMap);
        }
        return mUpdates;
    }



    public void ClearAllUpdates() {
        if (prefs().getString("taskupdates", null) != null)
            prefs().edit().remove("taskupdates").commit();
    }

    //добавя нов ъпдейт в структурата
    public void StoreTaskUpdate(String objectCode, TaskUpdateItem tui) {
        Map<String, ArrayList<TaskUpdateItem>> mUpdates = this.GetUpdatesStructure();
        if (!mUpdates.containsKey(objectCode))
            mUpdates.put(objectCode, new ArrayList<TaskUpdateItem>());
        mUpdates.get(objectCode).add(tui);
        StoreAllUpdates(mUpdates);
    }

    public void StoreAllUpdates(Map<String, ArrayList<TaskUpdateItem>> mUpdates) {
        String json = new Gson().toJson(mUpdates);

        prefs().edit().putString("taskupdates", json).commit();

    }


    //добавя нов ъпдейт в структурата
    public void StoreAllObjectUpdates(String objectCode, ArrayList<TaskUpdateItem> arr) {
        Map<String, ArrayList<TaskUpdateItem>> mUpdates = this.GetUpdatesStructure();
        if (!mUpdates.containsKey(objectCode))
            mUpdates.put(objectCode, arr);
        else {
            mUpdates.get(objectCode).clear();
            mUpdates.get(objectCode).addAll(arr);
        }
        StoreAllUpdates(mUpdates);

    }


    //Изчиства даден обект от чакащи ъпдейти
    public void ClearObjectFromUpdates(String objectCode) {
        Map<String, ArrayList<TaskUpdateItem>> mUpdates = this.GetUpdatesStructure();
        if (mUpdates.containsKey(objectCode)) {
            mUpdates.remove(objectCode);
            StoreAllUpdates(mUpdates);
        }
    }

    //Взима всички записани обекти в базата
    public ArrayList<String> GetAllUpdatedObjects() {
        Map<String, ArrayList<TaskUpdateItem>> mUpdates = this.GetUpdatesStructure();
        ArrayList<String> result = new ArrayList<String>();
        for (String key : mUpdates.keySet())
            result.add(key);
        return result;

    }

    public ArrayList<TaskUpdateItem> GetUpdatesInObject(String objectCode) {
        Map<String, ArrayList<TaskUpdateItem>> mUpdates = this.GetUpdatesStructure();
        if (mUpdates.containsKey(objectCode))
            return mUpdates.get(objectCode);
        else
            return new ArrayList<TaskUpdateItem>();

    }

    public void StoreLastGoodLogin(LoginResponse resp) {
        String json = new Gson().toJson(resp);
        prefs().edit().putString("lastgoodlogin", json).commit();
        ;
    }

    public LoginResponse GetLastGoodLogin() {
        String json = prefs().getString("lastgoodlogin", null);
        return json == null ? null : new Gson().fromJson(json, LoginResponse.class);
    }

}
