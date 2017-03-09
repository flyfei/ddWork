package com.tovi.ddwork.work;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.broadcast.TimeChangeReceiver;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AutoWork {
    public static void Work(Context context, int week, int hour, int minute) {
        // 0 为 周日
        if (week == 0 || week == 6) {
            return;
        }

        // onWork
        if (hour == Config.AUTO_ON_WORK_HOUR && minute == Config.AUTO_ON_WORK_MINUTE) {
            Work.onWork(context);
            init(context);
        }
        // offwork
        if (hour == Config.AUTO_OFF_WORK_HOUR && minute == Config.AUTO_OFF_WORK_MINUTE) {
            Work.offWork(context);
            init(context);
        }
    }

    public static void init(Context context) {

        destroy();

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeChangeReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());
        int week = curCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = curCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar calendar = (Calendar) curCalendar.clone();


        Calendar onWorkCalendar = (Calendar) curCalendar.clone();
        onWorkCalendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
        onWorkCalendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);

        Calendar offWorkCalendar = (Calendar) curCalendar.clone();
        offWorkCalendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_OFF_WORK_HOUR);
        offWorkCalendar.set(Calendar.MINUTE, Config.AUTO_OFF_WORK_MINUTE);

        if (curCalendar.before(onWorkCalendar)) {
            calendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
            calendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);
            System.out.println("init cur on ========");
        } else if (curCalendar.before(offWorkCalendar)) {
            calendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_OFF_WORK_HOUR);
            calendar.set(Calendar.MINUTE, Config.AUTO_OFF_WORK_MINUTE);
            System.out.println("init cur off ========");
        } else {
            if (week == 5) {
                calendar.add(Calendar.DAY_OF_MONTH, 3);
            } else if (week == 6) {
                calendar.add(Calendar.DAY_OF_MONTH, 2);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
            calendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);
            System.out.println("init next on ========");
        }
        calendar.add(Calendar.MINUTE, -10);


        if (calendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) {
            alarmMgr.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                    1000 * 60 * 1, alarmIntent);
            System.out.println("at once start ========");
            return;
        }
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                1000 * 60 * 1, alarmIntent);
    }

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    public static void destroy() {
        if (alarmMgr != null && alarmIntent != null) {
            alarmMgr.cancel(alarmIntent);
        }
        alarmMgr = null;
    }
}
