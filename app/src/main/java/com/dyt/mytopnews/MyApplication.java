package com.dyt.mytopnews;

import android.app.Application;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.webkit.WebView;

import com.dyt.mytopnews.util.AdBlocker;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static Context context;
    private static WebView mWebView;

    public static Context getContext() {
        return context;
    }

    public static WebView getWebview() {
        return mWebView;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        AdBlocker.init(context);
        mWebView = new WebView(new MutableContextWrapper(this));
    }

}
