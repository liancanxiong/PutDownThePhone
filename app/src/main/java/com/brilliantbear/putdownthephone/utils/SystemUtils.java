package com.brilliantbear.putdownthephone.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Bear on 2016/1/12.
 */
public class SystemUtils {

    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(50);
        for (ActivityManager.RunningServiceInfo info : infos) {
            String serviceClassName = info.service.getClassName();
            Log.d("system", serviceClassName);
            if (className.equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }


    public static String getTopAppInfoPackageName(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);

            if (runningTasks != null && runningTasks.size() > 0) {
                return runningTasks.get(0).topActivity.getPackageName();
            } else {
                return "";
            }
        } else {
            final int PROCESS_STATE_TOP = 2;
            try {
                Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
                List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo process : processes) {
                    if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && process.importanceReasonCode == 0) {
                        int state = processStateField.getInt(process);
                        if (state == PROCESS_STATE_TOP) {
                            String[] packname = process.pkgList;

                            return packname[0];
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
