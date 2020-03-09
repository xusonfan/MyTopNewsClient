/**
 * Copyright 2019 bejson.com
 */
package com.dyt.mytopnews.gson;
import java.util.List;

/**
 * Auto-generated: 2019-11-24 19:17:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class News {

    private int Code;
    private String Message;
    private List<Data> Data;
    private int Page;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<Data> getData() {
        return Data;
    }

    public void setData(List<Data> data) {
        Data = data;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    @Override
    public String toString() {
        return "News{" +
                "Code=" + Code +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                ", Page=" + Page +
                '}';
    }
}