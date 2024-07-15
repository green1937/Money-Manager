package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class NewPeriodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_period);
        ImageButton backBtn = findViewById(R.id.menu);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPeriodActivity.this, MenuActivity.class));
            }
        });
        EditText allBudget = findViewById(R.id.budgetInput);
        EditText food = findViewById(R.id.ctg1InputPrice);
        EditText transport = findViewById(R.id.ctg2InputPrice);
        EditText entertainment = findViewById(R.id.ctg3InputPrice);
        EditText work = findViewById(R.id.ctg4InputPrice);
        EditText study = findViewById(R.id.ctg5InputPrice);
        EditText heath = findViewById(R.id.ctg6InputPrice);
        EditText other = findViewById(R.id.ctg7InputPrice);
        Button saveBtn = findViewById(R.id.savebtn);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                // Форматирование времени как "день.месяц.год"
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateStart = dateFormat.format(currentDate);

                if (realm.where(Period.class).findAll().size() > 0) {
                    realm.beginTransaction();
                    Period lastPeriod = realm.where(Period.class).findAll().last();
                    lastPeriod.setDateEnd(dateStart);  // дата конца прошлого периода равна дате начала нового периода
                    realm.commitTransaction();
                }

                int budget = Integer.parseInt(allBudget.getText().toString());
                int priceFood = Integer.parseInt(food.getText().toString());
                int priceTransport = Integer.parseInt(transport.getText().toString());
                int priceEntertainment = Integer.parseInt(entertainment.getText().toString());
                int priceWork = Integer.parseInt(work.getText().toString());
                int priceStudy = Integer.parseInt(study.getText().toString());
                int priceHealth = Integer.parseInt(heath.getText().toString());
                int priceOther = Integer.parseInt(other.getText().toString());


                realm.beginTransaction();
                Period period = realm.createObject(Period.class);
                period.setDateStart(dateStart);
                period.setBudget(budget);
                period.setPriceFood(priceFood);
                period.setPriceTransport(priceTransport);
                period.setPriceEntertainment(priceEntertainment);
                period.setPriceWork(priceWork);
                period.setPriceStudy(priceStudy);
                period.setPriceHealth(priceHealth);
                period.setPriceOther(priceOther);

                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Period saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
