package com.tovi.ddwork.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tovi.ddwork.work.AutoWork;
import com.tovi.ddwork.work.Synchronization;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TYPE = "TYPE";
    public static final String SYNC = "SYNC";
    public static final String WORK = "WORK";

    @Override
    public void onReceive(Context context, Intent intent) {

        String type = intent.getStringExtra(TYPE);
        System.out.println("AlarmReceiver type:" + type);
        if (TextUtils.isEmpty(type)) return;

        if (SYNC.equals(type)) {
            Synchronization.start(context);
        } else if (WORK.equals(type)) {
            AutoWork.work(context, intent);
        } else {
            System.out.println("unkown type");
        }
    }
}
