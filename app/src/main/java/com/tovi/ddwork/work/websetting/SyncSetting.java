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

    public static void sync(final Context context) {
        BaseHttp.asyn("https://raw.githubusercontent.com/flyfei/ddWork/test/data", new BaseHttp.onHttpCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(String res) {
                dealWithData(context, res);
            }
        });
    }

    private static void dealWithData(Context context, String res) {
        String json = res.replaceAll("\n\t", "").replace("\n", "");
        System.out.println(json);


        int location = -1;
        int randomDelay = -1;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(location + " " + forceOnWork + " " + forceOffWork + "  randomDelay:" + randomDelay);

        StringBuffer stringBuffer = new StringBuffer();
        if (location != -1 && Util.getHomeLocation() != location) {
            System.out.println("update location");
            Util.setHomeLocation(context.getApplicationContext(), location);
            stringBuffer.append(" Location:" + location);
        }

        if (randomDelay != -1 && Util.getRandomDelay() != randomDelay) {
            System.out.println("update randomDelay");
            Util.setRandomDelay(context.getApplicationContext(), randomDelay);
            stringBuffer.append(" RandomDelay:" + randomDelay);
        }
        if (stringBuffer.length() > 0) {
            SendEMail.send((List<String>) null, stringBuffer.insert(0, "Setting Update:").toString(), "配置更新", null);
        }
    }
}
