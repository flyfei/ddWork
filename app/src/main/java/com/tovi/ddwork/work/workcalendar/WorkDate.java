package com.tovi.ddwork.work.workcalendar;

import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class WorkDate {
    Calendar calendar;
    boolean isOnWork;

    WorkDate() {

    }

    public WorkDate(Calendar calendar, boolean isOnWork) {
        this.calendar = calendar;
        this.isOnWork = isOnWork;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean isOnWork() {
        return isOnWork;
    }
}
