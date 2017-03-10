package com.tovi.ddwork.work;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class Alarm {
    private static AlarmManager alarmMgr;

    public static void bindIntent(Context context, Calendar calendar, PendingIntent pendingIntent) {
        if (calendar == null) return;

        bindIntent(context, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void bindIntent(Context context, long millis, PendingIntent pendingIntent) {
        if (context == null || pendingIntent == null) return;

        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
            System.out.println("setExact");
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
            System.out.println("setRepeating");
        }
    }

    public static void cancel(PendingIntent pendingIntent) {
        if (alarmMgr != null && pendingIntent != null) {
            alarmMgr.cancel(pendingIntent);
        }
    }
}
