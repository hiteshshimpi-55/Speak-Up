package com.example.apollo;

import com.google.firebase.Timestamp;

import java.util.Date;

public class BlogPost {

    public String user_id,post_txt;
    public Date TimeStamp;

    public BlogPost(String post_txt)
    {
        this.post_txt = post_txt;
    }
    public BlogPost(String user_id, String post_txt,Date timestamp) {
        this.user_id = user_id;
        this.post_txt = post_txt;
        this.TimeStamp = timestamp;
    }


    public String getUser_id() {

        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getPost_txt() {

        return post_txt;
    }

    public void setPost_txt(String post_txt) {
        this.post_txt = post_txt;
    }

    public Date getTimestamp() {
        return TimeStamp;
    }

    public void setTimestamp(Date timestamp) {
        this.TimeStamp = timestamp;
    }
}
