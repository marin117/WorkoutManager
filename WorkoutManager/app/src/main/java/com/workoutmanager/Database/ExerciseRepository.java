package com.workoutmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.workoutmanager.Database.ExerciseDatabase.getDatabase;

public class ExerciseRepository {

    private ExerciseDAO exerciseDAO;
    private LiveData<List<String>> types;
    private LiveData<List<String>> exercises;

    public ExerciseRepository(Application application){
        ExerciseDatabase db = getDatabase(application);
        exerciseDAO = db.exerciseDAO();
        types = exerciseDAO.getTypes();
        exercises = null;
    }

    public LiveData<List<String>> getTypes() {
        return types;
    }

    public LiveData<List<String>> getExercises(String[] types){
       return exerciseDAO.getExercises(types);
   }
}

