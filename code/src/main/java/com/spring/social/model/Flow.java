package com.spring.social.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("FLOW")
public class Flow implements Serializable {

    @Id
    private String id;
    private String user_img;
    private String user_name;
    private Date publishing;
    private String published_content;
    private List<String> published_media;
    private String source;


    /*public Flow(String id) {
        this.id = id;
    }*/

    /*public Flow() {

    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<String> getPublished_media() {
        return published_media;
    }

    public void setPublished_media(List<String> published_media) {
        this.published_media = published_media;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
