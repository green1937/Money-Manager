package com.example.moneymanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class ExpensesAnalysisActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_analysis);

        String periodsDate, currentDate;
        LocalDate perLocalDate, currLocalDate;
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        Period currentPeriod = realm.where(Period.class).findAll().last(); //текущий период


        periodsDate = currentPeriod.getDateStart(); //Дата текущего периода
        int currPeriodsBudget = currentPeriod.getBudget();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
        perLocalDate = LocalDate.parse(periodsDate, formatter);

        currLocalDate = LocalDate.now(); //Сегодняшняя дата
        currentDate = currLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


        int allPriceCurrentPeriod = 0;
        currLocalDate = currLocalDate.plusDays(1);
        while(perLocalDate.isBefore(currLocalDate)) {
            String date = perLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            int allPriceInThisDate = Integer.parseInt(realm.where(Expense.class).equalTo("dateExp", date).findAll().sum("price").toString());
            allPriceCurrentPeriod += allPriceInThisDate;
            perLocalDate = perLocalDate.plusDays(1);
        }

        //Вывод бюджета текущего периода и расходов за текущий период
        EditText budgetText = findViewById(R.id.currPeriodBudget);
        EditText allPriceExpText = findViewById(R.id.currPeriodExpPrice);
        budgetText.setText(String.valueOf(currPeriodsBudget));
        allPriceExpText.setText(String.valueOf(allPriceCurrentPeriod));


        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpensesAnalysisActivity.this, MenuActivity.class));
            }
        });
    }
}
