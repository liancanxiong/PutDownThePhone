package com.brilliantbear.putdownthephone.Activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.brilliantbear.putdownthephone.R;
import com.brilliantbear.putdownthephone.engine.ScreenLock;
import com.brilliantbear.putdownthephone.utils.T;
import com.brilliantbear.putdownthephone.view.SettingItemView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = SettingsActivity.this;
    private SettingItemView sivAppLock;
    private SettingItemView sivMode;
    private SettingItemView sivCancel;
    private SettingItemView sivTime;
    private SettingItemView sivFeedback;
    private SettingItemView sivHelp;
    private ScreenLock screenLock;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        initData();
    }

    private void initData() {
        screenLock = new ScreenLock(this);
        sp = getSharedPreferences("settings", MODE_PRIVATE);

        sivMode.setDesc(modeList[sp.getInt("mode", 0)]);
        sivTime.setDesc(timelist[sp.getInt("time", 1) - 1]);
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);


        sivAppLock = (SettingItemView) findViewById(R.id.siv_app_lock);
        sivAppLock.setOnClickListener(this);
        sivMode = (SettingItemView) findViewById(R.id.siv_mode);
        sivMode.setOnClickListener(this);
        sivCancel = (SettingItemView) findViewById(R.id.siv_cancel);
        sivCancel.setOnClickListener(this);
        sivTime = (SettingItemView) findViewById(R.id.siv_time);
        sivTime.setOnClickListener(this);
        sivFeedback = (SettingItemView) findViewById(R.id.siv_feedback);
        sivFeedback.setOnClickListener(this);
        sivHelp = (SettingItemView) findViewById(R.id.siv_help);
        sivHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_app_lock:
                Intent intent = new Intent(SettingsActivity.this, AppLockActivity.class);
                startActivity(intent);
                break;
            case R.id.siv_mode:
                showModeDialog();
                break;
            case R.id.siv_cancel:
                cancelActiveAdmin();
                break;
            case R.id.siv_time:
                selectTime();
                break;
            case R.id.siv_feedback:
                showFeedbackDialog();
                break;
            case R.id.siv_help:
                showHelpDialog();
                break;
        }
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
                });
        builder.show();
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

    private String[] timelist = {"1min", "2min", "3min", "4min", "5min", "6min", "7min", "8min", "9min", "10min",
            "11min", "12min", "13min", "14min", "15min", "16min", "17min", "18min", "19min", "20min",
            "21min", "22min", "23min", "24min", "25min", "26min", "27min", "28min", "29min", "30min",
            "31min", "32min", "33min", "34min", "35min", "36min", "37min", "38min", "39min", "40min",
            "41min", "42min", "43min", "44min", "45min", "46min", "47min", "48min", "49min", "50min",
            "51min", "52min", "53min", "54min", "55min", "56min", "57min", "58min", "59min", "60min",

    };

    private void selectTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择控制时间")
                .setIcon(R.drawable.phone_small)
                .setItems(timelist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        sivTime.setDesc(timelist[which]);
                        settime(which);
                    }
                });
        builder.show();
    }

    private void settime(int time) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("time", time + 1).apply();
    }

    private void cancelActiveAdmin() {
        if (screenLock.isActive()) {
            screenLock.removeActiveAdmin();
        }
        Toast.makeText(SettingsActivity.this, "设备管理器已取消激活", Toast.LENGTH_SHORT).show();
    }


    private String[] modeList = {"锁屏模式", "应用锁模式"};

    private void showModeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择控制模式")
                .setIcon(R.drawable.phone_small)
                .setItems(modeList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        sivMode.setDesc(modeList[which]);
                        setMode(which);
                    }
                });
        builder.show();
    }

    private void setMode(int mode) {
        SharedPreferences.Editor edit = sp.edit();
        if (mode == 0) {
            if (!screenLock.isActive()) {
                screenLock.activeAdmin();
            } else {
                Toast.makeText(this, "设备管理器已激活", Toast.LENGTH_SHORT).show();
            }
            edit.putInt("mode", 0).apply();
        } else if (mode == 1) {
            edit.putInt("mode", 1).apply();
        }
    }
}
