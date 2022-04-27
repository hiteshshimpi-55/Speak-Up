package com.example.apollo;

import java.util.Comparator;
import java.util.Date;

public class Most {
    String post_content;
    Date timeStamp;
    String position;
    int like_Count;
    String blogPostId;
    public int getLike_Count() {
        return like_Count;
    }

    public void setLike_Count(int like_Count) {
        this.like_Count = like_Count;
    }


    public String getPosition() {
        return position;
    }


    public Most(String position,String post_content,Date timeStamp,String blogPostId,int like_Count) {
        this.post_content = post_content;
        this.timeStamp = timeStamp;
        this.position = position;
        this.blogPostId = blogPostId;
        this.like_Count =  like_Count;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    Most()
    {

    }
    public static Comparator<Most> SortTheList = new Comparator<Most>() {
        @Override
        public int compare(Most o1, Most o2) {
            return -Integer.compare(o1.getLike_Count(), o2.getLike_Count());
        }
    };
}
