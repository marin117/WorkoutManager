package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("googleToken")
    @Expose
    private String googleToken;

    @SerializedName("pushyToken")
    @Expose
    private String pushyToken;


    public Token(String googleToken, String pushyToken){
        this.pushyToken = pushyToken;
        this.googleToken = googleToken;
    }


    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getPushyToken() {
        return pushyToken;
    }

    public void setPushyToken(String pushyToken) {
        this.pushyToken = pushyToken;
    }
}