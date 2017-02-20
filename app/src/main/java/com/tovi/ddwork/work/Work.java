package com.tovi.ddwork.work;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.work.adb.cmd;
import com.tovi.ddwork.work.takescreen.SendEMail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Work {
    public static void onWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cmd.onWork(onOKListener);
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
                cmd.offWork(hour < Config.OFF_WORK_HOUR, onOKListener);
            }
        }).start();
    }

    private static cmd.OnOKListener onOKListener = new cmd.OnOKListener() {
        @Override
        public void onOk(String type) {
            // 是否需要发送邮件
            if (!Config.AUTO_SEND_EMAIL) {
                return;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date(System.currentTimeMillis()));
            String fileName = String.format(".%s%s", date, type);
            final String filePath = String.format("/sdcard/Android/data/%s.png", fileName);
            cmd.takeScreen(filePath);
            SendEMail.send(filePath, String.format("%s%s", date, type), new SendEMail.OnSendListener() {
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
    };
}
