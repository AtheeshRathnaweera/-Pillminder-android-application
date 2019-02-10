package com.atheeshproperty.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class medicineViewAdapter extends RecyclerView.Adapter<medicineViewAdapter.medicineViewHolder> {

    private Context mContext;
    private List<Medicine> medData;

    public medicineViewAdapter(Context context, List<Medicine> mData) {

        this.mContext = context;
        this.medData = mData;
    }

    public class medicineViewHolder extends RecyclerView.ViewHolder {

        public TextView medicineNameItem;
        public TextView medicineTypeItem;
        public TextView medicineStartDate;
        public ImageView medicineTypeImage;
        public CardView meditemcard;

        public medicineViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineNameItem = itemView.findViewById(R.id.viewMedicineName);
            medicineTypeItem = itemView.findViewById(R.id.viewMedicinetype);
            medicineStartDate = itemView.findViewById(R.id.viewMedicinestarteddate);
            medicineTypeImage = itemView.findViewById(R.id.medimage);
            meditemcard = itemView.findViewById(R.id.meditemcard);
        }
    }

    @NonNull
    @Override
    public medicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_all_med_item_design, parent, false);
        return new medicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull medicineViewHolder medicineViewHolder, final int position) {

        medicineViewHolder.medicineNameItem.setText(medData.get(position).getMedName());
        medicineViewHolder.medicineTypeItem.setText(medData.get(position).getMedType());
        medicineViewHolder.medicineStartDate.setText(String.format("Started on : %s", medData.get(position).getMedStartdate()));

        String type = medData.get(position).getMedType();

        switch (type){

            case "Tablet" :  medicineViewHolder.medicineTypeImage.setImageResource(R.drawable.tablet);
            break;

            case "Gel" :  medicineViewHolder.medicineTypeImage.setImageResource(R.drawable.gel);
            break;

            case "Capsule" :  medicineViewHolder.medicineTypeImage.setImageResource(R.drawable.capsuleicon);
            break;

            case "Liquid" :  medicineViewHolder.medicineTypeImage.setImageResource(R.drawable.liquid);
            break;

            case "Injection" : medicineViewHolder.medicineTypeImage.setImageResource(R.drawable.injection);
            break;

            case "Add-water powder" : medicineViewHolder.medicineTypeImage.setImageResource(R.drawable.powdericon);
            break;

            default: break;
        }
        medicineViewHolder.meditemcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //passing data the card onClick activity "viewUpdateDeleteAMedicine"
                Intent intent = new Intent(mContext, viewUpdateDeleteAMedicine.class);

                int mID= medData.get(position).getMedID();
                String medid = String.valueOf(mID);

                int uID= medData.get(position).getMedID();
                String userid = String.valueOf(uID);

                intent.putExtra("MedID", medid);
                intent.putExtra("UserID",medData.get(position).getUserID());
                intent.putExtra("MedName", medData.get(position).getMedName());
                intent.putExtra("MedType",medData.get(position).getMedType());
                intent.putExtra("MedFrequency", medData.get(position).getMedFreq());
                intent.putExtra("MedDose", medData.get(position).getMedDose());
                intent.putExtra("MedTime", medData.get(position).getMedTime());
                intent.putExtra("MedDuration", medData.get(position).getMedDuration());
                intent.putExtra("MedStart", medData.get(position).getMedStartdate());
                intent.putExtra("MedDescript", medData.get(position).getMedDescrip());

                mContext.startActivity(intent);

            }
        });

        medicineViewHolder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return medData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
