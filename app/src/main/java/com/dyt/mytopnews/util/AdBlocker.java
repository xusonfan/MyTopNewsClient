package com.dyt.mytopnews.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.WorkerThread;

public class AdBlocker {
    private static final String AD_HOSTS_FILE = "host.txt";
    private static final Set<String> AD_HOSTS = new HashSet<>();
    private static final String TAG = "AdBlocker";

    public static void init(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    loadFromAssets(context);
                } catch (IOException e) {
                    // noop
                }
                return null;
            }
        }.execute();
    }

    @WorkerThread
    private static void loadFromAssets(Context context) throws IOException {
        InputStream stream = context.getAssets().open(AD_HOSTS_FILE);
//        Log.d(TAG, "loadFromAssets: 执行了读取过滤文件操作");
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) AD_HOSTS.add(line);
        bufferedReader.close();
        inputStreamReader.close();
        stream.close();
//        Log.d(TAG, "loadFromAssets: 列表文件大小："+ AD_HOSTS.size());
    }

    public static boolean isAd(String url) {
        try {
//            Log.d(TAG, "isAd: 执行了广告判断");
            return isAdHost(getHost(url))||AD_HOSTS.contains(Uri.parse(url).getLastPathSegment());

        } catch (MalformedURLException e) {
            Log.d("AmniX", e.toString());
            return false;
        }

    }

    private static boolean isAdHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        int index = host.indexOf(".");
        return index >= 0 && (AD_HOSTS.contains(host) ||
                index + 1 < host.length() && isAdHost(host.substring(index + 1)));
    }

    public static String getHost(String url) throws MalformedURLException {
//        Log.d(TAG, "getHost: 执行了");
        return new URL(url).getHost();

    }

    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
    }


}
