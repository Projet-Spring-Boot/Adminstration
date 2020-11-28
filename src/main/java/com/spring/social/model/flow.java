package com.spring.social.model;

import java.io.Serializable;
import java.util.Date;

public class flow implements Serializable {

    private int id;
    private String user_img;
    private String user_name;
    private Date publishing;
    private String published_content;
    private String published_media;
    private String source;


    public flow(String user_img, String user_name, Date publishing, String published_content, String published_media, String source) {
        this.user_img = user_img;
        this.user_name = user_name;
        this.publishing = publishing;
        this.published_content = published_content;
        this.published_media = published_media;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Date getPublishing() {
        return publishing;
    }

    public void setPublishing(Date publishing) {
        this.publishing = publishing;
    }

    public String getPublished_content() {
        return published_content;
    }

    public void setPublished_content(String published_content) {
        this.published_content = published_content;
    }

    public String getPublished_media() {
        return published_media;
    }

    public void setPublished_media(String published_media) {
        this.published_media = published_media;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
