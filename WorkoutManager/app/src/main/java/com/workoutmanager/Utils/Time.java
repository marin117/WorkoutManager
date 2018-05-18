package com.workoutmanager.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.getDefault());
        String formattedDate = sdf.format(new Date());

        return formattedDate;
    }
}
