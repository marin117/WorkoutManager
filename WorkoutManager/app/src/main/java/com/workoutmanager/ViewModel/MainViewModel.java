package com.workoutmanager.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.workoutmanager.Models.Workout;

public class MainViewModel extends AndroidViewModel{

    private MutableLiveData<Workout> routineID;
    private MutableLiveData<String> userId;

    public MainViewModel(@NonNull Application application) {
        super(application);
        routineID = new MutableLiveData<Workout>();
        userId = new MutableLiveData<String>();
    }

    public MutableLiveData<Workout> getRoutineID() {
        return routineID;
    }

    public void setRoutineID(Workout routineID) {
        this.routineID.setValue(routineID);
    }

    public MutableLiveData<String> getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }
}
