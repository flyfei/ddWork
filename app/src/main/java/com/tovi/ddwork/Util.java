package com.tovi.ddwork;

import android.app.Service;
import android.content.Context;
import android.os.PowerManager;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Util {
    private static PowerManager.WakeLock wakeLock = null;

    public static void wakelock(Context context) {
        dormancy();
        wakeLock = ((PowerManager) context.getSystemService(Service.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ddWork Lock");
        wakeLock.acquire();
    }

    public static void dormancy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
