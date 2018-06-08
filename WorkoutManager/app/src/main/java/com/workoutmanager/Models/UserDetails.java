package com.workoutmanager.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class UserDetails {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("workouts")
    @Expose
    private List<Workout> workouts = null;
    @SerializedName("type")
    @Expose
    private List<String> type = null;
    @SerializedName("exercise")
    @Expose
    private List<String> exercise = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getExercise() {
        return exercise;
    }

    public void setExercise(List<String> exercise) {
        this.exercise = exercise;
    }

}