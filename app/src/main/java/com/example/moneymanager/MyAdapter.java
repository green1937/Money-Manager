package com.example.moneymanager;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    RealmResults<Expense> expensesList;

    public  MyAdapter(Context context, RealmResults<Expense> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Expense expense = expensesList.get(position);
        holder.dateExpOutput.setText(expense.getDateExp());
        holder.categoryOutput.setText(expense.getCategory());
        holder.priceOutput.setText(String.valueOf(expense.getPrice()));
        holder.currencyOutput.setText(expense.getCurrency());
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateExpOutput;
        TextView categoryOutput;
        TextView priceOutput;
        TextView currencyOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateExpOutput = itemView.findViewById(R.id.dateOut);
            categoryOutput = itemView.findViewById(R.id.categoryOut);
            priceOutput = itemView.findViewById(R.id.priceOut);
            currencyOutput = itemView.findViewById(R.id.currencyOut);
        }
    }

}