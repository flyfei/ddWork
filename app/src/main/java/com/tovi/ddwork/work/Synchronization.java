package com.tovi.ddwork.work;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.tovi.ddwork.Util;
import com.tovi.ddwork.receiver.AlarmReceiver;
import com.tovi.ddwork.work.takescreen.SendEMail;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Synchronization {
    private static final int ACTION = 101;
    private static PendingIntent alarmIntent;

    private static final OkHttpClient client = new OkHttpClient();

    public static void init(Context context, Calendar calendar, Calendar curCalendar) {

        if (context == null || calendar == null || curCalendar == null) return;
        calendar.add(Calendar.MINUTE, -5);

        destroy();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.SYNC);
        alarmIntent = PendingIntent.getBroadcast(context, ACTION, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (calendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) {
            Alarm.bindIntent(context, System.currentTimeMillis(), alarmIntent);
            System.out.println("at once start ========");
        } else {
            Alarm.bindIntent(context, calendar.getTimeInMillis(), alarmIntent);
        }
    }

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
        Alarm.cancel(alarmIntent);
        stop();
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
