package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;

public class ExpensesAnalysisActivity extends AppCompatActivity {
    static Realm realm;
    static String periodsDate;
    static String currentDate;
    static LocalDate perLocalDate;
    static LocalDate currLocalDate;
    Period currentPeriod;
    static ArrayList<Integer> limList, expListInCurrPeriod;
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
        currentPeriod = realm.where(Period.class).findAll().last(); //текущий период


        limList = limitsInCurrPeriod(currentPeriod);            // Лимиты
        expListInCurrPeriod = expInCurrPeriod(currentPeriod);   // Все расходы в текущем периоде


        // Вывод бюджета текущего периода и расходов за текущий период
        EditText budgetText = findViewById(R.id.currPeriodBudget);
        EditText allPriceExpText = findViewById(R.id.currPeriodExpPrice);
        budgetText.setText(String.valueOf(currPeriodsBudget));
        allPriceExpText.setText(String.valueOf(allPriceCurrentPeriod));


        String info1 = "";  // Сообщения для пользователя
        if (currPeriodsBudget < allPriceCurrentPeriod) {  // если расходы больше, чем бюджет
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

    public static ArrayList<Integer> expInCurrPeriod(Period currentPeriod) {
        periodsDate = currentPeriod.getDateStart(); //Дата текущего периода
        currPeriodsBudget = currentPeriod.getBudget();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
        perLocalDate = LocalDate.parse(periodsDate, formatter);

        currLocalDate = LocalDate.now(); //Сегодняшняя дата
        currentDate = currLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
        expListInCurrPeriod.add(priceCurrPeriodCtg1);
        expListInCurrPeriod.add(priceCurrPeriodCtg2);
        expListInCurrPeriod.add(priceCurrPeriodCtg3);
        expListInCurrPeriod.add(priceCurrPeriodCtg4);
        expListInCurrPeriod.add(priceCurrPeriodCtg5);
        expListInCurrPeriod.add(priceCurrPeriodCtg6);
        expListInCurrPeriod.add(priceCurrPeriodCtg7);

        return expListInCurrPeriod;

    }
}
