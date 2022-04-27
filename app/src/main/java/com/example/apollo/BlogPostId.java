package com.example.apollo;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class BlogPostId {
    public String getBlogPostId() {
        return blogPostId;
    }

    @Exclude
    public String blogPostId;
    public int likeCount;

    public <T extends BlogPostId> T withId(@NonNull final String id)
    {
        this.blogPostId = id;
        return (T) this;
    }
}
