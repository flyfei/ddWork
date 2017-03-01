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

    public static void start(final Context context, int week, int hour, int minute) {
        // 0 为 周日
        if (week == 0 || week == 6) {
            return;
        }
        long curDate = new Date(0, 0, 0, hour, minute, 0).getTime();
        long onWorkDate = new Date(0, 0, 0, Config.AUTO_ON_WORK_HOUR, Config.AUTO_ON_WORK_MINUTE, 0).getTime();
        long offWorkDate = new Date(0, 0, 0, Config.AUTO_OFF_WORK_HOUR, Config.AUTO_OFF_WORK_MINUTE, 0).getTime();
        long chaDate = 1 * 60 * 60 * 1000;
        boolean isWork = false;
        if (onWorkDate - chaDate <= curDate && curDate <= onWorkDate) {
            isWork = true;
        }
        if (offWorkDate - chaDate <= curDate && curDate <= offWorkDate) {
            isWork = true;
        }
        if (!isWork) {
            return;
        }

        // 10 分钟同步一次
        if (minute % 10 != 0) {
            return;
        }
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


                int location = 0;
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

                if (Util.getHomeLocation() != location) {
                    System.out.println("update location");
                    Util.setHomeLocation(context.getApplicationContext(), location);
                    SendEMail.send(null, "Location update:" + location, null);
                }
            }
        });
    }
}
