package com.example.moneymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;

public class CurrentPeriodActivity extends AppCompatActivity {
    RealmResults<Period> periodsList; // глобальная переменная

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_detail_period);

        EditText dateText = findViewById(R.id.dateStartPeriod);
        EditText budgetText = findViewById(R.id.budgetInput);
        EditText foodText = findViewById(R.id.ctg1InputPrice);
        EditText trasnpText = findViewById(R.id.ctg2InputPrice);
        EditText entertText = findViewById(R.id.ctg3InputPrice);
        EditText workText = findViewById(R.id.ctg4InputPrice);
        EditText studyText = findViewById(R.id.ctg5InputPrice);
        EditText healthText = findViewById(R.id.ctg6InputPrice);
        EditText otherText = findViewById(R.id.ctg7InputPrice);

        Realm.init(getApplicationContext());

        Realm realm = Realm.getDefaultInstance();
        periodsList = realm.where(Period.class).findAll();
        System.out.println("Все имеющиеся у нас периоды ----- " + periodsList);
        Period currPeriod = periodsList.last();
        System.out.println("А это текущий период (т.е. последний созданный) -----" + currPeriod);


        dateText.setText(currPeriod.getDateStart());
        budgetText.setText(String.valueOf(currPeriod.getBudget()));
        foodText.setText(String.valueOf(currPeriod.getPriceFood()));
        trasnpText.setText(String.valueOf(currPeriod.getPriceTransport()));
        entertText.setText(String.valueOf(currPeriod.getPriceEntertainment()));
        workText.setText(String.valueOf(currPeriod.getPriceWork()));
        studyText.setText(String.valueOf(currPeriod.getPriceStudy()));
        healthText.setText(String.valueOf(currPeriod.getPriceHealth()));
        otherText.setText(String.valueOf(currPeriod.getPriceOther()));


    }

}
