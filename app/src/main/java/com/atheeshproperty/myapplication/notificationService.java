package com.atheeshproperty.myapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class notificationService extends Service {


    final class TheThread implements Runnable {
        int serviceID;
        DatabaseHelper mydb;
        Date RmorningTime = null, RdayTime = null, RnightTime = null;

        TheThread(int serviceID) {

            this.serviceID = serviceID;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            Log.d("alert", "Service started running in thread.");
            mydb = new DatabaseHelper(getApplicationContext());

            Cursor c = mydb.getAlarmTimes();

            String morning = null;
            String day = null;
            String night = null;

            while (c.moveToNext()) {

                morning = c.getString(0);
                day = c.getString(1);
                night = c.getString(2);

            }

            c.close();
            mydb.close();

            //Convert current time to "HH:mm:ss" format to compare in if condition
            Calendar currentTime = Calendar.getInstance();//Getting current system time
            SimpleDateFormat defaultFormatter = new SimpleDateFormat("HH:mm:ss");
            String now = defaultFormatter.format(currentTime.getTime());


            //Format and create morning time alert in milliseconds
            SimpleDateFormat onlyDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fullTimeFormatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

            String todaydate = onlyDateFormatter.format(currentTime.getTime());//

            String completeMorning = todaydate +"-"+morning;//Adding today date string with morning time string
            String completeDay = todaydate +"-"+day;//Adding today date string with day time string
            String completeNight = todaydate +"-"+ night;//Adding today date string with night alert time string

            try {

                RmorningTime = fullTimeFormatter.parse(completeMorning);
                RdayTime = fullTimeFormatter.parse(completeDay);
                RnightTime = fullTimeFormatter.parse(completeNight);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(notificationService.this, alertReceiver.class);


            //Create morning pending intent
            if(morning != null){
                if(now.compareTo(morning) < 0){
                    PendingIntent pendingIntentMorning = PendingIntent.getBroadcast(notificationService.this,1, intent, 0);
                    alarm.setExact(AlarmManager.RTC_WAKEUP,RmorningTime.getTime(), pendingIntentMorning);
                    Log.d("String status", "Morning alarm is set");
                    Log.d("String next","Now today date : "+todaydate+" morning time : "+morning+" "+completeMorning);
                }else{
                    Log.d("String status","Morning alarm has passed. "+ morning);
                }
            }else{
                Log.d("String key", "Morning time is null " + morning);
            }

            //Create day pending intent
            if(day != null){
                if(now.compareTo(day) < 0){
                    PendingIntent pendingIntentNight = PendingIntent.getBroadcast(notificationService.this, 2, intent, 0);
                    alarm.setExact(AlarmManager.RTC_WAKEUP,RdayTime.getTime(), pendingIntentNight);
                    Log.d("String status","Day alarm is set");
                    Log.d("String next","Now today date : "+todaydate+" day time : "+day+" "+completeDay);
                }else{
                    Log.d("String status", "Day time has passed. "+day);
                }
            }else{
                Log.d("String key", "Day time is null " + day);
            }

            //Create night pending intent
            if (night != null) {
                if (now.compareTo(night) < 0) { //now > night answer is positive
                    PendingIntent pendingIntentNightt = PendingIntent.getBroadcast(notificationService.this, 3, intent, 0);
                    alarm.setExact(AlarmManager.RTC_WAKEUP,RnightTime.getTime(), pendingIntentNightt);
                    Log.d("String status", "Night alarm is set");
                    Log.d("String next","Now today date : "+todaydate+" night time : "+night+ " "+completeNight);
                } else {
                    Log.d("String key", "Night time has passed. "+night);
                }
            } else {
                Log.d("String key", "Night time is null" + night);
            }

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(notificationService.this, "Service started", Toast.LENGTH_LONG).show();
        Log.d("String Key", "Service is started");
        Thread thread = new Thread(new TheThread(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(notificationService.this, "Service is destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
