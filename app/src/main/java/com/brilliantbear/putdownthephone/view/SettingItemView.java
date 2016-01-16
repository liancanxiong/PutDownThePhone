package com.brilliantbear.putdownthephone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brilliantbear.putdownthephone.R;


/**
 * Created by Bear on 2016/1/5.
 */
public class SettingItemView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;
    private String title;
    private String desc;
    private boolean isCheckBoxShow;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        tvTitle.setText(title);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        tvDesc.setText(desc);
    }

    public boolean isCheckBoxShow() {
        return isCheckBoxShow;
    }

    public void setIsShow(boolean isShow) {
        this.isCheckBoxShow = isShow;
        if (isShow) {
            cbStatus.setVisibility(View.VISIBLE);
        } else {
            cbStatus.setVisibility(View.INVISIBLE);
        }
    }


    public boolean isChecked() {
        return cbStatus.isChecked();
    }

    public void setChecked(boolean isChecked) {
        if (isCheckBoxShow) {
            if (isChecked) {
                cbStatus.setChecked(true);
            } else {
                cbStatus.setChecked(false);
            }
        }
    }


    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView, defStyleAttr, 0);
        title = attributes.getString(R.styleable.SettingItemView_item_title);
        desc = attributes.getString(R.styleable.SettingItemView_item_desc);
        isCheckBoxShow = attributes.getBoolean(R.styleable.SettingItemView_show_checkbox, false);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);

        if (isCheckBoxShow) {
            cbStatus.setVisibility(View.VISIBLE);
        } else {
            cbStatus.setVisibility(View.INVISIBLE);
        }

        if (title != null) {
            tvTitle.setText(title);
        }

        if (desc != null) {
            tvDesc.setText(desc);
        }
    }
}
