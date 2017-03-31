package com.tovi.ddwork.work;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.receiver.AlarmReceiver;
import com.tovi.ddwork.work.takescreen.SendEMail;
import com.tovi.ddwork.work.workdate.WorkCalendar;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AutoWork {
    private static final int ACTION = 100;
    private static final String WORK_TYPE = "WORK_TYPE";
    private static final String WORK_TYPE_ON_WORK = "ON_WORK";
    private static final String WORK_TYPE_OFF_WORK = "OFF_WORK";

    public static void work(Context context, Intent intent) {
        String workType = intent.getStringExtra(WORK_TYPE);
        // onWork
        if (workType.equals(WORK_TYPE_ON_WORK)) {
            Work.onWork(context);
            init(context);
        }
        // offWork
        if (workType.equals(WORK_TYPE_OFF_WORK)) {
            Work.offWork(context);
            init(context);
        }
    }

    public static void init(Context context) {

        destroy();

        Calendar calendar = WorkCalendar.getWorkDate();

        // sync
        Synchronization.init(context, (Calendar) calendar.clone());

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.WORK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isOnWork = hour == Config.AUTO_ON_WORK_HOUR;
        boolean isOffWork = hour == Config.AUTO_OFF_WORK_HOUR;
        String type = isOnWork ? WORK_TYPE_ON_WORK : (isOffWork ? WORK_TYPE_OFF_WORK : "");
        intent.putExtra(WORK_TYPE, type);

        alarmIntent = PendingIntent.getBroadcast(context, ACTION, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        System.out.println("work time: " + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "  type:" + type);

        SendEMail.send(null, "打卡时间设置为:" + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "  type:" + type, null);
        Alarm.bindIntent(context, calendar, alarmIntent);
    }

    private static PendingIntent alarmIntent;

    public static void destroy() {
        Alarm.cancel(alarmIntent);
        Synchronization.destroy();
    }
}
