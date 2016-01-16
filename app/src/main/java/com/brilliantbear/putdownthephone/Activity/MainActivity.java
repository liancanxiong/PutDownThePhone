package com.brilliantbear.putdownthephone.Activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brilliantbear.putdownthephone.R;
import com.brilliantbear.putdownthephone.db.AppLockDB;
import com.brilliantbear.putdownthephone.engine.ScreenLock;
import com.brilliantbear.putdownthephone.service.WatchedService;
import com.brilliantbear.putdownthephone.utils.DimensionUtils;
import com.brilliantbear.putdownthephone.utils.SystemUtils;
import com.brilliantbear.putdownthephone.utils.T;
import com.brilliantbear.putdownthephone.view.RingProgress;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext = MainActivity.this;
    private static final String ACTION_COUNT = "com.brilliantbear.putdownthephone.COUNT";
    private static final String ACTION_COUNT_MAX = "com.brilliantbear.putdownthephone.COUNT_MAX";
    private static final String ACTION_TIME_UP = "com.brilliantbear.putdownthephone.TIME_UP";
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private FloatingActionButton fab;
    private RingProgress ringProgress;
    private CountReceiver receiver;
    private SharedPreferences sp;
    private ScreenLock screenLock;
    private ObjectAnimator downAnimation;
    private ObjectAnimator upAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    private void initData() {

        receiver = new CountReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_COUNT);
        filter.addAction(ACTION_COUNT_MAX);
        filter.addAction(ACTION_TIME_UP);

        registerReceiver(receiver, filter);

        sp = getSharedPreferences("settings", MODE_PRIVATE);


        screenLock = new ScreenLock(this);

        downAnimation = ObjectAnimator.ofFloat(fab, "translationY", 0, DimensionUtils.dp2px(getResources(), 120));
        downAnimation.setDuration(500);

        upAnimation = ObjectAnimator.ofFloat(fab, "translationY", DimensionUtils.dp2px(getResources(), 120), 0);
        upAnimation.setDuration(500);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMode(sp.getInt("mode", 0))) {
                    Intent intent = new Intent(MainActivity.this, WatchedService.class);
                    intent.putExtra("count", 100);
                    startService(intent);
                    downAnimation.start();
                }

            }
        });


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_applock:
                        startActivity(new Intent(MainActivity.this, AppLockActivity.class));
                        break;
                    case R.id.action_feedback:
                        showFeedbackDialog();
                        break;
                    case R.id.action_help:
                        showHelpDialog();
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (sp.getBoolean("help", true)) {
            showHelpDialog();
        }

        ringProgress.setInnerText(countToTimeString(sp.getInt("time", 1) * 60));
    }


    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setIcon(R.drawable.phone_small)
                .setMessage(R.string.feedback)
                .setPositiveButton("复制邮箱账号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService
                                (CLIPBOARD_SERVICE);
                        clipboardManager.setText("liancanxiong@hotmail.com");
                        T.showSingle(mContext, "复制成功");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("帮助")
                .setMessage(Html.fromHtml(this.getString(R.string.help)))
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sp.edit().putBoolean("help", false).apply();
                    }
                });
        builder.show();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string
                .drawer_open, R.string.drawer_close);

        mToggle.syncState();
        mDrawer.setDrawerListener(mToggle);

        ringProgress = (RingProgress) findViewById(R.id.ring_progress);
    }


    private boolean checkMode(int mode) {
        boolean result = false;
        if (mode == 0) {

            if (screenLock.isActive()) {
                screenLock.lock();
                result = true;
            } else {
                showActiveDialog();
            }
        } else if (mode == 1) {
            AppLockDB db = new AppLockDB(this);
            List<String> lockAppInfos = db.findAll();
            if (lockAppInfos.size() == 0) {
                showEmptyDialog();
            } else {
                result = true;
            }
        }
        return result;
    }

    private void showEmptyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.phone_small)
                .setTitle(R.string.app_name)
                .setMessage(R.string.empty_message)
                .setNeutralButton("添加应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, AppLockActivity.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("修改为锁屏模式", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sp.edit().putInt("mode", 0).apply();
                        T.showSingle(MainActivity.this, "已修改为锁屏模式");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void showActiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.phone_small)
                .setTitle(R.string.app_name)
                .setMessage(R.string.active_message)
                .setPositiveButton("去激活", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        screenLock.activeAdmin();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


    private class CountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_COUNT)) {
                int count = intent.getIntExtra("count", 0);
                setRingProgress(count);
            } else if (intent.getAction().equals(ACTION_COUNT_MAX)) {
                int max = intent.getIntExtra("count_max", 100);
                ringProgress.setMax(max);
            } else if (intent.getAction().equals(ACTION_TIME_UP)) {
                upAnimation.start();
            }

        }
    }


    private void setRingProgress(int count) {
        ringProgress.setProgress(count);
        ringProgress.setInnerText(countToTimeString(count));
    }

    private String countToTimeString(int count) {
        int minute = count / 60;
        int second = count % 60;

        StringBuilder sb = new StringBuilder();
        if (minute > 9) {
            sb.append(minute);
        } else {
            sb.append("0").append(minute);
        }

        sb.append(":");

        if (second > 9) {
            sb.append(second);
        } else {
            sb.append("0").append(second);
        }
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        if (SystemUtils.isServiceRunning(this, "com.brilliantbear.putdownthephone.service.WatchedService")) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else {
            super.onBackPressed();
        }
    }
}
