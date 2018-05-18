package com.workoutmanager.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.workoutmanager.Database.ExerciseRepository;
import com.workoutmanager.Models.Routine;

import java.util.ArrayList;
import java.util.List;


public class AddExerciseViewModel extends AndroidViewModel {
    private LiveData<List<String>> types;
    private LiveData<List<String>> exercises;
    private MutableLiveData<ArrayList<String>> typesFrag;
    private MutableLiveData<Routine> routine;
    public AddExerciseViewModel(@NonNull Application application){
        super(application);
        ExerciseRepository exerciseRepository = new ExerciseRepository(getApplication());
        types = exerciseRepository.getTypes();
        exercises = null;
        typesFrag = new MutableLiveData<ArrayList<String>>();
        routine = new MutableLiveData<Routine>();

    }

    public LiveData<List<String>> getTypes(){
        return types;
    }

    public LiveData<List<String>> getExercises(String[] types) {
        ExerciseRepository exerciseRepository = new ExerciseRepository(getApplication());
        return exerciseRepository.getExercises(types);
    }

    public void setTypes(ArrayList<String> types){
        this.typesFrag.setValue(types);
    }

    public MutableLiveData<ArrayList<String>> getTypesFrag() {
        return typesFrag;
    }

    public void setRoutine(Routine routine){
        this.routine.setValue(routine);
    }
    public MutableLiveData<Routine> getRoutine() { return routine;}
}
