package com.workoutmanager.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.workoutmanager.Models.Workout;

public class MainViewModel extends AndroidViewModel{

    private MutableLiveData<Workout> routineID;

    public MainViewModel(@NonNull Application application) {
        super(application);
        routineID = new MutableLiveData<Workout>();
    }

    public MutableLiveData<Workout> getRoutineID() {
        return routineID;
    }

    public void setRoutineID(Workout routineID) {
        this.routineID.setValue(routineID);
    }
}
