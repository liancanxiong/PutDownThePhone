package com.brilliantbear.putdownthephone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bear on 2016/1/13.
 */
public class AppLockDB {

    private final String DB_NAME = "putdownthephone.db";
    private final int VERSION_CODE = 1;
    private AppLockOpenHelper helper;

    public AppLockDB(Context context) {
        helper = new AppLockOpenHelper(context, DB_NAME, null, VERSION_CODE);
    }


    public void add(String packageName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename", packageName);
        db.insert("appinfo", null, values);
        db.close();
    }


    public void delete(String packageName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete("appinfo", "packagename=?", new String[]{packageName});
        db.close();
    }

    public boolean contain(String packageName) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("appinfo", null, "packagename=?", new String[]{packageName}, null, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<String> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("appinfo", new String[]{"packagename"}, null, null, null, null, null);
        List<String> packageNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            packageNames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return packageNames;
    }
}
