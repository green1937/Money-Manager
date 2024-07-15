package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageButton backBtn = findViewById(R.id.backFromMenu);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });

        Button newPerBtn = findViewById(R.id.newPeriodBtn);
        newPerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NewPeriodActivity.class));
            }
        });

        Button exportBtn = findViewById(R.id.exportExpenses);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ExportDataActivity.class));
            }
        });

        Button seeCurrPeriod = findViewById(R.id.seeCurrPeriod);
        seeCurrPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, CurrentPeriodActivity.class));
            }
        });

        Button currPeriodsAnalysis = findViewById(R.id.analyzeData);
        currPeriodsAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ExpensesAnalysisActivity.class));
            }
        });


        Button graphBtn = findViewById(R.id.viewExpensesGraph);
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, GraphActivity.class));
            }
        });


        Button barBtn = findViewById(R.id.viewExpensesBar);
        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, BarActivity.class));
            }
        });


        Button tableBtn = findViewById(R.id.viewExpensesTable);
        tableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,TableActivity.class));
            }
        });

    }
}
