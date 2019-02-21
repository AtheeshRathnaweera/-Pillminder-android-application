package com.atheeshproperty.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class notificationHandler extends ContextWrapper {

    public static final String channelID = "channel";
    public static final String channelName = "Pillminder";
    public static final String vibrateTone = "Vibrate with tone";
    public static final String vibrateRing = "Vibrate with ring";
    public static final String vibrateOnly = "Vibrate only";
    public static final String lightsOnly = "Lights only";
    private NotificationManager mManager;
    DatabaseHelper mydb;

    public notificationHandler(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createchannels();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createchannels() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NotificationCompat.Builder getChannelNotification(String TitleWithTime) {

        mydb = new DatabaseHelper(this);
        String name = null;
        String alarmType = null;

        try {
            name = mydb.getUserName("1");

        } catch (Exception r) {
            Log.d("e", "Name not found");
        }

        List<MedicineHistory> homeListItem;
        List<MedicineHistory> receiveListItem;
        homeListItem = new ArrayList<>();
        receiveListItem = new ArrayList<>();

        long todayDate = System.currentTimeMillis();
        SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
        String todaynew = e.format(todayDate);

        if (TitleWithTime.equals("MORNING")) {
            receiveListItem = mydb.getMorningDataForNotification(todaynew);
            alarmType = mydb.getMorningAlarmType();
        }

        if (TitleWithTime.equals("DAY TIME")) {
            receiveListItem = mydb.getDayDataForNotification(todaynew);
            alarmType = mydb.getDayAlarmType();
        }

        if (TitleWithTime.equals("NIGHT")) {
            receiveListItem = mydb.getNightDataForNotification(todaynew);
            alarmType = mydb.getNightAlarmType();
        }

        for (MedicineHistory c : receiveListItem) {
            MedicineHistory med = new MedicineHistory();
            med.setMedID(c.getMedID());
            med.setUserID(c.getUserID());
            med.setMedName(c.getMedName());
            med.setMedDose(c.getMedDose());
            med.setMedType(c.getMedType());
            med.setMedTime(c.getMedTime());

            homeListItem.add(med);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);//Intent to open the app when notifaction click
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(TitleWithTime + " PRESCRIPTION ");

        String[] mIDS = new String[homeListItem.size()];

        for (int i = 0; i < homeListItem.size(); i++) {

            int medid = homeListItem.get(i).getMedID();
            String medids = Integer.toString(medid);
            mIDS[i] = medids;

            String medname = homeListItem.get(i).getMedName();
            String meddose = homeListItem.get(i).getMedDose();
            String medtype = homeListItem.get(i).getMedType();
            String medtime = homeListItem.get(i).getMedTime();

            inboxStyle.addLine("\n" + medid + " " + medname + " " + meddose + " " + medtype + " " + medtime);
        }

        //action for click the "Mark all as taken" in notification
        Intent jim = new Intent(this, updateOnPushButtonClick.class);
        jim.putExtra(" Received", mIDS);
        PendingIntent parseintent = PendingIntent.getService(this, 0, jim, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.d("alarm ", "Size of the mStrings : " + mIDS.length);

        if (homeListItem.isEmpty()) {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Hello " + name + " !")
                    .setContentText("Did you forgot to add your medicine details?")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Add your medicine details and get notified on time."))
                    .setSmallIcon(R.drawable.notificationiconnew)
                    .setVibrate(new long[]{1000, 1000})
                    .setPriority(Notification.PRIORITY_LOW)
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

        } else {

            Log.d("alarm ", "Notification popup with details");
            lightUpTheScreen();
            long[] pattern = new long[]{1000,1000};

            Uri alarmSound = null;

            if (alarmType.equals(vibrateRing)) {

                String ringName = mydb.getRingTone();


                Log.d("tone","This is the ring tone: "+ringName+" it worked.");

                if (ringName == null) {
                    alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    if (alarmSound == null) {
                        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                }else{
                    alarmSound = Uri.parse(ringName);
                }
                Log.d("alarm type", "Vibrate Ring");
            }

            if (alarmType.equals(vibrateTone)) {

                String notifyName = mydb.getNotificationTone();

                Log.d("tone","This is the notification tone: "+notifyName);
                if(notifyName==null){
                    Log.d("tone","Notification tone not found : "+notifyName);
                    alarmSound = Settings.System.DEFAULT_NOTIFICATION_URI;
                }else{
                    alarmSound = Uri.parse(notifyName);
                }

                Log.d("alarm type", "Vibrate with tone");
            }

            if (alarmType.equals(vibrateOnly)) {
                alarmSound = null;
                Log.d("alarm type", "Vibrate only");
            }

            if (alarmType.equals(lightsOnly)) {
                alarmSound = null;
                pattern = null;
                Log.d("alarm type", "Lights only.");

            }

            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Hello " + name + " !")
                    .setContentText("Did you take your " + TitleWithTime + " MEDICINES?")
                    .setSmallIcon(R.drawable.notificationiconnew)
                    .setVibrate(pattern)
                    .setSound(alarmSound)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setContentIntent(contentIntent)
                    .addAction(R.drawable.check, "Mark all as taken", parseintent)
                    .setStyle(inboxStyle);
        }


    }


    public void lightUpTheScreen() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert powerManager != null;
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Tag");
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        wakeLock.release();
    }


}
