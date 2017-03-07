package com.tovi.ddwork.work;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.Util;
import com.tovi.ddwork.work.adb.cmd;
import com.tovi.ddwork.work.takescreen.SendEMail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class Work {
    public static void onWork(Context context) {
        new WorkThread(context, new WorkThread.OnWorkListener() {
            @Override
            public void toWork() {
                cmd.onWork(Config.LOCATIONS.get(Util.getHomeLocation()), onOKListener);
            }
        }).start();
    }

    public static void offWork(Context context) {
        new WorkThread(context, new WorkThread.OnWorkListener() {
            @Override
            public void toWork() {
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                cmd.offWork(hour < Config.OFF_WORK_HOUR, Config.LOCATIONS.get(Util.getHomeLocation()), onOKListener);
            }
        }).start();
    }

    private static void wakeUp(Context context) {
        // 唤醒屏幕(如果屏幕没有被唤醒)
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            System.out.println("唤醒屏幕");
            // 唤醒屏幕
            cmd.power();
        }
    }

    /**
     * unLock
     *
     * @param context
     */
    private static void unLock(Context context) {
        // 解锁(如果有锁)
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            System.out.println("解锁");
            // unlock
            cmd.unLock();
        }
    }

    /**
     * lock
     */
    private static void lock() {
        cmd.power();
    }

    private static final cmd.OnOKListener onOKListener = new cmd.OnOKListener() {
        @Override
        public void onOk(String type) {
            sendEmail(type);
        }

        @Override
        public void onGotoHome() {
            sendEmail("GotoHome");
        }
    };

    private static void sendEmail(String type) {
        // 是否需要发送邮件
        if (!Config.AUTO_SEND_EMAIL) {
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String fileName = String.format(".%s%s", date, type);
        final String filePath = String.format("/sdcard/Android/data/%s.png", fileName);
        cmd.takeScreen(filePath);
        SendEMail.send(filePath, String.format("%s-%s", date, type), new SendEMail.OnSendListener() {
            @Override
            public void onSendOk() {
                cmd.delFile(filePath);
            }

            @Override
            public void onSendError() {
                cmd.delFile(filePath);
            }
        });
    }

    static class WorkThread extends Thread {
        private static final String TAG = "WorkThread";
        private Context applicationContext;
        private OnWorkListener onWorkListener;

        public WorkThread(Context context, OnWorkListener onWorkListener) {
            if (context != null) {
                applicationContext = context.getApplicationContext();
            }
            this.onWorkListener = onWorkListener;
        }

        @Override
        public void run() {
            super.run();
            if (applicationContext != null) {
                Work.wakeUp(applicationContext);
                Work.unLock(applicationContext);
                Util.wakelock(applicationContext);
            } else {
                Log.e(TAG, "run: applicationContext is null");
            }
            if (onWorkListener != null) onWorkListener.toWork();
            Util.dormancy();
            Work.lock();
        }

        public interface OnWorkListener {
            void toWork();
        }
    }
}
