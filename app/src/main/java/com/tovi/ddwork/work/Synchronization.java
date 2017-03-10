package com.tovi.ddwork.work;

import android.content.Context;

import com.tovi.ddwork.Config;
import com.tovi.ddwork.Util;
import com.tovi.ddwork.work.takescreen.SendEMail;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Synchronization {
    private static final OkHttpClient client = new OkHttpClient();

    public static void start(Context context, int week, int hour, int minute) {
        if (!checkWeek(week)) return;

        long curTime = new Date(0, 0, 0, hour, minute, 0).getTime();
        long onWorkTime = new Date(0, 0, 0, Config.AUTO_ON_WORK_HOUR, Config.AUTO_ON_WORK_MINUTE, 0).getTime();
        long offWorkTime = new Date(0, 0, 0, Config.AUTO_OFF_WORK_HOUR, Config.AUTO_OFF_WORK_MINUTE, 0).getTime();
        long frameSize = 1 * 60 * 60 * 1000; // work前一小时内执行


        int minuteSize = 10; // 10分钟执行一次 (在时间段范围内)
        long beforeSize = 2 * 60 * 1000; // work前2分钟执行一次
        if ((checkTimeFrame(curTime, onWorkTime, onWorkTime, frameSize) && checkMinute(minute, minuteSize)) || isBefore(curTime, onWorkTime, offWorkTime, beforeSize)) {
            sync(context);
        }
    }

    public static boolean checkWeek(int week) {
        // 0 为 周日
        if (week == 0 || week == 6) {
            return false;
        }
        return true;
    }

    public static boolean checkTimeFrame(long curTime, long onWorkTime, long offWorkTime, long frameSize) {
        if (onWorkTime - frameSize <= curTime && curTime <= onWorkTime) {
            return true;
        }
        if (offWorkTime - frameSize <= curTime && curTime <= offWorkTime) {
            return true;
        }
        return false;
    }

    public static boolean checkMinute(int curMinute, int minuteSize) {
        if (minuteSize <= 0) return false;
        return curMinute % minuteSize == 0;
    }

    public static boolean isBefore(long curTime, long onWorkTime, long offWorkTime, long beforeSize) {
        return curTime + beforeSize == onWorkTime || curTime + beforeSize == offWorkTime;
    }


    public static void sync(final Context context) {
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/flyfei/ddWork/test/data")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String json = response.body().string().replaceAll("\n\t", "").replace("\n", "");
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
        });
    }
}
