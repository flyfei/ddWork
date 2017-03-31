package com.tovi.ddwork.work.workdate;

import android.content.Context;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class WorkCalendar {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public static void start(Context context) {
        SyncWorkDate.start(context);
    }

    public static void stop() {
        SyncWorkDate.stop();
    }

    public static void destroy() {
        SyncWorkDate.destroy();
    }

    public static Calendar getWorkDate() {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());


        // 获取打卡工作日（可能是当天）
        Calendar calendar = getLocalWorkDate(curCalendar);

        if (calendar != null) {
            // 当天
            if (format.format(calendar.getTime()).equals(format.format(curCalendar.getTime()))) {

                calendar = getCurDayWorkDate(curCalendar);

                if (calendar == null) { // 寻找下个工作日
                    curCalendar.add(Calendar.DAY_OF_MONTH, 1);

                    // calendar 可能为空
                    calendar = getLocalWorkDate(curCalendar);
                    if (calendar != null) {
                        calendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
                        calendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    } else {
                        System.out.println("error: calendar is null =========");
                    }
                }
            } else { // 非当天
                calendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
                calendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        }

        if (calendar != null)
            System.out.println("work time11111: " + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        if (calendar == null) calendar = DefaultCalendar.getWorkDate();

        return calendar;
    }

    public static void sync(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncWorkDate.sync(context);
            }
        }).start();
    }

    private static Calendar getLocalWorkDate(Calendar calendar) {
        String dateString = format.format(calendar.getTime());
        int state = Util.isWorkDate(dateString);
        if (state == 1) {
            System.out.println("use LocalWorkDate==============");
            return (Calendar) calendar.clone();
        } else if (state == 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return getLocalWorkDate(calendar);
        }
        return null;
    }

    private static Calendar getCurDayWorkDate(Calendar curCalendar) {
        Calendar calendar = null;


        Calendar onWorkCalendar = (Calendar) curCalendar.clone();
        onWorkCalendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_ON_WORK_HOUR);
        onWorkCalendar.set(Calendar.MINUTE, Config.AUTO_ON_WORK_MINUTE);
        onWorkCalendar.set(Calendar.SECOND, 0);
        onWorkCalendar.set(Calendar.MILLISECOND, 0);

        Calendar offWorkCalendar = (Calendar) curCalendar.clone();
        offWorkCalendar.set(Calendar.HOUR_OF_DAY, Config.AUTO_OFF_WORK_HOUR);
        offWorkCalendar.set(Calendar.MINUTE, Config.AUTO_OFF_WORK_MINUTE);
        offWorkCalendar.set(Calendar.SECOND, 0);
        offWorkCalendar.set(Calendar.MILLISECOND, 0);

        if (curCalendar.before(onWorkCalendar)) {
            calendar = onWorkCalendar;
            System.out.println("init cur on ========");
        } else if (curCalendar.before(offWorkCalendar)) {
            calendar = offWorkCalendar;
            System.out.println("init cur off ========");
        }
        return calendar;
    }

    private interface onSyncWorkDateCallback {
        void onGetWorkDate();
    }
}
