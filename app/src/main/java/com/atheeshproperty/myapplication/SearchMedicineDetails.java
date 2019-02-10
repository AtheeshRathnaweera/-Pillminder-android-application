package com.atheeshproperty.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchMedicineDetails extends AppCompatActivity {

    RelativeLayout relativeL;
    private Handler mainHandler = new Handler();
    FloatingActionButton search;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_medicine_list_layout);//Reuse the layout that used for view all medicine activity

        TextView titles = findViewById(R.id.title);
        search = findViewById(R.id.searchMedFab);
        back = findViewById(R.id.backicon);

        titles.setText("Know about your medicines");

        getMedicineNames runnable = new getMedicineNames();
        new Thread(runnable).start();//Starting the thread

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMedicineDetails.this,ShowGoogleSearchResults.class);
                intent.putExtra("MedName"," ");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private class getMedicineNames implements Runnable {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            Log.d("status", "Thread started.");

            DatabaseHelper jh = new DatabaseHelper(SearchMedicineDetails.this);
            SQLiteDatabase rec = jh.getOne();
            ArrayList<String> data = new ArrayList<>();

            String query = "SELECT MED_NAME FROM MedDetails";

            @SuppressLint("Recycle")
            Cursor res = rec.rawQuery(query, null, null);

            if (res.getCount() > 0) {
                Log.d("status", "There is data" + res.getCount());
            } else {
                Log.d("status", "No data");
            }

            res.moveToFirst();

            while (!res.isAfterLast()) {
                data.add(res.getString(res.getColumnIndex("MED_NAME")));
                res.moveToNext();
            }

            res.close();
            rec.close();


            Log.d("result", "This is the first name: " + data.get(0) + " This is the second" + data.get(1) + " third:" + data.get(2));

            final RecyclerView mm = findViewById(R.id.viewMedicineRecyclerView);
            final SearchMedicineRecyclerAdapter myAdapter = new SearchMedicineRecyclerAdapter(SearchMedicineDetails.this, data);
            //mm.setAdapter(myAdapter);
            // mm.setLayoutManager(new GridLayoutManager(SearchMedicineDetails.this,2));

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mm.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                    mm.setLayoutManager(new GridLayoutManager(SearchMedicineDetails.this, 2));
                }
            });


        }
    }


}


