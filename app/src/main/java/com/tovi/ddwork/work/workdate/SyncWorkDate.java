package com.tovi.ddwork.work.workdate;

import android.content.Context;
import android.text.TextUtils;

import com.tovi.ddwork.Util;
import com.tovi.ddwork.work.BaseHttp;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class SyncWorkDate {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static Calendar checkCalendar;
    private static int newWorkDateCount = 0;
    private static SyncWorkDateThread thread;

    static void start(Context context) {
        start(context, 0);
    }

    private static void start(Context context, long time) {
        stop();
        init();
        thread = new SyncWorkDateThread(context, time);
        thread.start();
    }

    static void stop() {
        if (thread != null) {
            thread.close = true;
            thread.interrupt();
            thread = null;
        }
    }

    static void destroy() {
        stop();
    }

    static void sync(Context context) {
        start(context, 5 * 1000);
    }

    private static STATE isWorkDate(String date) {
        String res = BaseHttp.sync(String.format("http://www.easybots.cn/api/holiday.php?d=%s", date));
        STATE state = STATE.UNKNOWN;
        if (!TextUtils.isEmpty(res)) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getString(date).equals("0")) {
                    state = STATE.IS_WORK;
                } else {
                    state = STATE.NO_WORK;
                }
            } catch (Exception e) {
                e.printStackTrace();
                state = STATE.ERROR;
            }
        }
        System.out.println("isWorkDate: data:" + date + "  res:" + res + "  state:" + state);
        return state;
    }

    private static void init() {

        String date = Util.getLastDate();
        System.out.println("init get lastDate:" + date);
        checkCalendar = Calendar.getInstance();
        checkCalendar.setTimeInMillis(System.currentTimeMillis());

        if (!TextUtils.isEmpty(date)) {
            try {
                checkCalendar.setTime(format.parse(date));
                checkCalendar.add(Calendar.DAY_OF_MONTH, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newWorkDateCount = 0;
        System.out.println("SyncWorkDate: init checkCalendar:"  + checkCalendar.get(Calendar.DAY_OF_MONTH) + " - " + checkCalendar.get(Calendar.HOUR_OF_DAY) + ":" + checkCalendar.get(Calendar.MINUTE));
    }

    private static boolean checkDate(Context context, Calendar calendar) {
        String dateString = format.format(calendar.getTime());
        STATE state = isWorkDate(dateString);
        if (state == STATE.IS_WORK || state == STATE.NO_WORK) {
            saveDate(context, dateString, state == STATE.IS_WORK);
            return true;
        }
        return false;
    }

    private static void saveDate(Context context, String dataString, boolean isWork) {
        Util.setWorkDate(context, dataString, isWork);
        if (isWork) newWorkDateCount++;
        System.out.println("get share date:" + dataString + "  res:" + Util.isWorkDate(dataString));
    }


    enum STATE {
        UNKNOWN,
        ERROR,
        IS_WORK,
        NO_WORK
    }

    static class SyncWorkDateThread extends Thread {
        private static final String TAG = "SyncWorkDateThread";
        private Context applicationContext;
        private boolean close = false;
        private long time = 60 * 1000;

        public SyncWorkDateThread(Context context) {
            if (context != null) {
                applicationContext = context.getApplicationContext();
            }
        }

        public SyncWorkDateThread(Context context, long time) {
            if (context != null) {
                applicationContext = context.getApplicationContext();
            }
            if (time > 0) this.time = time;
        }

        @Override
        public void run() {
            while (newWorkDateCount <= 1 && !close) {
                if (checkDate(applicationContext, checkCalendar)) {
                    checkCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    System.out.println("检查成功 加一天");
                }
                System.out.println("newWorkDateCount:" + newWorkDateCount);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
