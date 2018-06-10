package com.workoutmanager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.media.RingtoneManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.workoutmanager.Activity.MainActivity;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "WorkoutManager";
        String notificationText = "Test notification";
        String message = "";
        String username = "";
        String routine_name = "";

        Log.d("TAAAAAAG", "notifikacija");

        if (intent.getStringExtra("message") != null) {
            message = intent.getStringExtra("message");
            username = intent.getStringExtra("user");
            routine_name = intent.getStringExtra("routine_name");
            notificationText = username + " "+ message + " your workout " + routine_name;

        }

        // Prepare a notification with vibration, sound and lights
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1987")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.weightlifting)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.
                getSystemService(context.NOTIFICATION_SERVICE);

        // Build the notification and display it
        notificationManager.notify(1, builder.build());
    }
}