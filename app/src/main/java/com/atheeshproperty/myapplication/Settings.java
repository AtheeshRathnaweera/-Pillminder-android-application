package com.atheeshproperty.myapplication;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Settings extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    ImageButton morningTime, dayTime, nightTime;
    TextView morning, day, night, ringName, notifyName;
    Boolean morningClick = false;
    Boolean dayClick = false;
    Boolean nightClick = false;
    String selectedMorningTime, selectedDayTime, selectedNightTime;
    Spinner spinmorning, spinday, spinnight;
    DatabaseHelper mydb;
    Button save, cancel;
    Uri toneUri, notifyUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mydb = new DatabaseHelper(this);

        morningTime = findViewById(R.id.morningtimeselect);
        morningTime.setOnClickListener(this);

        dayTime = findViewById(R.id.daytimeselect);
        dayTime.setOnClickListener(this);

        nightTime = findViewById(R.id.nighttimeselect);
        nightTime.setOnClickListener(this);

        morning = findViewById(R.id.morningtime);
        day = findViewById(R.id.daytime);
        night = findViewById(R.id.nighttime);

        save = findViewById(R.id.savesettings);
        cancel = findViewById(R.id.cancel);

        ringName = findViewById(R.id.ringingToneName);
        notifyName = findViewById(R.id.notificationToneName);

        spinmorning = findViewById(R.id.morningSpinner);
        ArrayAdapter<CharSequence> morningadapter = ArrayAdapter.createFromResource(this, R.array.alrttypespinnertext, android.R.layout.simple_spinner_item);
        morningadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinmorning.setAdapter(morningadapter);

        spinday = findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> adapterday = ArrayAdapter.createFromResource(this, R.array.alrttypespinnertext, android.R.layout.simple_spinner_item);
        adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinday.setAdapter(adapterday);

        spinnight = findViewById(R.id.nightSpinner);
        ArrayAdapter<CharSequence> adapternight = ArrayAdapter.createFromResource(this, R.array.alrttypespinnertext, android.R.layout.simple_spinner_item);
        adapternight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnight.setAdapter(adapternight);

        populateAtTheStart();
        settingsDisplay();

        notifyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                startActivityForResult(intent, 3);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToSettingsTable();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void settingsDisplay() {

        ringName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog openDialog = new Dialog(Settings.this);
                openDialog.setContentView(R.layout.customselectimagedialog);

                TextView text = (TextView) openDialog.findViewById(R.id.dialogTitle);
                Button optionTone = (Button) openDialog.findViewById(R.id.gallery);
                Button optionSong = (Button) openDialog.findViewById(R.id.takephoto);
                Button cancel = (Button) openDialog.findViewById(R.id.cancel);

                text.setText("Change ringing tone");
                optionTone.setText("Select a tone");
                optionSong.setText("Select a song");


                optionTone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                        startActivityForResult(intent, 1);

                        openDialog.dismiss();
                    }
                });

                optionSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 2);

                        openDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog.cancel();
                    }
                });


                openDialog.show();
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            try {
                toneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.d("Picked", "This is the URL  :" + toneUri);

                String name = getNames(toneUri, 1);//type 1 --> Ringtone
                ringName.setText(name);


            } catch (NullPointerException n) {
                Log.d("Picked", "Nothing selected");
            }

        }
        if (requestCode == 2) {

            try {
                toneUri = data.getData();
                Log.d("Picked Song", "This is the song URI : " + toneUri);
                String name = getNames(toneUri, 2);//type 2 --> Song
                ringName.setText(name);

            } catch (NullPointerException m) {
                Log.d("Picked song", "Nothing selected.");
            }
        }
        if (requestCode == 3) {

            try {
                Log.d("status", "Notification url process started.");
                notifyUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.d("Picked notification", "This is the notification URI : " + notifyUri);
                String name = getNames(notifyUri, 3);//type 3 --> notification
                notifyName.setText(name);

            } catch (NullPointerException m) {
                Log.d("Picked song", "Nothing selected.");
            }
        }

    }

    public String getNames(Uri uri, int type) {

        Log.d("Name", "getNames started.");

        String name = "";

        switch (type) {
            case 1:
                Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                name = ringtone.getTitle(this);

            case 2:
                String scheme = uri.getScheme();
                if (scheme.equals("content")) {
                    String[] proj = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};

                    Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        if (cursor.getColumnIndex(MediaStore.Audio.Media.TITLE) != -1) {
                            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                            Log.d("Name", "This is the name of the song : " + name);
                        }
                    }
                }

            case 3:
                name = RingtoneManager.getRingtone(this, uri).getTitle(this);
                Log.d("Name", "This is the name of the notification : " + name);


        }

        return name;

    }

    public void populateAtTheStart() {

        Cursor cursor = mydb.getAllSettings();
        String recMorningTime = null, recDayTime = null, recNightTime = null;
        String recMorningType = null, recDayType = null, recNightType = null;
        String recNotiTone = null;
        String recRingTone = null;

        try {
            while (cursor.moveToNext()) {
                recMorningTime = cursor.getString(1);
                recDayTime = cursor.getString(2);
                recNightTime = cursor.getString(3);
                recMorningType = cursor.getString(4);
                recDayType = cursor.getString(5);
                recNightType = cursor.getString(6);
                recNotiTone = cursor.getString(7);
                recRingTone = cursor.getString(8);
            }
        } catch (Exception n) {
            Log.d("Populate error", "Can not populate" + cursor.getCount());
        }

        cursor.close();


        Log.d("Received status", "Data received " + cursor.getCount() + recMorningTime);
        Log.d("Received status", "Notification :  " + recNotiTone + " RingTone : "+recRingTone);
        morning.setText(convertToDisplayableFormat(recMorningTime));
        day.setText(convertToDisplayableFormat(recDayTime));
        night.setText(convertToDisplayableFormat(recNightTime));

        int morningTypePosition = getArrayPosition(recMorningType, 1); // 1 is used for identify the array list (type array or ring tone)
        spinmorning.setSelection(morningTypePosition);//Set previously selected item into the spinner

        int dayTypePosition = getArrayPosition(recDayType, 1);
        spinday.setSelection(dayTypePosition);

        int nightTypePosition = getArrayPosition(recNightType, 1);
        spinnight.setSelection(nightTypePosition);

        if (recNotiTone == null) {
            notifyName.setText("Default tone");
        }else{
            Uri notifi = Uri.parse(recNotiTone);
            notifyUri = notifi;
            notifyName.setText(getNames(notifi,3));
        }

        if (recRingTone == null) {
            ringName.setText("Default tone");
        }else{
            Uri ring = Uri.parse(recRingTone);
            toneUri = ring;
            if(getNames(ring,1) == null){
                ringName.setText(getNames(ring,2));
            }else{
                ringName.setText(getNames(ring,1));
            }
        }

    }

    public String convertToDisplayableFormat(String received) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatterOne = new SimpleDateFormat("hh : mm aa");
        String result = null;
        try {
            Date rec = formatter.parse(received);
            result = formatterOne.format(rec);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Populate error", "Error occurred while converting");
        }

        return result;

    }

    public int getArrayPosition(String alertTypeString, Integer i) {
        if (i == 1) {
            List<String> g = Arrays.asList(getResources().getStringArray(R.array.alrttypespinnertext));
            return g.indexOf(alertTypeString);
        } else {
            List<String> g = Arrays.asList(getResources().getStringArray(R.array.tonespinnertext));
            return g.indexOf(alertTypeString);
        }

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar t = Calendar.getInstance();
        t.set(Calendar.HOUR_OF_DAY, hourOfDay);
        t.set(Calendar.MINUTE, minute);

        SimpleDateFormat formatterToDisplay = new SimpleDateFormat("hh : mm aa");
        String formattedMinute = formatterToDisplay.format(t.getTime());

        if (morningClick) {

            morning.setText(formattedMinute);
            morningClick = false;
        }

        if (dayClick) {

            day.setText(formattedMinute);
            dayClick = false;
        }

        if (nightClick) {

            night.setText(formattedMinute);
            nightClick = false;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.morningtimeselect:

                DialogFragment morningPicker = new TimePickerFragment();
                morningPicker.show(getSupportFragmentManager(), "Time picker morning");
                morningClick = true;
                break;

            case R.id.daytimeselect:

                DialogFragment dayPicker = new TimePickerFragment();
                dayPicker.show(getSupportFragmentManager(), "Time picker day");
                dayClick = true;
                break;

            case R.id.nighttimeselect:

                DialogFragment nightPicker = new TimePickerFragment();
                nightPicker.show(getSupportFragmentManager(), "Time picker night");
                nightClick = true;
                break;

            default:
                break;
        }

    }

    public void saveDataToSettingsTable() {
        String morningtype = spinmorning.getSelectedItem().toString();
        String daytype = spinday.getSelectedItem().toString();
        String nighttype = spinnight.getSelectedItem().toString();
        String notitone;
        String rintone;

        selectedMorningTime = convertToSavebleType(morning.getText().toString());
        selectedDayTime = convertToSavebleType(day.getText().toString());
        selectedNightTime = convertToSavebleType(night.getText().toString());

        if (notifyUri != null) {
            notitone = notifyUri.toString();
        } else {
            notitone = null;
        }

        if (toneUri != null) {
            rintone = toneUri.toString();
        } else {
            rintone = null;
        }

        Boolean res = mydb.updateDataOfSettingsTable("1", selectedMorningTime, selectedDayTime, selectedNightTime, morningtype, daytype, nighttype, notitone, rintone);

       if (res) {
            Log.d("Settings saved", "Settings saved successfully");
            Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String convertToSavebleType(String gotTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatterOne = new SimpleDateFormat("hh : mm aa");
        String result = null;
        try {
            Date rec = formatterOne.parse(gotTime);
            result = formatter.format(rec);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Populate error", "Error occured while converting");
        }

        return result;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
