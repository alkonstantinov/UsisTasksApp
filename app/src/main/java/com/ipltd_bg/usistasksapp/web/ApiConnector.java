package com.ipltd_bg.usistasksapp.web;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.CarModel;
import com.ipltd_bg.usistasksapp.model.GetTaskListModel;
import com.ipltd_bg.usistasksapp.model.LoginModel;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.StaffModel;
import com.ipltd_bg.usistasksapp.model.TaskListItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateModel;
import com.ipltd_bg.usistasksapp.model.TeamMembersResponse;
import com.ipltd_bg.usistasksapp.model.VehicleInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by avlaev on 8/26/15.
 */
public class ApiConnector {
    public static final String TAG = "ApiConnector";
    // public static String BASE_URL = "http://inkaso.smarttech-development.com";
    public static String BASE_URL = "http://inkaso-mobile.usis-bg.com:45536";
    private ObjectMapper objectMapper;

//    public static final String md5(final String s) {
//        final String MD5 = "MD5";
//        try {
//            // Create MD5 Hash
//            MessageDigest digest = java.security.MessageDigest
//                    .getInstance(MD5);
//            digest.update(s.getBytes());
//            byte messageDigest[] = digest.digest();
//
//            // Create Hex String
//            StringBuilder hexString = new StringBuilder();
//            for (byte aMessageDigest : messageDigest) {
//                String h = Integer.toHexString(0xFF & aMessageDigest);
//                while (h.length() < 2)
//                    h = "0" + h;
//                hexString.append(h);
//            }
//            return hexString.toString();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }



    public boolean IsNetworkAvailable(android.content.Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void login(LoginModel loginModel, final Callback cb) throws Exception {

        JSONObject j = new JSONObject();
        j.put("card_id", loginModel.getUsername());
        j.put("hash", loginModel.getMD5Password());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/api/data/GetCITLogin/", j, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String str = jsonObject.toString();
                    Log.d(TAG, "login response:" + str);
                    LoginResponse resp = getObjectMapper(false).readValue(str, LoginResponse.class);
                    MainActivity.setLoginResponse(resp);
                    //App.app().getAuth().setLoginResponse(resp);
                    //new Storage().updateLoginResponse(str);
                    cb.onResult(resp);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    cb.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.VolleyError volleyError) {
                cb.onError(volleyError);
            }
        });


        VolleyManager.getInstance(MainActivity.app).addToRequestQueue(req);

    }

    public void loadVehicleData(CarModel car, final Callback callback) throws Exception {
        JSONObject j = new JSONObject();
        j.put("card_id", car.getUsername());
        j.put("hash", car.getMD5Password());
        j.put("reg_plate", car.getPlate());
        Log.d(TAG, "reg plate check:" + j.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/api/data/GetCITVehicle/", j, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String respStr = jsonObject.toString();
                    Log.d(TAG, "resp:" + respStr);
                    VehicleInfo resp = getObjectMapper(false).readValue(respStr, VehicleInfo.class);
                    callback.onResult(resp);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    callback.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.VolleyError volleyError) {
                callback.onError(volleyError);
            }
        });
        VolleyManager.getInstance(MainActivity.app).addToRequestQueue(req);
    }

    public void loadTeamMembersInfo(StaffModel staff, final Callback callback) throws Exception {
        JSONObject j = new JSONObject();
        j.put("card_id", staff.getUsername());
        j.put("hash", staff.getMD5Password());
        JSONArray arr = new JSONArray();
        for (String tm : staff.getAlStaff()) {
            arr.put(tm);
        }
        j.put("users", arr);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/api/data/GetCITEmployeesByIds/", j, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String s = jsonObject.toString();
                    JSONObject jj = new JSONObject(s);
                    String forParse = jj.getJSONObject("data").toString();
                    TeamMembersResponse resp = getObjectMapper(false).readValue(forParse, TeamMembersResponse.class);
                    callback.onResult(resp);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    callback.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.VolleyError volleyError) {
                callback.onError(volleyError);
            }
        });
        VolleyManager.getInstance(MainActivity.app).addToRequestQueue(req);
    }
        /*
    {"card_id": "3315-13857", "hash" : "e10adc3949ba59abbe56e057f20f883e", "data": [{"task_id" : "2232", "task_state" : 1}]}
     */
    public void updateTask(TaskUpdateModel model, final Callback callback) throws Exception {
        JSONObject j = new JSONObject();
        j.put("card_id", model.getUsername());
        j.put("hash", model.getMD5Password());
        JSONArray data = new JSONArray();

        for (TaskUpdateItem item:model.getItems()) {
            JSONObject taskJ = new JSONObject();
            taskJ.put("task_id", String.valueOf(item.getTaskId()));
            taskJ.put("task_state", item.getTaskStatus());
            if (item.getTaskOffset()>0) {
                taskJ.put("offset", item.getTaskOffset());
            }
            data.put(taskJ);
        }
        j.put("data", data);

        Log.d(TAG, "updateTask params: " + j.toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/api/data/DailyPlanState/", j, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d(TAG, "jsonObject response:" + jsonObject);
                    callback.onResult(new ArrayList<TaskListItem>());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    callback.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.VolleyError volleyError) {
                callback.onError(volleyError);
            }
        });
        VolleyManager.getInstance(MainActivity.app).addToRequestQueue(req);
    }

    public void loadTaskList(GetTaskListModel model, final Callback callback) throws Exception {

        //String username, String password, String entityCode, String regionCode
        JSONObject j = new JSONObject();
        j.put("card_id", model.getUsername());
        j.put("hash", model.getMD5Password());
        j.put("entity_code", model.getEntityCode());
        j.put("RegionCode", model.getRegionCode());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = sdf.format(new Date());//"2015-09-25";
        j.put("date", date);

        Log.d(TAG, "loadTaskList params: " + j.toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/api/data/GetCITDailyPlans/", j, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d(TAG, "jsonObject response:" + jsonObject);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data.has("Tasks")) {
                        JSONArray tasks = data.getJSONArray("Tasks");
                        String forParse = tasks.toString();

                        List<TaskListItem> resp = getObjectMapper(false).readValue(forParse, new TypeReference<List<TaskListItem>>() {
                        });
                        Log.d(TAG, "tasks count:" + resp.size());
                        callback.onResult(resp);
                    } else {
                        callback.onResult(new ArrayList<TaskListItem>());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    callback.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.VolleyError volleyError) {
                callback.onError(volleyError);
            }
        });

        VolleyManager.getInstance(MainActivity.app).addToRequestQueue(req);
    }
