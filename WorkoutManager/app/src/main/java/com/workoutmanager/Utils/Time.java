package com.workoutmanager.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.getDefault());

        return sdf.format(new Date());
    }

    public static String formatDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd:mm:YYYY kk:mm:ss z", Locale.getDefault());
        Date result =  df.parse(date);
        return result.toString();
    }
}
