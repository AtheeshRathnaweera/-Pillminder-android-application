package com.atheeshproperty.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchMedicineRecyclerAdapter extends RecyclerView.Adapter<SearchMedicineRecyclerAdapter.searchViewHolder> {

    private Context mContext;
    private ArrayList<String> rData;

    public SearchMedicineRecyclerAdapter(Context context, ArrayList<String> data){
        this.mContext = context;
        this.rData = data;

    }
    @NonNull
    @Override
    public searchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.search_medicine_item, viewGroup,false);
        return new searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchViewHolder searchViewHolder, final int i) {

        searchViewHolder.itemName.setText(rData.get(i));

        searchViewHolder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ShowGoogleSearchResults.class);
                intent.putExtra("MedName",rData.get(i));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rData.size();
    }

    public static class searchViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;

        public searchViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.medName);
        }
    }
}
