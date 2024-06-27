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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu menu = new PopupMenu(context, v);
                menu.getMenu().add("Удалить");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Удалить")) {
                            // удаление расхода
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            expense.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(context, "Расход удалена", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                menu.show();
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditExpenseActivity.class);
                intent.putExtra("price", expense.price);
                intent.putExtra("currency", expense.currency);
                intent.putExtra("description", expense.description);
                intent.putExtra("category", expense.category);
                intent.putExtra("dateExp", expense.dateExp);
                intent.putExtra("photo_uri", expense.Photo_uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
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