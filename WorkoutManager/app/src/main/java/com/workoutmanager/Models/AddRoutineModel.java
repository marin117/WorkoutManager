package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AddRoutineModel {


    @SerializedName("routine")
    @Expose
    private Routine routine;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("date")
    @Expose
    private Date date;

    public AddRoutineModel(){
        this.routine = new Routine();
        this.date = new Date();
        this.location = "";
    }

    public AddRoutineModel(Routine routine){
        this.routine = routine;
        this.date = new Date();
        this.location = "";
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public Routine getRoutine() {
        return routine;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
