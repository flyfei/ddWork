package com.tovi.ddwork.work.workdate;

import com.tovi.ddwork.Config;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class DefaultCalendar {

    static Calendar getWorkDate1() {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());
        int week = curCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = curCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar calendar;


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
        } else {
            calendar = onWorkCalendar;
            if (week == 5) {
                calendar.add(Calendar.DAY_OF_MONTH, 3);
            } else if (week == 6) {
                calendar.add(Calendar.DAY_OF_MONTH, 2);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            System.out.println("init next on ========");
        }
        return (Calendar) calendar.clone();
    }

    static Calendar getWorkDate() {
        System.out.println("use DefaultCalendar ========");
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());
        int week = curCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = curCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar calendar;


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

        if (!isWeekend(week) && curCalendar.before(onWorkCalendar)) {
            calendar = onWorkCalendar;
            System.out.println("init cur on ========");
        } else if (!isWeekend(week) && curCalendar.before(offWorkCalendar)) {
            calendar = offWorkCalendar;
            System.out.println("init cur off ========");
        } else {
            calendar = onWorkCalendar;
            if (week == 5) {
                calendar.add(Calendar.DAY_OF_MONTH, 3);
            } else if (week == 6) {
                calendar.add(Calendar.DAY_OF_MONTH, 2);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            System.out.println("init next on ========");
        }
        return (Calendar) calendar.clone();
    }

    private static boolean isWeekend(int week) {
        return week == 0 || week == 6;
    }

}
