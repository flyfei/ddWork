package com.tovi.ddwork.work;

import com.tovi.ddwork.Config;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AutoWork {
    public static void Work() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        int week = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println("时间发生变化: week:" + week + " hour:" + hour + " minute:" + minute);

        // 0 为 周日
        if (week == 0 || week == 6) {
            return;
        }

        // onWork
        if (hour == Config.AUTO_ON_WORK_HOUR && minute == Config.AUTO_ON_WORK_MINUTE) {
            Work.onWork();
        }
        // offwork
        if (hour == Config.AUTO_OFF_WORK_HOUR && minute == Config.AUTO_OFF_WORK_MINUTE) {
            Work.offWork();
        }
    }
}
