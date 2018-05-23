package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleToken {

    @SerializedName("tokenId")
    @Expose
    private String tokenId;


    public GoogleToken(String tokenId){
        this.tokenId = tokenId;
    }
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

}