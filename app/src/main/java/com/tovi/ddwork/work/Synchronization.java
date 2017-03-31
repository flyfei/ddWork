package com.tovi.ddwork.work;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.receiver.AlarmReceiver;
import com.tovi.ddwork.work.websetting.SyncSetting;
import com.tovi.ddwork.work.workdate.WorkCalendar;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Synchronization {
    private static final int ACTION = 101;
    private static PendingIntent alarmIntent;

    public static void init(Context context, Calendar calendar) {

        if (context == null || calendar == null) return;
        calendar.add(Calendar.MINUTE, -5);

        destroy();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.SYNC);
        alarmIntent = PendingIntent.getBroadcast(context, ACTION, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Alarm.bindIntent(context, System.currentTimeMillis(), alarmIntent);
            System.out.println("at once start ========");
        } else {
            Alarm.bindIntent(context, calendar.getTimeInMillis(), alarmIntent);
        }
    }

    public static void start(Context context) {
        SyncSetting.start(context);
        WorkCalendar.start(context);
    }

    public static void stop() {
        SyncSetting.stop();
        WorkCalendar.stop();
    }

    public static void destroy() {
        Alarm.cancel(alarmIntent);
        SyncSetting.destroy();
        WorkCalendar.destroy();
    }

    public static void sync(Context context) {
        SyncSetting.sync(context);
        WorkCalendar.sync(context);
    }
}
