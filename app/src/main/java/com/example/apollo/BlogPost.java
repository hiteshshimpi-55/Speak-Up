package com.example.apollo;

import java.sql.Timestamp;

public class BlogPost {

    public String user_id,post_txt;

    public BlogPost()
    {

    }
    public BlogPost(String user_id, String post_txt, Timestamp timestamp) {
        this.user_id = user_id;
        this.post_txt = post_txt;
        this.timestamp = timestamp;
    }

    public Timestamp timestamp;
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_txt() {
        return post_txt;
    }

    public void setPost_txt(String post_txt) {
        this.post_txt = post_txt;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
