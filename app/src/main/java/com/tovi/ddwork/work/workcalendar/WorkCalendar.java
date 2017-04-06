package com.tovi.ddwork.work.workcalendar;

import com.tovi.ddwork.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class WorkCalendar {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public static WorkDate getWorkDate() {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());

        int onWorkHour24 = Util.getOnWorkHour();
        int onWorkMinute = Util.getOnWorkMinute();
        int offWorkHour24 = Util.getOffWorkHour();
        int offWorkMinute = Util.getOffWorkMinute();

        Calendar calendar;


        Calendar onWorkCalendar = (Calendar) curCalendar.clone();
        onWorkCalendar.set(Calendar.HOUR_OF_DAY, onWorkHour24);
        onWorkCalendar.set(Calendar.MINUTE, onWorkMinute);
        onWorkCalendar.set(Calendar.SECOND, 0);
        onWorkCalendar.set(Calendar.MILLISECOND, 0);

        Calendar offWorkCalendar = (Calendar) curCalendar.clone();
        offWorkCalendar.set(Calendar.HOUR_OF_DAY, offWorkHour24);
        offWorkCalendar.set(Calendar.MINUTE, offWorkMinute);
        offWorkCalendar.set(Calendar.SECOND, 0);
        offWorkCalendar.set(Calendar.MILLISECOND, 0);


        boolean isOnWork;
        boolean isWorkDate = isWorkDate(curCalendar);
        if (isWorkDate && curCalendar.before(onWorkCalendar)) {
            calendar = onWorkCalendar;
            isOnWork = true;
            System.out.println("init cur on ========");
        } else if (isWorkDate && curCalendar.before(offWorkCalendar)) {
            calendar = offWorkCalendar;
            isOnWork = false;
            System.out.println("init cur off ========");
        } else {
            curCalendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar = findWorkDate(curCalendar);
            calendar.set(Calendar.HOUR_OF_DAY, onWorkHour24);
            calendar.set(Calendar.MINUTE, onWorkMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            isOnWork = true;
            System.out.println("init next on ========");
        }
        return new WorkDate(calendar, isOnWork);
    }

    private static Calendar findWorkDate(Calendar calendar) {
        boolean isWorkDate = isWorkDate(calendar);
        if (isWorkDate) {
            return calendar;
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return findWorkDate(calendar);
        }
    }

    private static boolean isWorkDate(Calendar calendar) {
        String dateString = format.format(calendar.getTime());
        int state = WebCalendar.isWorkDate(dateString);
        if (state == -1) {
            return DefaultCalendar.isWorkDate(calendar);
        }
        return state == 1;
    }
}
