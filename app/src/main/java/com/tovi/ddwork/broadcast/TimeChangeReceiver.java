package com.tovi.ddwork.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.adb.cmd;

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
        System.out.println("时间发生变化: week:" + week + " hour:" + hour + " minute:" + minute);

        // 0 为 周日
        if (week == 0 || week == 1) {
            return;
        }

        // offwork
        if (hour == 19 && minute == 1) {
            cmd.offWork(hour < 19);
        } else if (hour == 9 && minute == 36) {
            cmd.onWork();
        }
    }
}
