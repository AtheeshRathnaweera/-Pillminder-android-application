package com.atheeshproperty.myapplication;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMedicine extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    ImageButton startdate;
    Button cancel, save;
    EditText nameinput, doseinput, duration, descripton;
    String name, dose, dure, descript, timebuttontext, type, dateandyear, enddate;
    int freqresult = 0, userid;
    DatabaseHelper pilldb;
    CheckBox checkmorning, checkday, checknight;
    RadioGroup timeGroup;
    RadioButton selectedradiobutton;
    Calendar d;
    boolean result = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_medi);

        pilldb = new DatabaseHelper(this);

        startdate = (ImageButton) findViewById(R.id.dateselect);
        cancel = (Button) findViewById(R.id.addmedicancel);
        save = (Button) findViewById(R.id.addmedisave);
        nameinput = (EditText) findViewById(R.id.medNameInput);
        doseinput = (EditText) findViewById(R.id.doseinput);
        duration = (EditText) findViewById(R.id.durationinput);
        descripton = (EditText) findViewById(R.id.descriptioninput);
        checkmorning = (CheckBox) findViewById(R.id.morningselect);
        checkday = (CheckBox) findViewById(R.id.dayselect);
        checknight = (CheckBox) findViewById(R.id.nightselect);
        timeGroup = (RadioGroup) findViewById(R.id.timeRadioGroup);
        selectedradiobutton = (RadioButton) findViewById(R.id.beforem);

        Spinner spin = findViewById(R.id.typespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typespinnertext, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);


        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        addmeddata();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        type = parent.getItemAtPosition(position).toString();//taking selected tablet type

    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        Calendar c = Calendar.getInstance();
        d = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        d.set(Calendar.YEAR, year);
        d.set(Calendar.MONTH, month);
        d.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat later = new SimpleDateFormat("yyyy-MM-dd");
        dateandyear = later.format(c.getTime());
        TextView nextline = findViewById(R.id.startdateinput);
        nextline.setText(dateandyear);

    }


    public void addmeddata() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = 1;

                try {

                    name = nameinput.getText().toString();
                    dose = doseinput.getText().toString();
                    dure = duration.getText().toString();
                    descript = descripton.getText().toString();

                    // (1-Morning) (3-day) (6-night) (4-morning+day) (7-morning+night) (9-day+night) (10-all)

                    if (checkmorning.isChecked()) {
                        freqresult = freqresult + 1;
                    }
                    if (checkday.isChecked()) {
                        freqresult = freqresult + 3;
                    }
                    if (checknight.isChecked()) {
                        freqresult = freqresult + 6;
                    }


                    int selectedId = timeGroup.getCheckedRadioButtonId();
                    selectedradiobutton = (RadioButton) findViewById(selectedId);

                    timebuttontext = (String) selectedradiobutton.getText();//get selected time button text

                    String dur = duration.getText().toString();
                    int f = Integer.parseInt(dur);

                    //add duration time to start date to store in database
                    d.add(Calendar.DAY_OF_MONTH, f);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd");
                    enddate = end.format(d.getTime());

                    if (name.trim().length() == 0 || dure.trim().length() == 0 || TextUtils.isEmpty(dateandyear)) {

                        Toast.makeText(AddMedicine.this, "Incomplete fields."/*+"\n"+"('Name','Duration','Starting Date')."+"\n"*/ + "Please fill the forum properly.", Toast.LENGTH_LONG).show();

                    } else {

                        final boolean exist = pilldb.checkMedicineExists(name);

                        if (exist) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMedicine.this);
                            builder.setTitle("Notice");
                            builder.setMessage("Medicine called '" + name + "' already exists in medicine list.\n\nWhat do you want to do?");

                            builder.setPositiveButton("Save it anyway!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //savingData();
                                            checkAndSaveThread runnable = new checkAndSaveThread();
                                            new Thread(runnable).start();
                                        }
                                    });

                            builder.setNegativeButton("Don't save this data!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(AddMedicine.this,"Nothing Saved!",Toast.LENGTH_LONG).show();
                                        }
                                    });

                            AlertDialog deleteAlert = builder.create();
                            deleteAlert.show();

                    }else {
                            //savingData();
                            checkAndSaveThread runnable = new checkAndSaveThread();
                            new Thread(runnable).start();
                        }}


                } catch (Exception date) {
                    Toast.makeText(AddMedicine.this, "Incomplete fields.Please fill the forum properly.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void savingData(){
        result = pilldb.insertDataToMedTable(userid, name, type, freqresult, dose, timebuttontext, dure, dateandyear, enddate, descript);

        if (result) {
            Toast.makeText(AddMedicine.this, "Saved successfully", Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(2, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        } else {
            Toast.makeText(AddMedicine.this, "Error Ocuured", Toast.LENGTH_LONG).show();
        }

        //checkAndSaveThread runnable = new checkAndSaveThread();
        //new Thread(runnable).start();



    }

    private class checkAndSaveThread implements Runnable{

        @Override
        public void run() {

            Log.d("String", "Thread is started.");
            result = pilldb.insertDataToMedTable(userid, name, type, freqresult, dose, timebuttontext, dure, dateandyear, enddate, descript);

           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(result){
                        Toast.makeText(AddMedicine.this, "Saved Successfully.", Toast.LENGTH_LONG).show();
                        Log.d("status","Saved successfully.");
                        finish();
                        startActivity(getIntent());
                    }else{
                        Toast.makeText(AddMedicine.this, "Error Occurred.", Toast.LENGTH_LONG).show();
                        Log.d("status","Error occurred.");
                    }
                }
            });

            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
            List<Medicine> harray = pilldb.getAllMedData();

            for (Medicine a : harray) {

                int medid = a.getMedID();//Getting medicine ID
                String id = Integer.toString(medid);
                String freq = a.getMedFreq();//Getting medicine frequency
                int fre = Integer.parseInt(freq);

                String startdate = a.getMedStartdate();//Get start Date
                String dur = a.getMedDuration();//get duration
                int dure = Integer.parseInt(dur);

                for(int n = 0;n<dure;n++){

                    boolean exist = pilldb.checkExisInHistoryTable(startdate, id);//
                    Log.d("Status","check existency "+exist);

                    if (exist) {
                        Log.d("exist","Exist data : "+ id + " "+ a.getMedName()+" "+startdate+" "+dur );

                    } else {
                        Boolean resly = pilldb.insertDataToHistoryTable(medid, a.getUserID(), startdate, a.getMedName(), a.getMedDose(), a.getMedType(), fre, a.getMedTime(), a.getMedStartdate(), a.getMedEnddate(), a.getMedDuration(), a.getMedDescrip(), "no","no","no");
                        Log.d("Status", "Saved "+ a.getMedName()+" "+startdate+" "+resly);
                    }

                    Date start = null;
                    try {
                        start = form.parse(startdate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar tCalendar = Calendar.getInstance();
                    tCalendar.setTime(start);
                    tCalendar.add(Calendar.DAY_OF_MONTH, 1);//
                    startdate = form.format(tCalendar.getTime());
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(AddMedicine.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
