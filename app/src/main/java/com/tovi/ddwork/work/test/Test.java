package com.tovi.ddwork.work.test;

import com.tovi.ddwork.work.adb.cmd;
import com.tovi.ddwork.work.takescreen.SendEMail;

import java.io.File;

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
                // test adb
                cmd.test(filePath);
                File file = new File(filePath);
                if (!file.exists()) {
                    System.out.println("System Test Error");
                    if (onTestListener != null) onTestListener.onTestRes("你的系统太牛逼了，头疼，你先root吧");
                    return;
                }
                System.out.println("System Test Success");
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
                    onTestListener.onTestRes("邮件发送失败，检查网络、邮箱设置、app权限设置");
                }
            }
        });
    }

    public interface OnTestListener {
        void onTestRes(String res);
    }
}
