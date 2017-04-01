package com.tovi.ddwork.work.workcalendar;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class CalendarUtil {

    /**
     * 得到两个日期相差的天数
     */
    public static int getBetweenDay(Calendar d1, Calendar d2) {
        if (d2.before(d1)) return _getBetweenDay(d2, d1);
        return _getBetweenDay(d1, d2);
    }

    private static int _getBetweenDay(Calendar d1, Calendar d2) {
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
//          d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
}
