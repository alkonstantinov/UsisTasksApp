package com.ipltd_bg.usistasksapp.timer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ipltd_bg.usistasksapp.activity.MainActivity;
import com.ipltd_bg.usistasksapp.model.Callback;
import com.ipltd_bg.usistasksapp.model.TaskUpdateItem;
import com.ipltd_bg.usistasksapp.model.TaskUpdateModel;
import com.ipltd_bg.usistasksapp.web.ApiConnector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created by alkon on 23-May-17.
 */

public class SendUnsentUpdates extends TimerTask {
    public Activity CurrentActivity;
    private static final String TAG = "Connectivity";

    @Override
    public synchronized void run() {
        //if (!MainActivity.updateCommLock.tryLock())
        //    return;

        ApiConnector ac = new ApiConnector();
        if (ac.IsNetworkAvailable(CurrentActivity)) {
            MainActivity.updateCommLock.lock();
            try {
                ArrayList<String> alObjects = MainActivity.getStorage().GetAllUpdatedObjects();
                if (alObjects.size() == 0)
                    return;
                for (final String key : alObjects) {
                    ArrayList<TaskUpdateItem> alTasks = MainActivity.getStorage().GetUpdatesInObject(key);
                    if (alTasks.size()==0)
                        continue;
                    if (alTasks != null && alTasks.size() > 0) {
                        TaskUpdateModel model = new TaskUpdateModel();
                        model.setUsername(MainActivity.getStorage().GetLastGoodLogin().getData().getCompanyCard());
                        model.setPassword(MainActivity.Password);
                        model.setItems(alTasks);
                        try {
                            ac.updateTask(model, new Callback() {
                                @Override
                                public void onResult(Object result) {

                                }

                                @Override
                                public void onError(Object error) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    MainActivity.getStorage().ClearObjectFromUpdates(key);

                }
            }
            finally {
                MainActivity.updateCommLock.unlock();
            }

        }
    }
}




