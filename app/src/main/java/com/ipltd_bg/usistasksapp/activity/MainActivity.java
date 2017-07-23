package com.ipltd_bg.usistasksapp.activity;

import android.app.Activity;
import android.app.Application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.ipltd_bg.usistasksapp.R;
import com.ipltd_bg.usistasksapp.fragment.CarFragment;
import com.ipltd_bg.usistasksapp.fragment.MainFragment;
import com.ipltd_bg.usistasksapp.fragment.StaffFragment;
import com.ipltd_bg.usistasksapp.fragment.StartDayFragment;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.GetTaskListModel;
import com.ipltd_bg.usistasksapp.model.TaskListItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateItem;
import com.ipltd_bg.usistasksapp.storage.Storage;
import com.ipltd_bg.usistasksapp.fragment.LoginFragment;
import com.ipltd_bg.usistasksapp.model.LoginResponse;
import com.ipltd_bg.usistasksapp.model.TeamMembersResponse;
import com.ipltd_bg.usistasksapp.model.VehicleInfo;
import com.ipltd_bg.usistasksapp.timer.SendUnsentUpdates;
import com.ipltd_bg.usistasksapp.web.ApiConnector;
import com.ipltd_bg.usistasksapp.web.VolleyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<TaskListItem> updatedTasks;

    final public static String Password = "123456";
    private static Storage storage = new Storage();

    public static ReentrantLock updateCommLock = new ReentrantLock();
    public static Application app;
    private static LoginResponse loginResponse;
    private static int routeId;
    private static VehicleInfo verhicleInfo;
    private static TeamMembersResponse teamMembersResponse;

    private static String selectedObject;

    private static List<TaskListItem> tasks;

    public static List<TaskListItem> getTasks() {
        return tasks;
    }

    public static void setTasks(List<TaskListItem> tasks) {
        MainActivity.tasks = tasks;
    }

    public static String getSelectedObject() {
        return selectedObject;
    }

    public static void setSelectedObject(String selectedObject) {
        MainActivity.selectedObject = selectedObject;
    }

    public static ArrayList<TaskListItem> getUpdatedTasks() {
        return updatedTasks;
    }


    private void DisplayCorrectScreen() {
//        if (true)
//            gotoFragment(new LoginFragment());
//        else
        if (!storage.IsDataToday()) {
            storage.RemoveAllStored();
            gotoFragment(new LoginFragment());
        } else {
            LoginResponse lr = storage.GetLogin();
            if (lr == null) {
                gotoFragment(new LoginFragment());
                return;
            } else {
                this.setLoginResponse(lr);
            }

            LoginResponse.CITRoute route = storage.GetRoute();
            if (route == null) {
                gotoFragment(new StartDayFragment());
                return;
            } else {
                MainActivity.setCurrentRoute(route);
            }

            VehicleInfo vi = storage.GetCar();
            if (vi == null) {
                gotoFragment(new CarFragment());
                return;
            } else {
                this.setVerhicleInfo(vi);
                this.setTasks(getStorage().GetTasks());

            }

            TeamMembersResponse tmr = storage.GetStaff();
            if (tmr == null) {
                gotoFragment(new StaffFragment());
                return;
            } else {
                this.setTeamMembersResponse(tmr);
                gotoFragment(new MainFragment());
            }

        }

    }


    public static LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public static void setLoginResponse(LoginResponse loginResponse) {
        MainActivity.loginResponse = loginResponse;
    }

    private static LoginResponse.CITRoute currentRoute;

    public static int getRouteId() {
        return routeId;
    }

    public static void setRouteId(int routeId) {
        MainActivity.routeId = routeId;
    }

    public static VehicleInfo getVerhicleInfo() {
        return verhicleInfo;
    }

    public static void setVerhicleInfo(VehicleInfo verhicleInfo) {
        MainActivity.verhicleInfo = verhicleInfo;
    }

    public static TeamMembersResponse getTeamMembersResponse() {
        return teamMembersResponse;
    }

    public static void setTeamMembersResponse(TeamMembersResponse teamMembersResponse) {
        MainActivity.teamMembersResponse = teamMembersResponse;
    }

    public static LoginResponse.CITRoute getCurrentRoute() {
        return currentRoute;
    }

    public static void setCurrentRoute(LoginResponse.CITRoute route) {
        currentRoute = route;
    }

    protected Timer tSendUnsent;
    protected SendUnsentUpdates ttSendUnsent;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        updatedTasks = new ArrayList<TaskListItem>();
        app = getApplication();
        setStorage(new Storage());
        getStorage().ClearAllUpdates();
        tSendUnsent = new Timer();
        ttSendUnsent = new SendUnsentUpdates();
        ttSendUnsent.CurrentActivity = this;
        tSendUnsent.schedule(ttSendUnsent, 1000, 5000);
        VolleyManager.getInstance(app);


        setContentView(R.layout.activity_main);
        DisplayCorrectScreen();

    }

    @Override
    protected void onResume() {
        HideBusy();
        super.onResume();
    }

    public void Logoff() {
        setLoginResponse(null);
    }

    public void gotoFragment(Fragment mFragment) {
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_out_left);
        trans.replace(R.id.screen_content, mFragment);
        trans.commit();

    }

    public void backToFragment(Fragment mFragment) {
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_out_right);
        trans.replace(R.id.screen_content, mFragment);
        trans.commit();

    }


    public void CheckInternet() {
        boolean res = new ApiConnector().IsNetworkAvailable(this);
        if (res)
            return;
        final Activity activity = this;
        final com.rey.material.app.Dialog dlgRecheckInternet = new com.rey.material.app.Dialog(this);
        dlgRecheckInternet
                .title("Няма връзка с интернет")
                .positiveAction("Отново")
                .negativeAction("Затвори")
                .cancelable(false)
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean res = new ApiConnector().IsNetworkAvailable(activity);
                        if (res)
                            dlgRecheckInternet.dismiss();
                    }
                })
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlgRecheckInternet.dismiss();
                        System.exit(1);
                    }
                })
                .show();


    }

    public void ShowBusy() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.screen_content);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout.getLayoutParams();
        lp.height = 0;
        layout.setVisibility(View.INVISIBLE);
        findViewById(R.id.busy).setVisibility(View.VISIBLE);

    }

    public void HideBusy() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.screen_content);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout.getLayoutParams();
        lp.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layout.setVisibility(View.VISIBLE);
        findViewById(R.id.busy).setVisibility(View.INVISIBLE);

    }


    public static Storage getStorage() {
        return storage;
    }

    public static void setStorage(Storage storage) {
        MainActivity.storage = storage;
    }


    private static long enterTime;

    public static long getEnterTime() {
        return enterTime;
    }

    public static void setEnterTime(long enterTime) {
        MainActivity.enterTime = enterTime;
    }


    public void GetTaskList() {
        boolean res = new ApiConnector().IsNetworkAvailable(this);
        if (!res)
            return;

        MainActivity ma = this;
        LoginResponse lr = MainActivity.getLoginResponse();
        GetTaskListModel model = new GetTaskListModel();
        model.setPassword(MainActivity.Password);
        model.setUsername(lr.getData().getCompanyCard());
        model.setEntityCode("-1");
        model.setRegionCode(lr.getData().getRegionCode());
        try {
            new ApiConnector().loadTaskList(model, new Callback() {
                @Override
                public void onResult(Object result) {
                    MainActivity.setTasks((List<TaskListItem>) result);
                    new Storage().StoreTasks(MainActivity.getTasks());
                }

                @Override
                public void onError(Object error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReloadTasks() {
        ArrayList<String> alObjects = MainActivity.getStorage().GetAllUpdatedObjects();
        if (alObjects.size() > 0)
            return;
        GetTaskList();
    }

}
