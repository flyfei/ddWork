package com.tovi.ddwork.work;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.Util;
import com.tovi.ddwork.work.adb.cmd;
import com.tovi.ddwork.work.takescreen.SendEMail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

class Work {
    public static void onWork(Context context, final String tag) {
        System.out.println("onWorking===");
        new WorkThread(context, new WorkThread.OnWorkListener() {
            private List<String> filenames = new ArrayList<>();
            private String type = "onWork";

            @Override
            public void toWork() {
                cmd.onWork(Config.LOCATIONS.get(Util.getHomeLocation()), new cmd.OnOKListener() {
                    @Override
                    public void onOk() {
                        System.out.print(type);
                        String filename = takeScreen(type);
                        if (!TextUtils.isEmpty(filename)) filenames.add(filename);
                    }

                    @Override
                    public void onGotoHome() {
                        System.out.print("GotoHome");
                        String filename = takeScreen("GotoHome");
                        if (!TextUtils.isEmpty(filename)) filenames.add(filename);
                    }
                });
                sendEmail(filenames, type, tag);
            }
        }).start();
    }

    public static void offWork(Context context, final String tag) {
        System.out.println("offWorking===");
        new WorkThread(context, new WorkThread.OnWorkListener() {
            private List<String> filenames = new ArrayList<>();
            private String type = "offWork";

            @Override
            public void toWork() {
                cmd.offWork(false, Config.LOCATIONS.get(Util.getHomeLocation()), new cmd.OnOKListener() {
                    @Override
                    public void onOk() {
                        System.out.print(type);
                        String filename = takeScreen(type);
                        if (!TextUtils.isEmpty(filename)) filenames.add(filename);
                    }

                    @Override
                    public void onGotoHome() {
                        System.out.print("GotoHome");
                        String filename = takeScreen("GotoHome");
                        if (!TextUtils.isEmpty(filename)) filenames.add(filename);
                    }
                });
                sendEmail(filenames, type, tag);
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

    private static String takeScreen(String tag) {
        // 是否需要发送邮件
        if (!Config.AUTO_SEND_EMAIL) {
            return null;
        }
        String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date(System.currentTimeMillis()));
        final String filePath = String.format("/sdcard/Android/data/.%s%s.png", date, TextUtils.isEmpty(tag) ? "" : tag);
        cmd.takeScreen(filePath);
        return filePath;
    }

    private static void sendEmail(final List<String> fileNames, String tag, String body) {
        // 是否需要发送邮件
        if (!Config.AUTO_SEND_EMAIL) {
            return;
        }
        String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String subject = String.format("%s-%s", date, TextUtils.isEmpty(tag) ? "" : tag);
        SendEMail.send(fileNames, subject, body, new SendEMail.OnSendListener() {
            @Override
            public void onSendOk() {
                delFiles();
            }

            @Override
            public void onSendError() {
                delFiles();
            }

            private void delFiles() {
                if (fileNames != null && !fileNames.isEmpty()) {
                    for (String fileName : fileNames) {
                        cmd.delFile(fileName);
                    }
                }
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
