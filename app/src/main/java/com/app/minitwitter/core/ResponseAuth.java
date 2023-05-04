
package com.app.minitwitter.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAuth {
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("id")
    @Expose
    private String id;

    public ResponseAuth() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseAuth(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
