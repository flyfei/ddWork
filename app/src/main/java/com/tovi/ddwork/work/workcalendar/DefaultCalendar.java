package com.tovi.ddwork.work.workcalendar;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class DefaultCalendar {
    public static boolean isWorkDate(Calendar calendar) {
        if (calendar != null) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return isWorkDate(dayOfWeek);
        }
        return false;
    }

    public static boolean isWorkDate(int dayOfWeek) {
        return dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY;
    }

}
