package com.workoutmanager.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Exercise", primaryKeys ={"name"})
public class ExerciseEntity {
    @NonNull
    public String name;

}
