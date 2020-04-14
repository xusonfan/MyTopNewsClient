package com.dyt.mytopnews.util;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {
    private static final String TAG = "HttpUtils";

    public static void getNewsList(String address, Callback callback) {
        Log.d(TAG, "getNewsList: " + address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
