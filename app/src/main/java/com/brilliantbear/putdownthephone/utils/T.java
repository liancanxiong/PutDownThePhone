package com.brilliantbear.putdownthephone.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bear on 2016/1/14.
 */
public class T {
    private T() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;

    public static void showSingle(Context context, String s){
        if(toast==null){
            toast =Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime=System.currentTimeMillis();
        }else{
            twoTime=System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime> Toast.LENGTH_SHORT){
                    toast.show();
                }
            }else{
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }

    public static void showShort(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
