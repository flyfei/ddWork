package com.tovi.ddwork.work.test;

import com.tovi.ddwork.work.adb.cmd;
import com.tovi.ddwork.work.takescreen.SendEMail;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Test {
    public static void start(final OnTestListener onTestListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                String filePath = "/sdcard/Android/" + currentTime + ".png";
                // test su
                boolean suRes = cmd.test(filePath);
                if (!suRes) {
                    if (onTestListener != null) onTestListener.onTestRes("超级权限获取失败");
                    return;
                }
                // test email
                testEmail(filePath, "程序测试" + currentTime, onTestListener);
            }
        }).start();
    }

    private static void testEmail(final String filePath, String subject, final OnTestListener onTestListener) {
        SendEMail.send(filePath, subject, new SendEMail.OnSendListener() {
            @Override
            public void onSendOk() {
                cmd.delFile(filePath);
                if (onTestListener != null) {
                    onTestListener.onTestRes(null);
                }
            }

            @Override
            public void onSendError() {
                cmd.delFile(filePath);
                if (onTestListener != null) {
                    onTestListener.onTestRes("邮件发送失败，请检查网络及设置");
                }
            }
        });
    }

    public interface OnTestListener {
        void onTestRes(String res);
    }
}
