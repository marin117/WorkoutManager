
package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.workoutmanager.R;
import com.workoutmanager.Utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class Routine {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("exercise")
    @Expose
    private List<Exercise> exercise = null;
    @SerializedName("types")
    @Expose
    private ArrayList<String> types;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("appraisal")
    @Expose
    private Integer appraisal;
    @SerializedName("ismy")
    @Expose
    private Boolean isMy;


    public Routine(){
        this.userId = "";
        this.id = 1;
        this.name = "";
        this.exercise = new ArrayList<Exercise>();
        this.appraisal = 0;
        this.comment="";
        this.types = new ArrayList<>();
        this.isMy = true;
    }

    public Routine(String user_id, String name, ArrayList<String> types){
        this.userId = user_id;
        this.id = 1;
        this.name = name;
        this.types = types;
        this.exercise = new ArrayList<Exercise>();
        this.appraisal = 0;
        this.comment="";
        this.isMy = true;


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }


    public Boolean getIsmy() {
        return isMy;
    }
    public void setIsmy(Boolean ismy) {
        this.isMy = ismy;
    }
}