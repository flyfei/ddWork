package com.tovi.ddwork.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tovi.ddwork.work.AutoWork;
import com.tovi.ddwork.work.takescreen.SendEMail;
import com.tovi.ddwork.work.websetting.SyncSetting;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AliveService extends Service implements SyncSetting.SyncSettingListener {
    public static final String ACTION = "com.tovi.ddwork.AService";
    private static boolean isFirst = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AliveService onCreate=====");
        init();
    }

    private void init() {
        String tag = AutoWork.init(this);
        SendEMail.send("服务启动", tag, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("AliveService onStartCommand=====" + isFirst);
        if (!isFirst) {
            SyncSetting.sync(this, this);
        } else {
            isFirst = false;
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("AliveService onDestroy=====");
        AutoWork.destroy();
        super.onDestroy();
    }

    @Override
    public void hasNewWorkTime() {
        init();
    }
}
