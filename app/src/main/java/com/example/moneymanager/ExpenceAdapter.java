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

import io.realm.Realm;
import io.realm.RealmResults;

public class ExpenceAdapter extends RecyclerView.Adapter<ExpenceAdapter.MyViewHolder> {

    Context context;
    RealmResults<Expense> expensesList;

    public  ExpenceAdapter(Context context, RealmResults<Expense> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (expensesList != null && expensesList.size() > 0) {
            Expense expense = expensesList.get(position);
            holder.date_tv.setText(expense.getDateExp());
            holder.ctg_tv.setText(expense.getCategory());
            holder.price_tv.setText(String.valueOf(expense.getPrice()));
        } else {
            return;
        }
    }


    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView id_tv;
        TextView date_tv;
        TextView ctg_tv;
        TextView price_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //id_tv = itemView.findViewById(R.id.id_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            ctg_tv = itemView.findViewById(R.id.ctg_tv);
            price_tv = itemView.findViewById(R.id.price_tv);
        }
    }

}