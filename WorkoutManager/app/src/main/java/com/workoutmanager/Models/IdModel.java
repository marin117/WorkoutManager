package com.workoutmanager.Models;

import android.app.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.workoutmanager.R;
import com.workoutmanager.Utils.SharedPreferencesUtil;

public class IdModel {
    @SerializedName("id")
    @Expose
    private String id;

    public IdModel(Activity activity){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(activity);
        this.id = sharedPreferencesUtil.readData(activity.getString(R.string.id));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
