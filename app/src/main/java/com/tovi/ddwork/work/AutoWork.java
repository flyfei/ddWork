package com.tovi.ddwork.work;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tovi.ddwork.Util;
import com.tovi.ddwork.receiver.AlarmReceiver;
import com.tovi.ddwork.work.workcalendar.WorkCalendar;
import com.tovi.ddwork.work.workcalendar.WorkDate;

import java.util.Calendar;
import java.util.Random;

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
            String tag = init(context);
            Work.onWork(context, tag);
        }
        // offWork
        if (workType.equals(WORK_TYPE_OFF_WORK)) {
            String tag = init(context);
            Work.offWork(context, tag);
        }
    }

    public static String init(Context context) {

        destroy();

        WorkDate workDate = WorkCalendar.getWorkDate();
        Calendar calendar = workDate.getCalendar();
        String workType = workDate.isOnWork() ? WORK_TYPE_ON_WORK : WORK_TYPE_OFF_WORK;

        // 设置误差时间
        randomMinute(calendar);

        // sync
        Synchronization.init(context, (Calendar) calendar.clone());

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.WORK);
        intent.putExtra(WORK_TYPE, workType);

        alarmIntent = PendingIntent.getBroadcast(context, ACTION, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String tag = String.format("下次打卡(%s)时间为:%s-%s:%s", workType, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        System.out.println(tag);

        Alarm.bindIntent(context, calendar, alarmIntent);

        return tag;
    }

    private static void randomMinute(Calendar calendar) {
        int randomDelay = Util.getRandomDelay();
        if (randomDelay <= 0) return;
        calendar.add(Calendar.MINUTE, new Random().nextInt(randomDelay));
    }

    private static PendingIntent alarmIntent;

    public static void destroy() {
        Alarm.cancel(alarmIntent);
        Synchronization.destroy();
    }
}
