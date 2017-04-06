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

    private static int getInt(String key, int defaultValue) {
        if (sp != null) {
            return sp.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    private static String getString(String key, String defaultValue) {
        if (sp != null) {
            return sp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        if (sp != null) {
            return sp.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    public static void setHomeLocation(Context context, int location) {
        initSharedPreferences(context);
        editor.putInt("location", location);
        editor.commit();
    }

    public static int getHomeLocation() {
        return getInt("location", 0);
    }

    public static void setTested(Context context, boolean test) {
        initSharedPreferences(context);
        editor.putBoolean("test", test);
        editor.commit();
    }

    public static boolean getTested() {
        return getBoolean("test", false);
    }

    public static void setWorkDate(Context context, String date, boolean isWorkDate) {
        initSharedPreferences(context);
        editor.putInt(date, isWorkDate ? 1 : 0);
        editor.putString("lastDate", date);
        editor.commit();
    }

    public static int isWorkDate(String date) {
        return getInt(date, -1);
    }

    public static String getLastDate() {
        return getString("lastDate", "");
    }

    public static void setRandomDelay(Context context, int randomDelay) {
        initSharedPreferences(context);
        editor.putInt("randomDelay", randomDelay);
        editor.commit();
    }

    public static int getRandomDelay() {
        return getInt("randomDelay", -1);
    }

    public static void setOnWorkTime(Context context, int hour, int minute) {
        initSharedPreferences(context);
        editor.putInt("onWorkHour", hour);
        editor.putInt("onWorkMinute", minute);
        editor.commit();
    }

    public static void setOffWorkTime(Context context, int hour, int minute) {
        initSharedPreferences(context);
        editor.putInt("offWorkHour", hour);
        editor.putInt("offWorkMinute", minute);
        editor.commit();
    }

    public static int getOnWorkHour() {
        return getInt("onWorkHour", Config.AUTO_ON_WORK_HOUR);
    }

    public static int getOnWorkMinute() {
        return getInt("onWorkMinute", Config.AUTO_ON_WORK_MINUTE);
    }

    public static int getOffWorkHour() {
        return getInt("offWorkHour", Config.AUTO_OFF_WORK_HOUR);
    }

    public static int getOffWorkMinute() {
        return getInt("offWorkMinute", Config.AUTO_OFF_WORK_MINUTE);
    }

}
