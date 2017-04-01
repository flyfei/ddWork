package com.tovi.ddwork.work.workcalendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class DefaultCalendar {
    public static boolean isWorkDate(int dayOfWeek) {
        return dayOfWeek != 0 && dayOfWeek != 6;
    }
}
