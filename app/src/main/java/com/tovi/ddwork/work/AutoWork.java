package com.tovi.ddwork.work;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.receiver.AlarmReceiver;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AutoWork {
    private static final int ACTION = 100;
    private static final String WORK_WEEK = "WORK_WEEK";
    private static final String WORK_HOUR = "WORK_HOUR";
    private static final String WORK_MINUTE = "WORK_MINUTE";

    public static void work(Context context, Intent intent) {
        int hour = intent.getIntExtra(WORK_HOUR, -1);
        int week = intent.getIntExtra(WORK_WEEK, -1);
        int minute = intent.getIntExtra(WORK_MINUTE, -1);
        work(context, week, hour, minute);
    }

    private static void work(Context context, int week, int hour, int minute) {
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


        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);
        int week = curCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = curCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar calendar = (Calendar) curCalendar.clone();


        Calendar onWorkCalendar = (Calendar) curCalendar.clone();
        onWorkCalendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
        onWorkCalendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);

        Calendar offWorkCalendar = (Calendar) curCalendar.clone();
        offWorkCalendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_OFF_WORK_HOUR);
        offWorkCalendar.set(Calendar.MINUTE, Config.AUTO_OFF_WORK_MINUTE);

        if (!isWeekend(week) && curCalendar.before(onWorkCalendar)) {
            calendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
            calendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);
            System.out.println("init cur on ========");
        } else if (!isWeekend(week) && curCalendar.before(offWorkCalendar)) {
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

        // sync
        Synchronization.init(context, (Calendar) calendar.clone(), (Calendar) curCalendar.clone());

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.WORK);
        intent.putExtra(WORK_WEEK, calendar.get(Calendar.DAY_OF_WEEK) - 1);
        intent.putExtra(WORK_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        intent.putExtra(WORK_MINUTE, calendar.get(Calendar.MINUTE));

        alarmIntent = PendingIntent.getBroadcast(context, ACTION, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        System.out.println("work time: " + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        Alarm.bindIntent(context, calendar, alarmIntent);
    }

    private static boolean isWeekend(int week) {
        return week == 0 || week == 6;
    }

    private static PendingIntent alarmIntent;

    public static void destroy() {
        Alarm.cancel(alarmIntent);
        Synchronization.destroy();
    }
}
