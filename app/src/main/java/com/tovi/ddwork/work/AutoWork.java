package com.tovi.ddwork.work;

import android.content.Context;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.Util;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class AutoWork {
    public static void Work(Context context, int week, int hour, int minute) {
        // 0 为 周日
        if (week == 0 || week == 6) {
            return;
        }

        // onWork
        if (hour == Config.AUTO_ON_WORK_HOUR && minute == Config.AUTO_ON_WORK_MINUTE) {
            Work.wakeUp(context);
            Work.unLock(context);
            Util.wakelock(context);
            Work.onWork();
            Util.dormancy();
            Work.lock();
        }
        // offwork
        if (hour == Config.AUTO_OFF_WORK_HOUR && minute == Config.AUTO_OFF_WORK_MINUTE) {
            Work.wakeUp(context);
            Work.unLock(context);
            Util.wakelock(context);
            Work.offWork();
            Util.dormancy();
            Work.lock();
        }
    }
}
