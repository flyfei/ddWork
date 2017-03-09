package com.tovi.ddwork.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.work.AutoWork;
import com.tovi.ddwork.work.Synchronization;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class TimeChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        int week = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println("TimeChange: week:" + week + " hour:" + hour + " minute:" + minute);

        Synchronization.sync(context);
        if (Config.AUTO_WORK) {
            AutoWork.Work(context, week, hour, minute);
        }
    }
}
