package com.brilliantbear.putdownthephone;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("AdminReceiver", "onReceive");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.d("AdminReceiver", "onDisabled");
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d("AdminReceiver", "onEnabled");
    }
}
