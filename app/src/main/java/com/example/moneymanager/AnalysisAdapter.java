package com.example.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.MyViewHolder> {

    Context context;
    RealmResults<Period> periodList;
    ArrayList<ArrayList<Integer>> expListInCurrPeriod;
    ArrayList<String> dateEndForPeriods, infoAboutPeriods;


    public  AnalysisAdapter(Context context, RealmResults<Period> periodList,
                            ArrayList<ArrayList<Integer>> expListInCurrPeriod,
                            ArrayList<String> dateEndForPeriods,
                            ArrayList<String> infoAboutPeriods) {
        this.context = context;
        this.periodList = periodList;
        this.expListInCurrPeriod = expListInCurrPeriod;
        this.dateEndForPeriods = dateEndForPeriods;
        this.infoAboutPeriods = infoAboutPeriods;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.period_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (expListInCurrPeriod != null && expListInCurrPeriod.size() > 0 && periodList != null && periodList.size() > 0) {
            Period period = periodList.get(position);
            ArrayList<Integer> expListInPeriod = expListInCurrPeriod.get(position);
            String dateEnd = dateEndForPeriods.get(position);
            String info = infoAboutPeriods.get(position);


            String time = period.dateStart + " - " + dateEnd;
            holder.timePeriod_tv.setText(time);  // время периода

            holder.allExp_tv.setText(String.valueOf(expListInPeriod.get(0)));  // все расходы в периоде
            holder.budget_tv.setText(String.valueOf(period.getBudget()));  // бюджет периода


            // категории
            holder.ctg1_tv.setText(String.valueOf(expListInPeriod.get(1)));
            holder.periodCtg1_tv.setText(String.valueOf(period.getPriceFood()));

            holder.ctg2_tv.setText(String.valueOf(expListInPeriod.get(2)));
            holder.periodCtg2_tv.setText(String.valueOf(period.getPriceTransport()));

            holder.ctg3_tv.setText(String.valueOf(expListInPeriod.get(3)));
            holder.periodCtg3_tv.setText(String.valueOf(period.getPriceEntertainment()));

            holder.ctg4_tv.setText(String.valueOf(expListInPeriod.get(4)));
            holder.periodCtg4_tv.setText(String.valueOf(period.getPriceWork()));

            holder.ctg5_tv.setText(String.valueOf(expListInPeriod.get(5)));
            holder.periodCtg5_tv.setText(String.valueOf(period.getPriceStudy()));

            holder.ctg6_tv.setText(String.valueOf(expListInPeriod.get(6)));
            holder.periodCtg6_tv.setText(String.valueOf(period.getPriceHealth()));

            holder.ctg7_tv.setText(String.valueOf(expListInPeriod.get(7)));
            holder.periodCtg7_tv.setText(String.valueOf(period.getPriceOther()));

            holder.infoPeriod_tv.setText(info);  // информация о периоде


        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return periodList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timePeriod_tv, infoPeriod_tv;
        TextView allExp_tv, ctg1_tv, ctg2_tv, ctg3_tv, ctg4_tv, ctg5_tv, ctg6_tv, ctg7_tv;

        TextView budget_tv, periodCtg1_tv, periodCtg2_tv, periodCtg3_tv, periodCtg4_tv, periodCtg5_tv, periodCtg6_tv, periodCtg7_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timePeriod_tv = itemView.findViewById(R.id.timeOut);
            infoPeriod_tv = itemView.findViewById(R.id.infoOut);

            allExp_tv = itemView.findViewById(R.id.expOut);
            budget_tv = itemView.findViewById(R.id.budgetOut);

            //categories
            ctg1_tv = itemView.findViewById(R.id.foodExpOut);
            periodCtg1_tv = itemView.findViewById(R.id.foodPeroidOut);

            ctg2_tv = itemView.findViewById(R.id.transpExpOut);
            periodCtg2_tv = itemView.findViewById(R.id.transpPeroidOut);

            ctg3_tv = itemView.findViewById(R.id.entertExpOut);
            periodCtg3_tv = itemView.findViewById(R.id.entertPeroidOut);

            ctg4_tv = itemView.findViewById(R.id.workExpOut);
            periodCtg4_tv = itemView.findViewById(R.id.workPeroidOut);

            ctg5_tv = itemView.findViewById(R.id.studyExpOut);
            periodCtg5_tv = itemView.findViewById(R.id.studyPeroidOut);

            ctg6_tv = itemView.findViewById(R.id.healthExpOut);
            periodCtg6_tv = itemView.findViewById(R.id.healthPeroidOut);

            ctg7_tv = itemView.findViewById(R.id.otherExpOut);
            periodCtg7_tv = itemView.findViewById(R.id.otherPeroidOut);
        }
    }

}