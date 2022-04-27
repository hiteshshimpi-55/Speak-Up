package com.example.apollo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class BlogPost extends BlogPostId {

    public String user_id,post_txt;
    public Date TimeStamp;
    ArrayList<String> liked_by;
    public ArrayList<String> getLiked_by() {
        return liked_by;
    }

    public void setLiked_by(ArrayList<String> liked_by) {
        this.liked_by = liked_by;
    }

    public BlogPost(String post_txt)
    {
        this.post_txt = post_txt;
    }
    public BlogPost()
    {

    }
    public BlogPost(String user_id, String post_txt,Date timestamp,ArrayList<String> arrayList) {
        this.user_id = user_id;
        this.post_txt = post_txt;
        this.TimeStamp = timestamp;
        this.liked_by = arrayList;
    }
    public BlogPost(String user_id, String post_txt, Date timeStamp, int like_count) {
        this.user_id = user_id;
        this.post_txt = post_txt;
        TimeStamp = timeStamp;
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
