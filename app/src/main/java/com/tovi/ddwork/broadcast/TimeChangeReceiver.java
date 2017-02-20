package com.tovi.ddwork.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.work.AutoWork;
import com.tovi.ddwork.Config;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class TimeChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Config.AUTO_WORK) {
            AutoWork.Work();
        }
    }
}
