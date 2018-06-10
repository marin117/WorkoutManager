package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("pushyid")
    @Expose
    private String pushyid;
    @SerializedName("stars")
    @Expose
    private Integer stars;
    @SerializedName("isstar")
    @Expose
    private Boolean isstar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPushyid() {
        return pushyid;
    }

    public void setPushyid(String pushyid) {
        this.pushyid = pushyid;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Boolean getIsstar() {
        return isstar;
    }

    public void setIsstar(Boolean isstar) {
        this.isstar = isstar;
    }

}