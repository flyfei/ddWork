package com.tovi.ddwork.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tovi.ddwork.work.AutoWork;
import com.tovi.ddwork.work.takescreen.SendEMail;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AliveService extends Service {
    public static final String ACTION = "com.tovi.ddwork.AService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AliveService onCreate=====");
        String tag = AutoWork.init(this);
        SendEMail.send("服务启动", tag, null);
    }

    @Override
    public void onDestroy() {
        System.out.println("AliveService onDestroy=====");
        AutoWork.destroy();
        super.onDestroy();
    }
}
