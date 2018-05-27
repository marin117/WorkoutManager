package com.workoutmanager.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private SharedPreferences sharedPreferences;
    public SharedPreferencesUtil(Activity activity){
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public String readData(String key){
        return sharedPreferences.getString(key, "");
    }

    public void writeData(String key, String value){
        SharedPreferences.Editor editor = editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void clearData(){
        SharedPreferences.Editor editor = editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

}
