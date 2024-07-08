package com.example.moneymanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.Entry;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        //Лимиты текущего периода по категориям
        int currPeriodLimit1, currPeriodLimit2, currPeriodLimit3, currPeriodLimit4,
                currPeriodLimit5, currPeriodLimit6, currPeriodLimit7 = 0;
        currPeriodLimit1 = currentPeriod.getPriceFood();
        currPeriodLimit2 = currentPeriod.getPriceTransport();
        currPeriodLimit3 = currentPeriod.getPriceEntertainment();
        currPeriodLimit4 = currentPeriod.getPriceWork();
        currPeriodLimit5 = currentPeriod.getPriceStudy();
        currPeriodLimit6 = currentPeriod.getPriceHealth();
        currPeriodLimit7 = currentPeriod.getPriceOther();

        periodsDate = currentPeriod.getDateStart(); //Дата текущего периода
        int currPeriodsBudget = currentPeriod.getBudget();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
        perLocalDate = LocalDate.parse(periodsDate, formatter);

        currLocalDate = LocalDate.now(); //Сегодняшняя дата
        currentDate = currLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        //ArrayList<Expense> allExpInCurrPeriod = new ArrayList<>();

        int allPriceCurrentPeriod = 0, priceCurrPeriodCtg1 = 0, priceCurrPeriodCtg2 = 0, priceCurrPeriodCtg3 = 0,
        priceCurrPeriodCtg4 = 0, priceCurrPeriodCtg5 = 0, priceCurrPeriodCtg6 = 0, priceCurrPeriodCtg7 = 0;

        currLocalDate = currLocalDate.plusDays(1);
        while(perLocalDate.isBefore(currLocalDate)) {
            String date = perLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            int allPriceInThisDate = Integer.parseInt(realm.where(Expense.class).equalTo("dateExp", date).findAll().sum("price").toString());
            allPriceCurrentPeriod += allPriceInThisDate;

            priceCurrPeriodCtg1 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Еда").equalTo("dateExp", date).findAll().sum("price").toString());
            priceCurrPeriodCtg2 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Транспорт").equalTo("dateExp", date).findAll().sum("price").toString());
            priceCurrPeriodCtg3 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Развлечения").equalTo("dateExp", date).findAll().sum("price").toString());
            priceCurrPeriodCtg4 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Работа").equalTo("dateExp", date).findAll().sum("price").toString());
            priceCurrPeriodCtg5 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Учеба").equalTo("dateExp", date).findAll().sum("price").toString());
            priceCurrPeriodCtg6 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Здоровье").equalTo("dateExp", date).findAll().sum("price").toString());
            priceCurrPeriodCtg7 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Остальное").equalTo("dateExp", date).findAll().sum("price").toString());

            perLocalDate = perLocalDate.plusDays(1);
        }


        //Вывод бюджета текущего периода и расходов за текущий период
        EditText budgetText = findViewById(R.id.currPeriodBudget);
        EditText allPriceExpText = findViewById(R.id.currPeriodExpPrice);
        budgetText.setText(String.valueOf(currPeriodsBudget));
        allPriceExpText.setText(String.valueOf(allPriceCurrentPeriod));

        //Сообщения для пользователя
        String info1 = "";
        if (currPeriodsBudget < allPriceCurrentPeriod) { //если расходы больше, чем бюджет
            info1 = "Сумма расходов превысила бюджет! ";
        }
        else {
            if (currPeriodsBudget == allPriceCurrentPeriod) {
                info1 = "Сумма расходов равна бюджету. ";
            }
            else {
                int diff = currPeriodsBudget - allPriceCurrentPeriod;
                info1 = "Сумма расходов меньше бюджета, разница равна " + diff + ". ";
            }
        }

        //Обработка расходов по категориям (вывод сообщений, если расходы превысили лимиты)
        String info2 = "";
        int diff = 0;
        if (priceCurrPeriodCtg1 > currPeriodLimit1) {
            diff = priceCurrPeriodCtg1 - currPeriodLimit1;
            info2 = info2.concat("Расходы в категории 'Еда' были превышены за текущий период на " + diff + ". ");
        }
        if (priceCurrPeriodCtg2 > currPeriodLimit2) {
            diff = priceCurrPeriodCtg2 - currPeriodLimit2;
            info2 = info2.concat("Расходы в категории 'Транспорт' были превышены за текущий период на " + diff + ". ");
        }
        if (priceCurrPeriodCtg3 > currPeriodLimit3) {
            diff = priceCurrPeriodCtg3 - currPeriodLimit3;
            info2 = info2.concat("Расходы в категории 'Развлечения' были превышены за текущий период на " + diff + ". ");
        }
        if (priceCurrPeriodCtg4 > currPeriodLimit4) {
            diff = priceCurrPeriodCtg4 - currPeriodLimit4;
            info2 = info2.concat("Расходы в категории 'Работа' были превышены за текущий период на " + diff + ". ");
        }
        if (priceCurrPeriodCtg5 > currPeriodLimit5) {
            diff = priceCurrPeriodCtg5 - currPeriodLimit5;
            info2 = info2.concat("Расходы в категории 'Учеба' были превышены за текущий период на " + diff + ". ");
        }
        if (priceCurrPeriodCtg6 > currPeriodLimit6) {
            diff = priceCurrPeriodCtg6 - currPeriodLimit6;
            info2 = info2.concat("Расходы в категории 'Здоровье' были превышены за текущий период на " + diff + ". ");
        }
        if (priceCurrPeriodCtg7 > currPeriodLimit7) {
            diff = priceCurrPeriodCtg7 - currPeriodLimit7;
            info2 = info2.concat("Расходы в категории 'Остальное' были превышены за текущий период на " + diff + ". ");
        }


        System.out.println(info1);
        System.out.println(info2);

        String info = info1.concat(info2); //Объединение всех сообщений

        EditText infoText = findViewById(R.id.currPeriodInfo);
        infoText.setText(info);



        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpensesAnalysisActivity.this, MenuActivity.class));
            }
        });
    }
}
