package com.example.apollo;

import java.util.Date;

public class UserLayout {

    String post_content;
    Date timeStamp;
    String position;
    public String getPosition() {
        return position;
    }


    public UserLayout(String post_content, Date timeStamp,String position) {
        this.post_content = post_content;
        this.timeStamp = timeStamp;
        this.position = position;
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

    UserLayout()
    {

    }
}
