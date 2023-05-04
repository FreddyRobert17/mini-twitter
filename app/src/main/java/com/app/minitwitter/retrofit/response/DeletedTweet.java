package com.app.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeletedTweet {
    @SerializedName("id")
    @Expose
    private String id;

    public DeletedTweet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
