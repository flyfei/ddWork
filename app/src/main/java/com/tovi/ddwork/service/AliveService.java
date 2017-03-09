package com.tovi.ddwork.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tovi.ddwork.work.AutoWork;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AliveService extends Service {
    public static final String ACTION = "com.tovi.ddwork.AService";
//    private TimeChangeReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AliveService onCreate=====");
//        receiver = new TimeChangeReceiver();
//        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        AutoWork.init(this);
    }

    @Override
    public void onDestroy() {
        System.out.println("AliveService onDestroy=====");
//        unregisterReceiver(receiver);
        AutoWork.destroy();
        super.onDestroy();
    }
}
