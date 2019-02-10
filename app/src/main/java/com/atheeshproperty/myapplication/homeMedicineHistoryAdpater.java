package com.atheeshproperty.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class homeMedicineHistoryAdpater extends RecyclerView.Adapter<homeMedicineHistoryAdpater.historyViewHolder> {

    private Context myContext;
    private List<MedicineHistory> cardData;
    DatabaseHelper mydb;

    public homeMedicineHistoryAdpater(Context myContext, List<MedicineHistory> cardData) {
        this.myContext = myContext;
        this.cardData = cardData;
    }


    public static class historyViewHolder extends RecyclerView.ViewHolder {

        public TextView homeMedName;
        public TextView homeMedQty;
        public TextView homeMedTime;
        public TableRow morningRow;
        public TableRow dayRow;
        public TableRow nightRow;
        public CardView itemCard;
        public ImageButton morningTickIcon;
        public ImageButton dayTickIcon;
        public ImageButton nightTickIcon;


        public historyViewHolder(View itemView) {
            super(itemView);
            homeMedName = itemView.findViewById(R.id.diplayMedicineName);
            homeMedQty = itemView.findViewById(R.id.viewMedicinetype);
            homeMedTime = itemView.findViewById(R.id.viewMedicinetakeTime);
            morningRow = itemView.findViewById(R.id.morningRow);
            dayRow = itemView.findViewById(R.id.dayRow);
            nightRow = itemView.findViewById(R.id.nightRow);
            itemCard = itemView.findViewById(R.id.meditemcard);
            morningTickIcon = itemView.findViewById(R.id.tickMorning);
            dayTickIcon = itemView.findViewById(R.id.tickDay);
            nightTickIcon = itemView.findViewById(R.id.tickNight);

        }


    }

    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
        LayoutInflater myinflater = LayoutInflater.from(myContext);
        View view = myinflater.inflate(R.layout.home_medlist_display_item_layout, parent, false);
        return new historyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final historyViewHolder historyViewHolder, final int viewtype) {

        mydb = new DatabaseHelper(myContext);
        String e = cardData.get(viewtype).getMedDose() + " " + cardData.get(viewtype).getMedType();

        historyViewHolder.homeMedName.setText(cardData.get(viewtype).getMedName());
        historyViewHolder.homeMedQty.setText(e);
        historyViewHolder.homeMedTime.setText(cardData.get(viewtype).getMedTime());

        String freq = cardData.get(viewtype).getMedFreq();
        final String morningTake = cardData.get(viewtype).getMedmorTaken();
        final String dayTake = cardData.get(viewtype).getMeddayTaken();
        final String nightTake = cardData.get(viewtype).getMednightTaken();

        // (1-Morning) (3-day) (6-night) (4-morning+day) (7-morning+night) (9-day+night) (10-all)

        if (freq.equals("1") || freq.equals("4") || freq.equals("7") || freq.equals("10")) {
            historyViewHolder.morningRow.setVisibility(View.VISIBLE);
        }
        if (freq.equals("3") || freq.equals("4") || freq.equals("9") || freq.equals("10")) {
            historyViewHolder.dayRow.setVisibility(View.VISIBLE);
        }
        if (freq.equals("6") || freq.equals("7") || freq.equals("9") || freq.equals("10")) {
            historyViewHolder.nightRow.setVisibility(View.VISIBLE);
        }


        historyViewHolder.itemCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Dialog openDialog = new Dialog(myContext);
                openDialog.setContentView(R.layout.customdialogbox);

                TextView text = (TextView) openDialog.findViewById(R.id.dialogText);
                Button yes = (Button) openDialog.findViewById(R.id.firstOption);
                Button no = (Button) openDialog.findViewById(R.id.secondOption);

                text.setText(R.string.removeMedicinetext);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int medidforDelete = cardData.get(viewtype).getMedID();
                        String date = cardData.get(viewtype).getTdate();
                        String medid = Integer.toString(medidforDelete);
                        int res = mydb.deleteMedicineFromHistoryTableInLongPress(medid, date);

                        if (res > 0) {
                            Toast.makeText(myContext, "Removed successfully! ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(myContext, "Error occured", Toast.LENGTH_LONG).show();
                        }
                        openDialog.dismiss();


                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(myContext, " " + morningTake + dayTake + nightTake, Toast.LENGTH_LONG).show();
                        openDialog.cancel();
                    }
                });
                openDialog.show();
                //AlertDialog deleteAlert = builder.create();
                //deleteAlert.show();

                return true;
            }
        });

        final String date = cardData.get(viewtype).getTdate();
        final Integer medid = cardData.get(viewtype).getMedID();
        final String medID = Integer.toString(medid);

        List<String> tr = mydb.getTakenValue(date, medID);

        //final int semiTransparentGrey = Color.argb(155, 185, 185, 185);
        //drawable.setColorFilter(semiTransparentGrey, PorterDuff.Mode.SRC_ATOP);

        if (tr.get(0) != null && tr.get(0).equals("1")) {
            historyViewHolder.morningTickIcon.setColorFilter(Color.RED);
        }else {
            historyViewHolder.morningTickIcon.setColorFilter(Color.TRANSPARENT);
        }

        if (tr.get(1) != null && tr.get(1).equals("1")) {
            historyViewHolder.dayTickIcon.setColorFilter(Color.RED);
        }else {
            historyViewHolder.dayTickIcon.setColorFilter(Color.TRANSPARENT);
        }

        if (tr.get(2) != null && tr.get(2).equals("1")) {
            historyViewHolder.nightTickIcon.setColorFilter(Color.RED);
        }else {
            historyViewHolder.nightTickIcon.setColorFilter(Color.TRANSPARENT);
        }


        historyViewHolder.morningTickIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tr = mydb.getTakenValue(date, medID);

                if (tr.get(0) != null && tr.get(0).equals("1")) {
                    historyViewHolder.morningTickIcon.setColorFilter(Color.TRANSPARENT);
                    Boolean re = mydb.updateMorningTaken(date, medID, "0");
                    //Toast.makeText(myContext, "This " + cardData.get(viewtype).getMedName() + tr.get(0), Toast.LENGTH_LONG).show();

                } else {
                    historyViewHolder.morningTickIcon.setColorFilter(Color.RED);
                    mydb.updateMorningTaken(date, medID, "1");
                    // Toast.makeText(myContext, "This " + cardData.get(viewtype).getMedName(), Toast.LENGTH_LONG).show();
                }

            }
        });

        historyViewHolder.dayTickIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tr = mydb.getTakenValue(date, medID);

                if (tr.get(1) != null && tr.get(1).equals("1")) {
                    historyViewHolder.dayTickIcon.setColorFilter(Color.TRANSPARENT);
                    Boolean re = mydb.updateDayTaken(date, medID, "0");
                    // Toast.makeText(myContext, "This " + cardData.get(viewtype).getMedName()+re, Toast.LENGTH_LONG).show();

                } else {
                    historyViewHolder.dayTickIcon.setColorFilter(Color.RED);
                    mydb.updateDayTaken(date, medID, "1");
                    // Toast.makeText(myContext, "This " + cardData.get(viewtype).getMedName(), Toast.LENGTH_LONG).show();
                }

            }
        });

        historyViewHolder.nightTickIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tr = mydb.getTakenValue(date, medID);

                if (tr.get(2) != null && tr.get(2).equals("1")) {
                    historyViewHolder.nightTickIcon.setColorFilter(Color.TRANSPARENT);
                    Boolean re = mydb.updateNightTaken(date, medID, "0");
                    //Toast.makeText(myContext, "This " + cardData.get(viewtype).getMedName()+re, Toast.LENGTH_LONG).show();

                } else {
                    historyViewHolder.nightTickIcon.setColorFilter(Color.RED);
                    Boolean res = mydb.updateNightTaken(date, medID, "1");
                   //Toast.makeText(myContext, "This " + cardData.get(viewtype).getMedName()+res, Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cardData.size();
    }


}
