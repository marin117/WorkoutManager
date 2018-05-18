package com.workoutmanager.Database;



import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Type", primaryKeys ={"name"})
public class TypeEntity {
    @NonNull
    public String name;
}
