package com.dyt.mytopnews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.Color;
import android.webkit.WebView;

import com.billy.android.swipe.SmartSwipeBack;

import org.litepal.LitePal;

import static com.billy.android.swipe.SwipeConsumer.DIRECTION_LEFT;

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
        mWebView = new WebView(new MutableContextWrapper(this));

        //添加界面滑动返回功能
        SmartSwipeBack.activitySlidingBack(this, new SmartSwipeBack.ActivitySwipeBackFilter() {
            @Override
            public boolean onFilter(Activity activity) {
                return !(activity instanceof MainActivity);
            }
        }, 0, Color.TRANSPARENT, 0x80000000, 10, 0.5F, DIRECTION_LEFT);

    }

}
