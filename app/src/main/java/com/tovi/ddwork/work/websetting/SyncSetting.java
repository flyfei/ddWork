package com.tovi.ddwork.work.websetting;

import android.content.Context;
import android.os.Handler;

import com.tovi.ddwork.Util;
import com.tovi.ddwork.work.BaseHttp;
import com.tovi.ddwork.work.takescreen.SendEMail;

import org.json.JSONObject;

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
        boolean forceOnWork = false;
        boolean forceOffWork = false;
        try {
            JSONObject jsonObject = new JSONObject(json);
            forceOnWork = jsonObject.getInt("forceOnWork") == 1;
            forceOffWork = jsonObject.getInt("forceOffWork") == 1;
            location = jsonObject.getInt("location");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(location + " " + forceOnWork + " " + forceOffWork);

        if (location == -1) return;

        if (Util.getHomeLocation() != location) {
            System.out.println("update location");
            Util.setHomeLocation(context.getApplicationContext(), location);
            SendEMail.send(null, "Location update:" + location, null);
        }
    }
}
