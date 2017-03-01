package com.tovi.ddwork;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Util {
    private static PowerManager.WakeLock wakeLock = null;

    /**
     * 设置屏幕常亮
     *
     * @param context
     */
    public static void wakelock(Context context) {
        dormancy();
        wakeLock = ((PowerManager) context.getSystemService(Service.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ddWork Lock");
        wakeLock.acquire();
    }

    /**
     * 屏幕取消常亮
     */
    public static void dormancy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    /**
     * 屏幕是否亮着
     */
    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService(Service.POWER_SERVICE)).isScreenOn();
    }

    private static volatile SharedPreferences sp;
    private static volatile SharedPreferences.Editor editor;
    private static final String NAME = "ddwork";

    private static void initSharedPreferences(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
            editor = sp.edit();
        }
    }

    public static void setHomeLocation(Context context, int location) {
        initSharedPreferences(context);
        editor.putInt("location", location);
        editor.commit();
    }

    public static int getHomeLocation() {
        if (sp != null) {
            return sp.getInt("location", 0);
        }
        return 0;
    }

}
