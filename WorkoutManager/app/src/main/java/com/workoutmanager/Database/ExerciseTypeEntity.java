package com.workoutmanager.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

//Not in string resource because of better readability of the code

@Entity(tableName = "exercise_type", foreignKeys = {@ForeignKey(entity=ExerciseEntity.class,
                                                    parentColumns = "name", childColumns ="exercise_name"),
                                                    @ForeignKey(
                                                    entity = TypeEntity.class, parentColumns = "name",
                                                    childColumns = "type_name")},
        primaryKeys = {"exercise_name", "type_name"})
public class ExerciseTypeEntity {
    @NonNull
    public String exercise_name;
    @NonNull
    public String type_name;
}
