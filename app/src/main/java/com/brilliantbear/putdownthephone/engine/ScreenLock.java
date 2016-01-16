package com.brilliantbear.putdownthephone.engine;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.brilliantbear.putdownthephone.AdminReceiver;

/**
 * Created by Bear on 2016/1/14.
 */
public class ScreenLock {

    private Context context;
    private final DevicePolicyManager manager;
    private final ComponentName componentName;

    public ScreenLock(Context context) {
        this.context = context;
        manager = (DevicePolicyManager) context.getSystemService(Context
                .DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(context, AdminReceiver.class);
    }


    public boolean lock() {
        if (isActive()) {
            manager.lockNow();
            return true;
        }
        return false;
    }

    public void activeAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "本应用需要获取锁定屏幕的权限，才能使你放下手机");
        context.startActivity(intent);
    }

    public void removeActiveAdmin() {
        manager.removeActiveAdmin(componentName);
    }


    public boolean isActive() {
        return manager.isAdminActive(componentName);
    }
}