//
//
//
//    /**
//     * {"card_id": "641671972", "hash" : "e10adc3949ba59abbe56e057f20f883e", "reg_plate" : "С9849КР"}
//     */
//    public void loadVehicleData(String username, String password, String regPlate, final Callback callback) throws Exception {
//        JSONObject j = new JSONObject();
//        j.put("card_id", username);
//        j.put("hash", md5(password));
//        j.put("reg_plate", regPlate);
//        Log.d(TAG, "reg plate check:" + j.toString());
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/api/data/GetCITVehicle/", j, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                try {
//                    String respStr = jsonObject.toString();
//                    Log.d(TAG, "resp:" + respStr);
//                    VehicleInfo resp = getObjectMapper(false).readValue(respStr, VehicleInfo.class);
//                    new Storage().updateVehicleInfo(respStr);
//                    callback.onResult(resp);
//                } catch (Exception e) {
//                    Log.e(TAG, "", e);
//                    callback.onError(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(com.android.volley.VolleyError volleyError) {
//                callback.onError(volleyError);
//            }
//        });
//        VolleyManager.getInstance(App.app()).addToRequestQueue(req);
//    }
//
//
//
//
//        VolleyManager.getInstance(App.app()).addToRequestQueue(req);
//    }
//
    public ObjectMapper getObjectMapper(boolean shouldFail) {
        if (objectMapper == null) {
            objectMapper = createObjectMapper(shouldFail);
        }
        return objectMapper;
    }
//
    public static ObjectMapper createObjectMapper(boolean shouldFail) {
        ObjectMapper obj = new ObjectMapper();
        // http://stackoverflow.com/questions/8409567/jackson-unrecognized-field
        obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, shouldFail);
        return obj;
    }

}
