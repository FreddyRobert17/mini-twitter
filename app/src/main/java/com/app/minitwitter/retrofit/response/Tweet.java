
package com.app.minitwitter.retrofit.response;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tweet {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("likes")
    @Nullable
    @Expose
    private List<String> likes;
    @SerializedName("user")
    @Expose
    private User user;

    public Tweet() {
    }

    public Tweet(String id, String message, List<String> likes, User user) {
        this.id = id;
        this.message = message;
        this.likes = likes;
        this.user = user;
    }

    public Tweet(Tweet tweet) {
        this.id = tweet.getId();
        this.message = tweet.getMessage();
        this.likes = tweet.getLikes();
        this.user = tweet.getUser();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
