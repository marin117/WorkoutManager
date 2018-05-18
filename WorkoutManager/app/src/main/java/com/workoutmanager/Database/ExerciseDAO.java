package com.workoutmanager.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface ExerciseDAO {

    @Query("Select * from Type")
    LiveData<List<String>> getTypes();

    @Query("select exercise_name from exercise_type where type_name in (:types)")
    LiveData<List<String>> getExercises(String[] types);

    class ReadSql {
        public static void readFile (Context context, int resourceCode, SupportSQLiteDatabase db) throws IOException {
            InputStream insertStream = context.getResources().openRawResource(resourceCode);
            BufferedReader insertRead = new BufferedReader(new InputStreamReader(insertStream));


            while(insertRead.ready()){
                String insertLine = insertRead.readLine();
                if(insertLine != null){
                    if(insertLine.isEmpty()) continue;
                    db.execSQL(insertLine);
                }
            }
            insertRead.close();
        }
    }
}
