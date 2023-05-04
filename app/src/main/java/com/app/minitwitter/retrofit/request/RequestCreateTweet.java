package com.app.minitwitter.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCreateTweet {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("message")
    @Expose
    private String message;

    public RequestCreateTweet(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
