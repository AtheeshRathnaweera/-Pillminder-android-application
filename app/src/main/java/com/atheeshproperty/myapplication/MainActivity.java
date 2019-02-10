package com.atheeshproperty.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    static boolean active = false;
    DatabaseHelper mydb;
    EditText username, userage, userbgroup, userweight, userheight, usernotes;
    TextView displayname, displayage, displayblood, displayweight, displayheight, displaynotes, displaybmi;
    Button save, userok, edituserprofile;
    FloatingActionButton addmedicinefab, calendarfab, openFab, prof;
    Boolean viewprofile = false;
    private List<MedicineHistory> medicineHomeList;
    private long todayDate;
    boolean isFABOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    CircleImageView userpicture;
    RecyclerView recyclerView;

    private Handler mainHandler = new Handler();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.inputname);
        userage = (EditText) findViewById(R.id.inputage);
        userbgroup = (EditText) findViewById(R.id.inputblood);
        userweight = (EditText) findViewById(R.id.inputweight);
        userheight = (EditText) findViewById(R.id.inputheight);
        usernotes = (EditText) findViewById(R.id.inputnotes);
        save = (Button) findViewById(R.id.addmediok);
        userok = (Button) findViewById(R.id.viewprofileok);
        edituserprofile = (Button) findViewById(R.id.viewprofileedit);
        displayname = (TextView) findViewById(R.id.prodisname);
        displayage = (TextView) findViewById(R.id.prodisage);
        displayblood = (TextView) findViewById(R.id.prodisblood);
        displayweight = (TextView) findViewById(R.id.prodisweight);
        displayheight = (TextView) findViewById(R.id.prodisheight);
        displaynotes = (TextView) findViewById(R.id.prodisnotes);
        displaybmi = (TextView) findViewById(R.id.prodisbmi);

        addmedicinefab = findViewById(R.id.homefab);
        calendarfab = findViewById(R.id.calendarfab);
        prof = findViewById(R.id.profilefab);
        openFab = findViewById(R.id.homeopen);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        userpicture = findViewById(R.id.methodprofile);

        startup("pillminder.db");
        saveDefaultSettings();

        recyclerView = findViewById(R.id.HomeMedicineRecyclerView);

        fabControl();

        todayDate = System.currentTimeMillis();
        SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
        String todaynew = e.format(todayDate);
        medicineHomeList = mydb.getMedicineDataFromHistoryTable(todaynew);

        //Toast.makeText(MainActivity.this, "Found" + cui, Toast.LENGTH_LONG).show();

        executingRecyclerViewForHistory(medicineHomeList);

        settingDateOnHomeScreen();//Setting up date and day name on action bar in home layout

        addnewmedicine();

        displayuserdataonprofile();

        startService();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //medicineHomeList = new ArrayList<>();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Hide the app name label on action bar

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //Calendar fab button
        calendarfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

                /*Snackbar.make(v,"Select a date",Snackbar.LENGTH_LONG).setAction("Action",null).show();*/

            }
        });
    }

    public void fabControl() {
        openFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });

    }

    public void animateFAB() {

        if (isFABOpen) {

            openFab.startAnimation(rotate_backward);
            addmedicinefab.startAnimation(fab_close);
            calendarfab.startAnimation(fab_close);
            prof.startAnimation(fab_close);
            addmedicinefab.setClickable(false);
            calendarfab.setClickable(false);
            prof.setClickable(false);
            isFABOpen = false;

        } else {

            openFab.startAnimation(rotate_forward);
            addmedicinefab.startAnimation(fab_open);
            calendarfab.startAnimation(fab_open);
            prof.startAnimation(fab_open);
            addmedicinefab.setClickable(true);
            calendarfab.setClickable(true);
            prof.setClickable(true);
            isFABOpen = true;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.view_all_med:

                Intent intent = new Intent(MainActivity.this, ViewAllMedicine.class);
                startActivity(intent);
                finish();

                break;

            case R.id.know_medicines:

                Intent intents = new Intent(MainActivity.this, SearchMedicineDetails.class);
                startActivity(intents);

                break;

            case R.id.settings:

                Intent intentSettings = new Intent(this, Settings.class);
                startActivity(intentSettings);
                break;

            case R.id.help:

                Toast.makeText(MainActivity.this, "Select help", Toast.LENGTH_LONG).show();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addnewmedicine() {
        //Add medicine fab button
        addmedicinefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddMedicine.class);
                startActivity(intent);

            }
        });
    }//ok

    public void startService() {
        Intent intent = new Intent(this, notificationService.class);
        startService(intent);
    }

    public void displayuserdataonprofile() {

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String receivedUri = null;
                Cursor res = mydb.getAllData();

                addmedicinefab.setClickable(false);
                calendarfab.setClickable(false);
                viewprofile = true;

                if (res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Unexpected error occured.", Toast.LENGTH_LONG).show();

                } else {
                    while (res.moveToNext()) {
                        displayname.setText(res.getString(1));
                        displayage.setText(res.getString(2));
                        displayblood.setText(res.getString(3));
                        displayweight.setText(res.getString(4));
                        displayheight.setText(res.getString(5));
                        displaynotes.setText(res.getString(7));

                        receivedUri = res.getString(6);

                        String bmistring = calculateBMI(res.getFloat(4), res.getFloat(5));//calculate the BMI
                        displaybmi.setText(bmistring);

                        RelativeLayout x = (RelativeLayout) findViewById(R.id.viewprofilelayout);
                        x.setVisibility(View.VISIBLE);

                        OkbuttonInProfileView();
                        editButonInProfileView();

                    }


                    try {
                        Uri imageUri = Uri.parse(receivedUri);
                        userpicture.setImageURI(imageUri);
                    } catch (Exception e) {
                        Log.d("Image not found", "This is the found uri : " + receivedUri);
                    }

                }

                res.close();

            }

        });
        mydb.close();


    } //ok


    public void editButonInProfileView() {
        edituserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, UpdateUserprofile.class);
                startActivity(intent);

            }
        });
    }

    public void OkbuttonInProfileView() {
        userok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout p = (RelativeLayout) findViewById(R.id.viewprofilelayout);
                addmedicinefab.setClickable(true);
                calendarfab.setClickable(true);

                p.setVisibility(View.INVISIBLE);

                viewprofile = false;
            }
        });
    } //ok

    //BMI calculation
    public String calculateBMI(float w, float h) {

        float bheight = h / 100;
        float bmivalue = w / (bheight * bheight);

        DecimalFormat decimal = new DecimalFormat("#.##");
        String formattedValue = decimal.format(bmivalue);
        return formattedValue;

    }//ok

    private void adduserdate() {

        RelativeLayout x = (RelativeLayout) findViewById(R.id.profileformlayout);
        x.setVisibility(View.VISIBLE);//Showing start up user details form to get user details
        final String camera = "no";

        save.setOnClickListener(new View.OnClickListener() {//Save input data to database on save button onClick
            @Override
            public void onClick(View v) {

                if (username.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please fill 'Name' field", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isinserted = mydb.insertData(
                            username.getText().toString(),
                            userage.getText().toString(),
                            userbgroup.getText().toString(),
                            userweight.getText().toString(),
                            userheight.getText().toString(),
                            usernotes.getText().toString(),
                            camera);

                    if (isinserted == true) {
                        Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Data not saved", Toast.LENGTH_LONG).show();
                    }

                    RelativeLayout x = (RelativeLayout) findViewById(R.id.profileformlayout);
                    x.setVisibility(View.INVISIBLE);//Hide the user details form after saving data to the database
                }
            }
        });

    } //ok

    //Checking whether the database is exists or not.If not creating a database
    public void startup(String dbName) {

        File database = getApplicationContext().getDatabasePath(dbName);

        if (!database.exists()) {
            mydb = new DatabaseHelper(this);
            Toast.makeText(MainActivity.this, "Database created", Toast.LENGTH_SHORT).show();
            adduserdate();

        } else {
            mydb = new DatabaseHelper(this);
        }
    } //ok

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
        String today = outFormat.format(todayDate);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat later = new SimpleDateFormat("MMM dd, yyyy");
        String dateandyear = later.format(c.getTime());
        TextView nextline = findViewById(R.id.date_and_year);
        nextline.setText(dateandyear);

        SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = e.format(c.getTime());
        String todaynew = e.format(todayDate);//Convert selected date to string (yyyy-MM_dd format)

        SimpleDateFormat daynameFormatter = new SimpleDateFormat("EEEE");
        String selectedDateString = daynameFormatter.format(c.getTime());
        TextView updatedayname = findViewById(R.id.dontknow);
        updatedayname.setText(selectedDateString);

        int i = dateString.compareTo(todaynew);//compare two dates
        List<MedicineHistory> medicineDataReceived;
        // if(){
        //  0 comes when two date are same,
        //  1 comes when date1 is higher then date2
        // -1 comes when date1 is lower then date2
        // medicineDataReceived = mydb.getMedicineDataFromHistoryTable(dateString);
        //mydb.close();
        //int cui = medicineDataReceived.size();
        //Toast.makeText(MainActivity.this, "Found " + cui, Toast.LENGTH_LONG).show();
        //executingRecyclerViewForHistory(medicineDataReceived);

        if (i == 0) {
            updatedayname.setText("Today");
        }

        populateHomeRecycler runnable = new populateHomeRecycler(dateString);
        new Thread(runnable).start();


    } //ok

    public void executingRecyclerViewForHistory(List<MedicineHistory> medicines) {
        List<MedicineHistory> homeListItem;

        homeListItem = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (MedicineHistory c : medicines) {
            MedicineHistory med = new MedicineHistory();
            med.setMedID(c.getMedID());
            med.setUserID(c.getUserID());
            med.setTdate(c.getTdate());
            med.setMedName(c.getMedName());
            med.setMedDose(c.getMedDose());
            med.setMedType(c.getMedType());
            med.setMedFreq(c.getMedFreq());
            med.setMedTime(c.getMedTime());
            med.setMedmorTaken(c.getMedmorTaken());
            med.setMeddayTaken(c.getMeddayTaken());
            med.setMednightTaken(c.getMednightTaken());
            med.setMedDuration(c.getMedDuration());
            med.setMedStartdate(c.getMedStartdate());
            med.setMedEnddate(c.getMedEnddate());
            med.setMedDescrip(c.getMedDescrip());

            homeListItem.add(med);

        }

        homeMedicineHistoryAdpater myAdapter = new homeMedicineHistoryAdpater(this, homeListItem);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }



    private class populateHomeRecycler implements Runnable {

        String dateString;

        populateHomeRecycler(String dateString) {
            this.dateString = dateString;
        }

        @Override
        public void run() {
            Log.d("background status", "Populate home recycler background started.");
            List<MedicineHistory> homeListItems = new ArrayList<>();

            List<MedicineHistory> medicineDataReceived = mydb.getMedicineDataFromHistoryTable(dateString);

            for (MedicineHistory c : medicineDataReceived) {
                MedicineHistory med = new MedicineHistory();
                med.setMedID(c.getMedID());
                med.setUserID(c.getUserID());
                med.setTdate(c.getTdate());
                med.setMedName(c.getMedName());
                med.setMedDose(c.getMedDose());
                med.setMedType(c.getMedType());
                med.setMedFreq(c.getMedFreq());
                med.setMedTime(c.getMedTime());
                med.setMedmorTaken(c.getMedmorTaken());
                med.setMeddayTaken(c.getMeddayTaken());
                med.setMednightTaken(c.getMednightTaken());
                med.setMedDuration(c.getMedDuration());
                med.setMedStartdate(c.getMedStartdate());
                med.setMedEnddate(c.getMedEnddate());
                med.setMedDescrip(c.getMedDescrip());

                homeListItems.add(med);

            }

            Log.d("received", "received number of data : " + homeListItems.size() + " " + medicineDataReceived.size());

            final homeMedicineHistoryAdpater myAdapter = new homeMedicineHistoryAdpater(MainActivity.this, homeListItems);

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            });


        }

    }


    public void settingDateOnHomeScreen() {
        long date = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);
        TextView update = findViewById(R.id.date_and_year);
        update.setText(dateString);

        TextView updatedayname = findViewById(R.id.dontknow);
        updatedayname.setText("Today");


    } //ok

    public void saveDefaultSettings() {
        Cursor cur = mydb.getAllSettings();

        if (cur.getCount() < 1) {
            //Populate settings table with default settings
            mydb.insertDataToSettingsTable(1, "08:30:00", "12:30:00", "19:30:00", "Vibrate with tone", "Vibrate with tone",
                    "Vibrate with tone", null,null);


        } else {
            Log.d("Found settings", "Found default settings");
        }

        cur.close();
        mydb.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewprofile && !drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        }

        if (viewprofile) {
            RelativeLayout x = (RelativeLayout) findViewById(R.id.viewprofilelayout);
            x.setVisibility(View.INVISIBLE);

            addmedicinefab.setClickable(true);
            calendarfab.setClickable(true);

            viewprofile = false;
        }


    }//ok


}