package com.dyt.mytopnews.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dyt.mytopnews.MyApplication;
import com.dyt.mytopnews.gson.News;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static final String TAG = "Utility";

    //处理返回的json字符串，转换为java对象
    public static News handleNewsResponse(String response) {

        News news;
        if (response != null && !"".equals(response)) {
            Gson gson = new Gson();
            news = gson.fromJson(response, News.class);
            return news;
        }
        return null;


    }

    //调取上次最后浏览的页面数，以便每次打开不会浏览重复内容
    public static Integer getPageNumber() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        Integer lastPageNumber = preferences.getInt("lastPageNumber", 1);
        if (lastPageNumber > 200) {
            return 1;
        }
        return lastPageNumber;
    }

    //存储最后浏览的页码，以供调用
    public static void setPageNumber(Integer pageNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("lastPageNumber", pageNumber);
        edit.apply();
    }

    //将秒值转换为与当前时间差
    public static String parseTimeToDay(Long time) {
        long now = System.currentTimeMillis();
        long t = now - time * 1000L;
        Date date = new Date(t);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm", Locale.CHINA);
        return simpleDateFormat.format(date);
    }


}
