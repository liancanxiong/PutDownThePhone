package com.brilliantbear.putdownthephone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.brilliantbear.putdownthephone.Activity.AlarmActivity;
import com.brilliantbear.putdownthephone.db.AppLockDB;
import com.brilliantbear.putdownthephone.engine.ScreenLock;
import com.brilliantbear.putdownthephone.utils.SystemUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WatchedService extends Service {

    private static final String TAG = "WatchedService";
    private List<String> packageNames;
    private ScreenLockReceiver receiver;
    private ScreenLock screenLock;

    public WatchedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private Timer timer;
    private TimerTask timerTask;
    private int timeCount;
    private int timeCountMax;
    private Intent countIntent;
    private Intent maxIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);

        countIntent = new Intent("com.brilliantbear.putdownthephone.COUNT");
        maxIntent = new Intent("com.brilliantbear.putdownthephone.COUNT_MAX");
        timeCountMax = sp.getInt("time", 1) * 60;
        timeCount = timeCountMax;
        maxIntent.putExtra("count_max", timeCountMax);
        sendBroadcast(maxIntent);

        timer = new Timer();

        screenLock = new ScreenLock(this);

        AppLockDB db = new AppLockDB(this);
        packageNames = db.findAll();


        int mode = sp.getInt("mode", 0);


        if (mode == 0) {
            receiver = new ScreenLockReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(receiver, filter);
        } else {
            startWatch();
        }


        StartTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();

        flag = false;

        if (receiver != null) {
            unregisterReceiver(receiver);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");
//        timeCountMax = intent.getIntExtra("count", 100);
//        timeCount = timeCountMax;
//        StartTimer();

        return super.onStartCommand(intent, flags, startId);
    }

    private void StartTimer() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    timeCount--;

                    countIntent.putExtra("count", timeCount);
                    sendBroadcast(countIntent);

                    if (timeCount <= 0) {
                        Intent intent = new Intent("com.brilliantbear.putdownthephone.TIME_UP");
                        sendBroadcast(intent);
                        stopTimer();
                        stopSelf();
                    }
                }
            };
        }
        timer.schedule(timerTask, 0, 1000);
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private boolean flag;

    private void startWatch() {
        new Thread() {
            @Override
            public void run() {
                flag = true;
                while (flag) {
                    String packageName = SystemUtils.getTopAppInfoPackageName(WatchedService.this);
                    Log.d(TAG, packageName);
                    if (packageNames != null && packageNames.contains(packageName)) {
                        Intent intent = new Intent(WatchedService.this, AlarmActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    class ScreenLockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            screenLock.lock();
            Log.d(TAG, "receive");
        }
    }
}
