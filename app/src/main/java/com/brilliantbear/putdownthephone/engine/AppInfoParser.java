package com.brilliantbear.putdownthephone.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.brilliantbear.putdownthephone.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bear on 2016/1/12.
 */
public class AppInfoParser {
    public static List<AppInfo> getAppInfos(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<AppInfo> appInfos = new ArrayList<>();
        List<PackageInfo> packageinfos = packageManager.getInstalledPackages(0);

        for (PackageInfo p : packageinfos) {
            AppInfo appInfo = new AppInfo();
            appInfo.setPackageName(p.packageName);
            appInfo.setIcon(p.applicationInfo.loadIcon(packageManager));
            appInfo.setAppName(p.applicationInfo.loadLabel(packageManager).toString());

            int flags = p.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
                appInfo.setUserApp(false);
            } else {
                appInfo.setUserApp(true);
            }
            appInfos.add(appInfo);
            appInfo = null;
        }
        return appInfos;
    }
}
