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
        if (isHeld()) return;

        wakeLock = ((PowerManager) context.getSystemService(Service.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ddWork Lock");
        wakeLock.acquire();
    }

    /**
     * 屏幕取消常亮
     */
    public static void dormancy() {
        if (isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    private static boolean isHeld() {
        return wakeLock != null && wakeLock.isHeld();
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

    public static void setTested(Context context, boolean test) {
        initSharedPreferences(context);
        editor.putBoolean("test", test);
        editor.commit();
    }

    public static boolean getTested() {
        if (sp != null) {
            return sp.getBoolean("test", false);
        }
        return false;
    }

    public static void setWorkDate(Context context, String date, boolean isWorkDate) {
        initSharedPreferences(context);
        editor.putInt(date, isWorkDate ? 1 : 0);
        editor.putString("lastDate", date);
        editor.commit();
    }

    public static int isWorkDate(String date) {
        if (sp != null) {
            return sp.getInt(date, -1);
        }
        return -1;
    }

    public static String getLastDate() {
        if (sp != null) {
            return sp.getString("lastDate", "");
        }
        return "";
    }

    public static void setRandomSize(Context context, int randomSize) {
        initSharedPreferences(context);
        editor.putInt("randomSize", randomSize);
        editor.commit();
    }

    public static int getRandomSize() {
        if (sp != null) {
            return sp.getInt("randomSize", -1);
        }
        return -1;
    }

}
