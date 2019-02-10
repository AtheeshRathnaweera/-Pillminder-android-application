package com.atheeshproperty.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewAllMedicine extends AppCompatActivity{

    private medicineViewAdapter myAdapter;
    DatabaseHelper mydb;
    private List<Medicine> medicineList;
    private  List<Medicine> listitem;
    ImageButton back;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_medicine);

        back = (ImageButton) findViewById(R.id.backicon);
        backButtonAction();

        recyclerView = findViewById(R.id.viewMedicineRecyclerView);

        process p = new process();
        p.execute();

       // mydb = new DatabaseHelper(this);

        //RecyclerView recyclerView = findViewById(R.id.viewMedicineRecyclerView);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //medicineList = new ArrayList<>();
        //listitem = new ArrayList<>();

        //medicineList = mydb.getAllMedData();

        /*for(Medicine c:medicineList){
            Medicine med = new Medicine();
            med.setMedID(c.getMedID());
            med.setUserID(c.getUserID());
            med.setMedName(c.getMedName());
            med.setMedType(c.getMedType());
            med.setMedFreq(c.getMedFreq());
            med.setMedDose(c.getMedDose());
            med.setMedTime(c.getMedTime());
            med.setMedDuration(c.getMedDuration());
            med.setMedStartdate(c.getMedStartdate());
            med.setMedEnddate(c.getMedEnddate());
            med.setMedDescrip(c.getMedDescrip());

            listitem.add(med);

        }*/

        //myAdapter = new medicineViewAdapter(this,listitem);
       // recyclerView.setAdapter(myAdapter);
       // myAdapter.notifyDataSetChanged();

    }

    public void backButtonAction(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllMedicine.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewAllMedicine.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    private class process extends AsyncTask< Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Asynch Status","Asynch task preExecute started.");

            mydb = new DatabaseHelper(ViewAllMedicine.this);

            medicineList = new ArrayList<>();
            listitem = new ArrayList<>();

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(ViewAllMedicine.this));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("Background status","Do in background started.");

            Boolean s;

            medicineList = mydb.getAllMedData();

            for(Medicine c:medicineList){
                Medicine med = new Medicine();
                med.setMedID(c.getMedID());
                med.setUserID(c.getUserID());
                med.setMedName(c.getMedName());
                med.setMedType(c.getMedType());
                med.setMedFreq(c.getMedFreq());
                med.setMedDose(c.getMedDose());
                med.setMedTime(c.getMedTime());
                med.setMedDuration(c.getMedDuration());
                med.setMedStartdate(c.getMedStartdate());
                med.setMedEnddate(c.getMedEnddate());
                med.setMedDescrip(c.getMedDescrip());

                listitem.add(med);

            }

            myAdapter = new medicineViewAdapter(ViewAllMedicine.this,listitem);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();

            if(!listitem.isEmpty()){
                Log.d("Received data status","Received data in background.");
                s = true;
            }else{
                s = false;
            }

            return s;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
