package com.atheeshproperty.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class homeMedicineDisplayAdapter extends RecyclerView.Adapter<homeMedicineDisplayAdapter.homeViewHolder> {

    private Context myContext;
    private List<Medicine> cardData;

    public homeMedicineDisplayAdapter(Context myContext, List<Medicine> cardData) {

        this.myContext = myContext;
        this.cardData = cardData;
    }

    public static class homeViewHolder extends RecyclerView.ViewHolder {

        public TextView homeMedName;
        public TextView homeMedQty;
        public TextView homeMedTime;
        public TableRow morningRow;
        public TableRow dayRow;
        public TableRow nightRow;

        public homeViewHolder(View itemView) {
            super(itemView);

            homeMedName = itemView.findViewById(R.id.diplayMedicineName);
            homeMedQty = itemView.findViewById(R.id.viewMedicinetype);
            homeMedTime = itemView.findViewById(R.id.viewMedicinetakeTime);
            morningRow = itemView.findViewById(R.id.morningRow);
            dayRow = itemView.findViewById(R.id.dayRow);
            nightRow = itemView.findViewById(R.id.nightRow);

        }
    }

    @NonNull
    @Override
    public homeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
        LayoutInflater myinflater = LayoutInflater.from(myContext);
        View view = myinflater.inflate(R.layout.home_medlist_display_item_layout, parent, false);
        return new homeViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull homeViewHolder homeViewHolder, int viewtype) {

        Integer medid = cardData.get(viewtype).getMedID();

            String e = cardData.get(viewtype).getMedDose() + " " + cardData.get(viewtype).getMedType();

            homeViewHolder.homeMedName.setText(cardData.get(viewtype).getMedName());
            homeViewHolder.homeMedQty.setText(e);
            homeViewHolder.homeMedTime.setText(cardData.get(viewtype).getMedTime());

            String freq = cardData.get(viewtype).getMedFreq();

            // (1-Morning) (3-day) (6-night) (4-morning+day) (7-morning+night) (9-day+night) (10-all)

            if (freq.equals("1") || freq.equals("4") || freq.equals("7") || freq.equals("10")) {
                homeViewHolder.morningRow.setVisibility(View.VISIBLE);
            }
            if (freq.equals("3") || freq.equals("4") || freq.equals("9") || freq.equals("10")) {
                homeViewHolder.dayRow.setVisibility(View.VISIBLE);
            }
            if (freq.equals("6") || freq.equals("7") || freq.equals("9") || freq.equals("10")) {
                homeViewHolder.nightRow.setVisibility(View.VISIBLE);
            }
        }


    @Override
    public int getItemCount() {
        return cardData.size();
    }


}
