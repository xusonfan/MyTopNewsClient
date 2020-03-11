/**
 * Copyright 2019 bejson.com
 */
package com.dyt.mytopnews.gson;

import java.io.Serializable;

/**
 * Auto-generated: 2019-11-24 19:17:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data implements Serializable {

    private static final long serialVersionUID = -2264309095089454796L;
    private String CreateTime;
    private String Title;
    private String Url;
    private String id;
    private String desc;
    private String tid;
    private String type;

    @Override
    public String toString() {
        return "Data{" +
                "CreateTime='" + CreateTime + '\'' +
                ", Title='" + Title + '\'' +
                ", Url='" + Url + '\'' +
                ", id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", tid='" + tid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}