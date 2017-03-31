package com.tovi.ddwork.work;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class BaseHttp {
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 同步
     *
     * @param url
     */
    public static String sync(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步
     *
     * @param url
     * @param callback
     */
    public static void asyn(String url, final onHttpCallback callback) {
        if (TextUtils.isEmpty(url)) {
            if (callback != null) callback.onFailure();
            return;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                if (callback != null) callback.onResponse(response.body().string());
            }
        });
    }

    public interface onHttpCallback {
        void onFailure();

        void onResponse(String res);
    }
}
