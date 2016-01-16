package com.brilliantbear.putdownthephone.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by Bear on 2016/1/12.
 */
public class AppInfo {

    private Drawable icon;
    private String appName;
    private boolean userApp;
    private String packageName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                ", appName='" + appName + '\'' +
                ", userApp=" + userApp +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
