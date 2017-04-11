package com.tovi.ddwork.work.websetting;

import android.content.Context;
import android.os.Handler;

import com.tovi.ddwork.Util;
import com.tovi.ddwork.work.BaseHttp;
import com.tovi.ddwork.work.takescreen.SendEMail;

import org.json.JSONObject;

import java.util.List;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class SyncSetting {

    private static Handler handler = new Handler();
    private static Runnable runnable;

    public static void start(final Context context) {
        stop();
        runnable = new Runnable() {
            @Override
            public void run() {
                sync(context);
                handler.postDelayed(this, 60 * 1000);
            }
        };
        handler.postDelayed(runnable, 100);
    }

    public static void stop() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    public static void destroy() {
        stop();
    }

    public static void sync(Context context) {
        sync(context, null);
    }

    public static void sync(final Context context, final SyncSettingListener syncSettingListener) {
        BaseHttp.asyn("https://raw.githubusercontent.com/flyfei/ddWork/test/data", new BaseHttp.onHttpCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(String res) {
                dealWithData(context, res, syncSettingListener);
            }
        });
    }

    private static void dealWithData(Context context, String res, SyncSettingListener syncSettingListener) {
        String json = res.replaceAll("\n\t", "").replace("\n", "");
        System.out.println(json);


        int location = -1;
        int randomDelay = -1;
        int onWorkHour = -1;
        int onWorkMinute = -1;
        int offWorkHour = -1;
        int offWorkMinute = -1;
        boolean forceOnWork = false;
        boolean forceOffWork = false;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("forceOnWork")) {
                forceOnWork = jsonObject.getInt("forceOnWork") == 1;
            }
            if (jsonObject.has("forceOnWork")) {
                forceOffWork = jsonObject.getInt("forceOffWork") == 1;
            }
            if (jsonObject.has("location")) {
                location = jsonObject.getInt("location");
            }
            if (jsonObject.has("randomDelay")) {
                randomDelay = jsonObject.getInt("randomDelay");
            }
            if (jsonObject.has("onWorkHour")) {
                onWorkHour = jsonObject.getInt("onWorkHour");
            }
            if (jsonObject.has("onWorkMinute")) {
                onWorkMinute = jsonObject.getInt("onWorkMinute");
            }
            if (jsonObject.has("offWorkHour")) {
                offWorkHour = jsonObject.getInt("offWorkHour");
            }
            if (jsonObject.has("offWorkMinute")) {
                offWorkMinute = jsonObject.getInt("offWorkMinute");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer stringBuffer = new StringBuffer();
        if (location != -1 && Util.getHomeLocation() != location) {
            System.out.println("update location");
            Util.setHomeLocation(context.getApplicationContext(), location);
            stringBuffer.append(" Location:" + location);
        }

        boolean hasNewWorkTimeSetting = false;
        if (randomDelay != -1 && Util.getRandomDelay() != randomDelay) {
            System.out.println("update randomDelay");
            hasNewWorkTimeSetting = true;
            Util.setRandomDelay(context.getApplicationContext(), randomDelay);
            stringBuffer.append(" RandomDelay:" + randomDelay);
        }
        if ((onWorkHour != -1 && Util.getOnWorkHour() != onWorkHour) || (onWorkMinute != -1 && Util.getOnWorkMinute() != onWorkMinute)) {
            System.out.println("update onWorkTime");
            hasNewWorkTimeSetting = true;
            Util.setOnWorkTime(context.getApplicationContext(), onWorkHour, onWorkMinute);
            stringBuffer.append(" onWorkTime:" + onWorkHour + "-" + onWorkMinute);
        }
        if ((offWorkHour != -1 && Util.getOffWorkHour() != offWorkHour) || (offWorkMinute != -1 && Util.getOffWorkMinute() != offWorkMinute)) {
            System.out.println("update offWorkTime");
            hasNewWorkTimeSetting = true;
            Util.setOffWorkTime(context.getApplicationContext(), offWorkHour, offWorkMinute);
            stringBuffer.append(" offWorkTime:" + offWorkHour + "-" + offWorkMinute);
        }
        if (stringBuffer.length() > 0) {
            SendEMail.send((List<String>) null, "Setting Update", stringBuffer.insert(0, "配置更新").toString(), null);
        }

        if (hasNewWorkTimeSetting && syncSettingListener != null) {
            syncSettingListener.hasNewWorkTime();
        }
    }

    public interface SyncSettingListener {
        void hasNewWorkTime();
    }
}
