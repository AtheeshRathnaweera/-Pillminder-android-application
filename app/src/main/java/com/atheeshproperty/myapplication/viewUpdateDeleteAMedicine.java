package com.atheeshproperty.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class viewUpdateDeleteAMedicine extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    String medid, name, dose, duration, description, mtype, frequency, time, startdate, dateandyear;
    int userid;
    private EditText mname, mdose, mduration, mdescription;
    TextView date;
    RadioGroup timebutton;
    ImageButton dateselect, back;
    CheckBox morning, day, night;
    Button update, delete;
    RadioButton selectedradiobutton;
    Date d;
    DatabaseHelper pilldbaccess;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update_delete_amedicine);

        pilldbaccess = new DatabaseHelper(this);

        mname = (EditText) findViewById(R.id.updatemedName);
        mdose = (EditText) findViewById(R.id.updatedose);
        mduration = (EditText) findViewById(R.id.updateduration);
        mdescription = (EditText) findViewById(R.id.updatedescription);
        timebutton = (RadioGroup) findViewById(R.id.timeRadioGroup);
        dateselect = (ImageButton) findViewById(R.id.dateselect);
        date = (TextView) findViewById(R.id.updatestartdate);
        morning = (CheckBox) findViewById(R.id.morningselect);
        day = (CheckBox) findViewById(R.id.dayselect);
        night = (CheckBox) findViewById(R.id.nightselect);
        back = (ImageButton) findViewById(R.id.backicon);
        update = (Button) findViewById(R.id.mediupdate);
        delete = (Button) findViewById(R.id.medidelete);

        Intent intent = getIntent();
        medid = intent.getExtras().getString("MedID");
        userid = intent.getExtras().getInt("UserID");
        name = intent.getExtras().getString("MedName");
        mtype = intent.getExtras().getString("MedType");
        frequency = intent.getExtras().getString("MedFrequency");
        dose = intent.getExtras().getString("MedDose");
        time = intent.getExtras().getString("MedTime");
        duration = intent.getExtras().getString("MedDuration");
        startdate = intent.getExtras().getString("MedStart");
        description = intent.getExtras().getString("MedDescript");

        mname.setText(name);
        mdose.setText(dose);
        mduration.setText(duration);
        mdescription.setText(description);
        date.setText(startdate);

        checkedCheckBoxes();
        setTimeRadioButtonAtBegining();
        sendupdates();
        deleteMedicine();
        final int p = gettypeposition(mtype);//get position of previously selected value in type array list

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        spin = findViewById(R.id.typespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typespinnertext, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setSelection(p);//set drop down list selected item to previously selected value

        spin.setOnItemSelectedListener(this);

    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        @SuppressLint("SimpleDateFormat") SimpleDateFormat later = new SimpleDateFormat("yyyy-MM-dd");
        dateandyear = later.format(c.getTime());
        TextView nextline = findViewById(R.id.updatestartdate);
        nextline.setText(dateandyear);

    }

    public void sendupdates() {

        update.setOnClickListener(new View.OnClickListener() {

            int newFreq = 0;
            String endMedDate;

            @Override
            public void onClick(View v) {

                try {
                    name = mname.getText().toString();
                    dose = mdose.getText().toString();
                    duration = mduration.getText().toString();
                    startdate = date.getText().toString();
                    description = mdescription.getText().toString();

                    // (1-Morning) (3-day) (6-night) (4-morning+day) (7-morning+night) (9-day+night) (10-all)
                    if (morning.isChecked()) {
                        newFreq = newFreq + 1;
                    }
                    if (day.isChecked()) {
                        newFreq = newFreq + 3;
                    }
                    if (night.isChecked()) {
                        newFreq = newFreq + 6;
                    }

                    //Getting selected Radio Butoon text
                    int selectedId = timebutton.getCheckedRadioButtonId();
                    selectedradiobutton = (RadioButton) findViewById(selectedId);
                    String timebuttontext = (String) selectedradiobutton.getText();

                    endMedDate = getEndDate();

                    if (name.trim().length() == 0 || duration.trim().length() == 0) {

                        Toast.makeText(viewUpdateDeleteAMedicine.this, "Incomplete fields."/*+"\n"+"('Name','Duration','Starting Date')."+"\n"*/ + "Please fill the forum properly.", Toast.LENGTH_LONG).show();

                    } else {
                        boolean result = pilldbaccess.updateMedData(medid, userid, name, mtype, newFreq, dose, timebuttontext, duration, startdate, endMedDate, description);
                        boolean resul = pilldbaccess.updateHistoryMedData(medid, userid, name, mtype, newFreq, dose, timebuttontext, duration, startdate, endMedDate, description);

                        if (result && resul) {
                            Toast.makeText(viewUpdateDeleteAMedicine.this, "Updated successfully" + newFreq, Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(viewUpdateDeleteAMedicine.this, ViewAllMedicine.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(viewUpdateDeleteAMedicine.this, "Error Ocuured", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception date) {
                    Toast.makeText(viewUpdateDeleteAMedicine.this, "Exception", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public String getEndDate() {
        String enddate;
        int f = Integer.parseInt(duration) - 1;
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = formatter.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar tCalendar = Calendar.getInstance();
        tCalendar.setTime(d);
        //add duration time to start date to store in database
        tCalendar.add(Calendar.DAY_OF_MONTH, f);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd");
        return enddate = end.format(tCalendar.getTime());

    }

    private int gettypeposition(String type) {
        List<String> g = Arrays.asList(getResources().getStringArray(R.array.typespinnertext));
        return g.indexOf(type);

    }

    public void setTimeRadioButtonAtBegining() {
        if (time.equals("Before Meal")) {
            timebutton.check(R.id.beforem);
        } else {
            timebutton.check(R.id.afterm);
        }

    }


    public void checkedCheckBoxes() {//Checked check boxes at the launch

        switch (frequency) {
            case "1":
                morning.setChecked(true);
                break;
            case "3":
                day.setChecked(true);
                break;
            case "6":
                night.setChecked(true);
                break;
            case "4":
                day.setChecked(true);
                morning.setChecked(true);
                break;
            case "7":
                morning.setChecked(true);
                night.setChecked(true);
                break;
            case "9":
                day.setChecked(true);
                night.setChecked(true);
                break;
            case "10":
                morning.setChecked(true);
                day.setChecked(true);
                night.setChecked(true);
                break;
        }

    }

    public void deleteMedicine() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog openDialog = new Dialog(viewUpdateDeleteAMedicine.this);
                openDialog.setContentView(R.layout.customdialogbox);

                TextView text = (TextView) openDialog.findViewById(R.id.dialogText);
                Button yes = (Button) openDialog.findViewById(R.id.firstOption);
                Button no = (Button) openDialog.findViewById(R.id.secondOption);


                text.setText(R.string.deletemedicinetext);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer deletedRows = pilldbaccess.deleteMedicineData(medid);//Removing data from medicine detials table
                        Integer t = pilldbaccess.deleteMedicineFromHistoryTable(medid);//Removing this medicine data from medicine history table
                        pilldbaccess.close();

                        String ti = Integer.toString(t);
                        if (deletedRows > 0) {

                            Toast.makeText(viewUpdateDeleteAMedicine.this, "Deleted Successfully" + ti, Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(viewUpdateDeleteAMedicine.this, ViewAllMedicine.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(viewUpdateDeleteAMedicine.this, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                        }


                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog.cancel();
                    }
                });
                openDialog.show();

               /* AlertDialog.Builder builder = new AlertDialog.Builder(viewUpdateDeleteAMedicine.this);
                builder.setTitle("Notice");
                builder.setMessage("This medicine details will be deleted permanently.\n\nDo you want to continue?\n");

                builder.setPositiveButton("Yes! Continue deleting.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                builder.setNegativeButton("No!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog deleteAlert = builder.create();
                deleteAlert.show();*/

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mtype = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
