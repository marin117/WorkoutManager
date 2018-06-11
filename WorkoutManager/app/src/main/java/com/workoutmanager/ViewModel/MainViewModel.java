package com.workoutmanager.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.Workout;

public class MainViewModel extends AndroidViewModel{

    private MutableLiveData<Workout> workout;
    private MutableLiveData<String> userId;
    private MutableLiveData<Routine> routineId;

    public MainViewModel(@NonNull Application application) {
        super(application);
        workout = new MutableLiveData<Workout>();
        userId = new MutableLiveData<String>();
        routineId = new MutableLiveData<Routine>();
    }

    public MutableLiveData<Workout> getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout.setValue(workout);
    }

    public MutableLiveData<String> getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    public MutableLiveData<Routine> getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Routine routineId) {
        this.routineId.setValue(routineId);
    }
}
