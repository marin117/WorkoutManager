package com.workoutmanager.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;


import com.workoutmanager.R;

import java.io.IOException;

@Database(entities = {ExerciseEntity.class, ExerciseTypeEntity.class, TypeEntity.class}, version = 1)
public abstract class ExerciseDatabase extends RoomDatabase {
    public abstract ExerciseDAO exerciseDAO();

    private static ExerciseDatabase INSTANCE;

    static ExerciseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExerciseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ExerciseDatabase.class,
                            "exerciseDB")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    try {
                                        ExerciseDAO.ReadSql.readFile(context, R.raw.create, db);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}