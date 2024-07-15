package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class ExpensesAnalysisActivity extends AppCompatActivity {
    static Realm realm;
    static String periodsDate;
    static String currentDate;
    static LocalDate perLocalDate;
    static LocalDate currLocalDate;
    Period currentPeriod;
    static ArrayList<Integer> limList, expListInCurrPeriod;
    static ArrayList<String> dateEndForPeriods, infoAboutPeriod;

    static ArrayList<ArrayList<Integer>> expListInPeriod;
    int periodSize;
    static int allPriceCurrentPeriod, priceCurrPeriodCtg1, priceCurrPeriodCtg2, priceCurrPeriodCtg3,
            priceCurrPeriodCtg4, priceCurrPeriodCtg5, priceCurrPeriodCtg6, priceCurrPeriodCtg7;
    static int currPeriodLimit1, currPeriodLimit2, currPeriodLimit3, currPeriodLimit4,
            currPeriodLimit5, currPeriodLimit6, currPeriodLimit7;
    static int currPeriodsBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_analysis);


        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        dateEndForPeriods = new ArrayList<>();
        infoAboutPeriod = new ArrayList<>();

        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpensesAnalysisActivity.this, MenuActivity.class));
            }
        });

        expListInPeriod = new ArrayList<>();
        RealmResults<Period> allPeriods = realm.where(Period.class).findAll();

        for(Period onePeriod : allPeriods) {
            expListInPeriod = getListOfExpInAllPeriod(onePeriod);
            infoAboutPeriod = getInfoAboutPeriods(onePeriod);
        }



        Realm.init(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerviewAnalysis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AnalysisAdapter analysisAdapter = new AnalysisAdapter(getApplicationContext(), allPeriods, expListInPeriod, dateEndForPeriods, infoAboutPeriod);
        recyclerView.setAdapter(analysisAdapter);
        allPeriods.addChangeListener(new RealmChangeListener<RealmResults<Period>>() {
            @Override
            public void onChange(RealmResults<Period> period) {
                analysisAdapter.notifyDataSetChanged();
            }
        });
        System.out.println(allPeriods);


    }

    public static ArrayList<Integer> limitsInCurrPeriod(Period currentPeriod) {
        //Лимиты текущего периода по категориям
        currPeriodLimit1 = 0; currPeriodLimit2 = 0; currPeriodLimit3 = 0; currPeriodLimit4 = 0;
        currPeriodLimit5 = 0; currPeriodLimit6 = 0; currPeriodLimit7 = 0;

        limList = new ArrayList<>();
        currPeriodLimit1 = currentPeriod.getPriceFood();
        currPeriodLimit2 = currentPeriod.getPriceTransport();
        currPeriodLimit3 = currentPeriod.getPriceEntertainment();
        currPeriodLimit4 = currentPeriod.getPriceWork();
        currPeriodLimit5 = currentPeriod.getPriceStudy();
        currPeriodLimit6 = currentPeriod.getPriceHealth();
        currPeriodLimit7 = currentPeriod.getPriceOther();


        limList.add(currentPeriod.getPriceFood());
        limList.add(currentPeriod.getPriceTransport());
        limList.add(currentPeriod.getPriceEntertainment());
        limList.add(currentPeriod.getPriceWork());
        limList.add(currentPeriod.getPriceStudy());
        limList.add(currentPeriod.getPriceHealth());
        limList.add(currentPeriod.getPriceOther());
        System.out.println("LIMITS  -----  " + limList);
        return limList;
    }

    public static ArrayList<Integer> expInCurrPeriod(Period currentPeriod, ArrayList<String> dateEndForPeriods) {
        periodsDate = currentPeriod.getDateStart();  // Дата начала текущего периода
        currPeriodsBudget = currentPeriod.getBudget();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
        perLocalDate = LocalDate.parse(periodsDate, formatter);

        String currDate = currentPeriod.getDateEnd();  // Дата конца текущего периода
        if (currDate == null) {
            currLocalDate = LocalDate.now(); //Сегодняшняя дата
            currentDate = currLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        else {
            currLocalDate = LocalDate.parse(currDate, formatter);
            currentDate = currDate;
        }
        dateEndForPeriods.add(currentDate);

        allPriceCurrentPeriod = 0; priceCurrPeriodCtg1 = 0; priceCurrPeriodCtg2 = 0; priceCurrPeriodCtg3 = 0;
                priceCurrPeriodCtg4 = 0; priceCurrPeriodCtg5 = 0; priceCurrPeriodCtg6 = 0; priceCurrPeriodCtg7 = 0;
        currLocalDate = currLocalDate.plusDays(1);

        realm = Realm.getDefaultInstance();
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
        expListInCurrPeriod = new ArrayList<>();
        expListInCurrPeriod.add(allPriceCurrentPeriod);
        expListInCurrPeriod.add(priceCurrPeriodCtg1);
        expListInCurrPeriod.add(priceCurrPeriodCtg2);
        expListInCurrPeriod.add(priceCurrPeriodCtg3);
        expListInCurrPeriod.add(priceCurrPeriodCtg4);
        expListInCurrPeriod.add(priceCurrPeriodCtg5);
        expListInCurrPeriod.add(priceCurrPeriodCtg6);
        expListInCurrPeriod.add(priceCurrPeriodCtg7);

        return expListInCurrPeriod;

    }


    public static ArrayList<ArrayList<Integer>> getListOfExpInAllPeriod(Period onePeriod) {
        ArrayList<Integer> expList = expInCurrPeriod(onePeriod, dateEndForPeriods);   // Все расходы в текущем периоде
        expListInPeriod.add(expList);
        return expListInPeriod;
    }

    public static ArrayList<String> getInfoAboutPeriods(Period onePeriod) {
        String info = "";  // Сообщения для пользователя
        if (currPeriodsBudget < allPriceCurrentPeriod) {  // если расходы больше, чем бюдже
            int diff = allPriceCurrentPeriod - currPeriodsBudget;// т
            info = "Сумма расходов превысила бюджет на " + diff + "!";
        }
        else {
            if (currPeriodsBudget == allPriceCurrentPeriod) {
                info = "Сумма расходов равна бюджету.";
            }
            else {
                int diff = currPeriodsBudget - allPriceCurrentPeriod;
                info = "Сумма расходов меньше бюджета, разница равна " + diff + ".";
            }
        }
        infoAboutPeriod.add(info);
        return infoAboutPeriod;
    }
}
