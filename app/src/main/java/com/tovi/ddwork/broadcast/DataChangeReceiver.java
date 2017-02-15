package com.tovi.ddwork.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.adb.cmd;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class DataChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar mClalender = Calendar.getInstance();
        mClalender.setTimeInMillis(System.currentTimeMillis());
        int hour = mClalender.get(Calendar.HOUR_OF_DAY);
        int minute = mClalender.get(Calendar.MINUTE);
        System.out.println("时间发生变化: hour:" + hour + " minute:" + minute);


        // offwork
        if (hour == 19 && minute == 10) {
            cmd.offWork(hour < 19);
        } else if (hour == 8 && minute == 50) {
            cmd.onWork();
        }
    }
}
