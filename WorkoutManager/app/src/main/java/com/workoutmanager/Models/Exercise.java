package com.workoutmanager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exercise {

    @SerializedName("routine_id")
    @Expose
    private Integer routineId;
    @SerializedName("exercise_name")
    @Expose
    private String exerciseName;
    @SerializedName("sets")
    @Expose
    private Integer sets;
    @SerializedName("reps")
    @Expose
    private Integer reps;

    public Exercise(){
        this.exerciseName="";
        this.routineId = null;
        this.sets = 0;
        this.reps = 0;

    }

    public Integer getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Integer routineId) {
        this.routineId = routineId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

}