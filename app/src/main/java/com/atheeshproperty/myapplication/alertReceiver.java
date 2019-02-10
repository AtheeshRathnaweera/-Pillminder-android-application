package com.atheeshproperty.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class alertReceiver extends BroadcastReceiver {

    public String dtString;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("String Key", "Alarm intent received.");

        Calendar c = Calendar.getInstance();
        Calendar morningLimit = Calendar.getInstance();
        Calendar dayLimit = Calendar.getInstance();
        Calendar nightLimit = Calendar.getInstance();

        morningLimit.set(Calendar.HOUR_OF_DAY, 12);
        morningLimit.set(Calendar.MINUTE, 0);
        morningLimit.set(Calendar.SECOND, 0);

        dayLimit.set(Calendar.HOUR_OF_DAY, 17);
        dayLimit.set(Calendar.MINUTE, 30);
        dayLimit.set(Calendar.SECOND, 0);

        nightLimit.set(Calendar.HOUR_OF_DAY, 20);
        nightLimit.set(Calendar.MINUTE, 30);
        nightLimit.set(Calendar.SECOND, 0);


        if (Calendar.getInstance().before(morningLimit)) {
            dtString = "MORNING";
            Log.d("String Key", "Set time as MORNING in title");
        } else {
            if (Calendar.getInstance().before(dayLimit)) {
                dtString = "DAY TIME";
                Log.d("String Key", "Set time as DAY in title");
            } else {
                dtString = "NIGHT";
                Log.d("String Key", "Set time as NIGHT in title");
            }

        }

        notificationHandler myNotification = new notificationHandler(context);
        NotificationCompat.Builder nb = myNotification.getChannelNotification(dtString);
        myNotification.getManager().notify(1, nb.build());


    }
}
