
package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Routine {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("exercise")
    @Expose
    private List<Exercise> exercise = null;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("appraisal")
    @Expose
    private Integer appraisal;

    public Routine(){
        this.userId = 2;
        this.id = 1;
        this.name = "";
        exercise = new ArrayList<Exercise>();
        this.appraisal = 0;
        this.comment="";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Exercise> getExercise() {
        return exercise;
    }

    public void setExercise(ArrayList<Exercise> exercise){
        this.exercise = exercise;
    }

    public void setExercise(List<Exercise> exercise) {
        this.exercise = exercise;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(Integer appraisal) {
        this.appraisal = appraisal;
    }

}