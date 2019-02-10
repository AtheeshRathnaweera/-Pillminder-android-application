package com.atheeshproperty.myapplication;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class updateOnPushButtonClick extends Service {


    final class TheUpdateThread implements Runnable {
        int serviceID;
        String[] got;
        DatabaseHelper mydbaccess;
        boolean res;

        TheUpdateThread(int serviceID, String[] uyi) {

            this.serviceID = serviceID;
            this.got = uyi;

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);//Cancel the active notification in status bar

            Calendar morning = Calendar.getInstance();
            Calendar day = Calendar.getInstance();
            Calendar night = Calendar.getInstance();

            morning.set(Calendar.HOUR_OF_DAY, 12);
            morning.set(Calendar.MINUTE, 0);
            morning.set(Calendar.SECOND, 0);

            day.set(Calendar.HOUR_OF_DAY, 17);
            day.set(Calendar.MINUTE, 30);
            day.set(Calendar.SECOND, 0);

            night.set(Calendar.HOUR_OF_DAY, 19);
            night.set(Calendar.MINUTE, 12);
            night.set(Calendar.SECOND, 0);

            long todayDate = System.currentTimeMillis();
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            String todaynew = e.format(todayDate);

            mydbaccess = new DatabaseHelper(updateOnPushButtonClick.this);

            String b = Integer.toString(got.length);
            Log.d("Received status", "Length of the recived array : " + b);

            for (int i = 0; i < got.length; i++) {

                if (Calendar.getInstance().before(morning)) {
                   res = mydbaccess.updateMorningTaken(todaynew, got[i], "1");

                } else {

                    if (Calendar.getInstance().before(day)) {
                        res = mydbaccess.updateDayTaken(todaynew, got[i], "1");
                    } else {
                       res = mydbaccess.updateNightTaken(todaynew, got[i], "1");
                    }
                }

                if(res){
                    Log.d("Update status","update status is : "+res + " Medicine ID : "+got[i]);
                }else{
                    Log.d("Update status","Error occured "+res + " "+got[i]);
                }

            }

            stopSelf();


        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("String Key", "Database update service started");

        String[] atheesh = intent.getStringArrayExtra("Received");

        Thread thread = new Thread(new TheUpdateThread(startId, atheesh));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("String Key", "Database updated service stopped");
    }


}
