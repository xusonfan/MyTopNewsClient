package com.dyt.mytopnews.util;

import com.dyt.mytopnews.gson.News;
import com.google.gson.Gson;

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


    //将秒值转换为与当前时间差
    public static String parseTimeToDay(Long time) {
        long now = System.currentTimeMillis();
        long t = now - time * 1000L;
        long duration = t / 1000 / 60;
        if (duration < 60) {
            return duration + " 分钟前";
        } else {
            long hour = duration / 60;
            return hour + " 小时前";
        }

    }


}
