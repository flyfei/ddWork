package com.tovi.ddwork.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.tovi.ddwork.broadcast.TimeChangeReceiver;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AliveService extends Service {
    public static final String ACTION = "com.tovi.ddwork.AService";
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AliveService onCreate=====");
        // 保持屏幕常亮
        powerManager = (PowerManager) getSystemService(Service.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ddWork Lock");
        wakeLock.acquire();
        TimeChangeReceiver receiver = new TimeChangeReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }
}
