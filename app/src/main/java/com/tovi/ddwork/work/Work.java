package com.tovi.ddwork.work;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

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
    public static void onWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cmd.onWork(Config.LOCATIONS.get(Util.getHomeLocation()), onOKListener);
            }
        }).start();
    }

    public static void offWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                cmd.offWork(hour < Config.OFF_WORK_HOUR, Config.LOCATIONS.get(Util.getHomeLocation()), onOKListener);
            }
        }).start();
    }

    public static void wakeUp(Context context) {
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
    public static void unLock(Context context) {
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
    public static void lock() {
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
}
