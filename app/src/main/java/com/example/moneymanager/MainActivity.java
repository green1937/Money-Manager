package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    RealmResults<Expense> expensesList; // глобальная переменная
    int price1Filt, price2Filt;
    String ctgFilt, dateFilt;
    ImageButton exportBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });

        //Фильтрация
        Button filterBtn = findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FilterActivity.class));
            }
        });
        Realm.init(getApplicationContext());


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Realm realm = Realm.getDefaultInstance();
        expensesList = realm.where(Expense.class).findAll();
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), expensesList);
        recyclerView.setAdapter(myAdapter);
        expensesList.addChangeListener(new RealmChangeListener<RealmResults<Expense>>() {
            @Override
            public void onChange(RealmResults<Expense> notes) {

                myAdapter.notifyDataSetChanged();
            }
        });
        ImageButton noBtn = findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                expensesList = realm.where(Expense.class).findAll();
                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), expensesList);
                recyclerView.setAdapter(myAdapter);
                expensesList.addChangeListener(new RealmChangeListener<RealmResults<Expense>>() {
                    @Override
                    public void onChange(RealmResults<Expense> notes) {
                        myAdapter.notifyDataSetChanged();
                    }
                });

            }
        });



        ImageButton yesBtn = findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение данных
                Bundle bundle = getIntent().getExtras();
                if(bundle.equals("null")) {
                    price1Filt=0;
                    price2Filt=0;
                    ctgFilt="Все";
                    dateFilt=null;
                }
                else {
                    price1Filt = bundle.getInt("price1Filt");
                    price2Filt = bundle.getInt("price2Filt");
                    ctgFilt = bundle.getString("ctgFilt");
                    dateFilt = bundle.getString("dateFilt");
                }
                System.out.println("see filer DATA = " + price1Filt + "   " + price2Filt + "   " + ctgFilt + "   " + dateFilt);
                System.out.println(price1Filt + "   " + price2Filt);
                if(price1Filt > price2Filt) {
                    int num = price1Filt;
                    price1Filt = price2Filt;
                    price2Filt = num;
                }
                System.out.println(price1Filt + "   " + price2Filt);
                Realm realm = Realm.getDefaultInstance();
                //фильтрация по дате
                if(price1Filt==0 && price2Filt==0 && ctgFilt.equals("Все") && dateFilt==null) {
                    expensesList = realm.where(Expense.class).findAll(); //если все поля пустые
                }

                    if(price1Filt!=0 && price2Filt!=0 && !ctgFilt.equals("Все") && dateFilt!=null) {
                        expensesList = realm.where(Expense.class).equalTo("price", price1Filt).equalTo("price", price2Filt).equalTo("category", ctgFilt).equalTo("dateExp", dateFilt).findAll();
                    }
                    if(price1Filt!=0 && price2Filt!=0 && ctgFilt.equals("Все") && dateFilt==null) {
                        //фильтрация ТОЛЬКО по сумме (диапазон)
                        expensesList = realm.where(Expense.class).between("price", price1Filt, price2Filt).findAll();
                    }
                    if(!ctgFilt.equals("Все") && price1Filt==0 && price2Filt==0 && dateFilt==null) {
                        //фильтрация ТОЛЬКО по категории
                        expensesList = realm.where(Expense.class).equalTo("category", ctgFilt).findAll();
                    }
                    if(dateFilt!=null && price1Filt==0 && price2Filt==0 && ctgFilt.equals("Все")) {
                        //фильтрация ТОЛЬКО по дате
                        expensesList = realm.where(Expense.class).equalTo("dateExp", dateFilt).findAll();
                    }
                    if (ctgFilt.equals("Все") && dateFilt==null) {
                        if (price1Filt != 0) { expensesList = realm.where(Expense.class).equalTo("price", price1Filt).findAll(); }
                        if (price2Filt != 0) { expensesList = realm.where(Expense.class).equalTo("price", price2Filt).findAll(); }
                    }
                    if (!ctgFilt.equals("Все") && dateFilt!=null && price1Filt==0 && price2Filt==0) { expensesList = realm.where(Expense.class).equalTo("category", ctgFilt).equalTo("dateExp", dateFilt).findAll(); }
                    if(price1Filt!=0 && price2Filt==0 && !ctgFilt.equals("Все") && dateFilt!=null){ expensesList = realm.where(Expense.class).equalTo("price", price1Filt).equalTo("category", ctgFilt).equalTo("dateExp", dateFilt).findAll(); }
                    if(price1Filt!=0 && price2Filt!=0 && !ctgFilt.equals("Все") && dateFilt==null){ expensesList = realm.where(Expense.class).between("price", price1Filt, price2Filt).equalTo("category", ctgFilt).findAll(); }
                    if(price1Filt==0 && price2Filt!=0 && !ctgFilt.equals("Все") && dateFilt==null){ expensesList = realm.where(Expense.class).equalTo("price", price2Filt).equalTo("category", ctgFilt).findAll(); }
                    if(price1Filt==0 && price2Filt!=0 && ctgFilt.equals("Все") && dateFilt!=null){ expensesList = realm.where(Expense.class).equalTo("price", price2Filt).equalTo("dateExp", dateFilt).findAll(); }
                    if(price1Filt!=0 && price2Filt==0 && !ctgFilt.equals("Все") && dateFilt==null){ expensesList = realm.where(Expense.class).equalTo("price", price1Filt).equalTo("category", ctgFilt).findAll(); }
                    if(price1Filt!=0 && price2Filt==0 && ctgFilt.equals("Все") && dateFilt!=null){ expensesList = realm.where(Expense.class).equalTo("price", price1Filt).equalTo("dateExp", dateFilt).findAll(); }
                    if(price1Filt==0 && price2Filt!=0 && !ctgFilt.equals("Все") && dateFilt!=null){ expensesList = realm.where(Expense.class).equalTo("price", price2Filt).equalTo("category", ctgFilt).equalTo("dateExp", dateFilt).findAll(); }

                if(price1Filt!=0 && price2Filt!=0 && ctgFilt.equals("Все") && dateFilt==null) {
                    //фильтрация ТОЛЬКО по сумме (диапазон)
                    expensesList = realm.where(Expense.class).between("price", price1Filt, price2Filt).findAll();
                }
                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), expensesList);
                recyclerView.setAdapter(myAdapter);
                expensesList.addChangeListener(new RealmChangeListener<RealmResults<Expense>>() {
                    @Override
                    public void onChange(RealmResults<Expense> notes) {
                        myAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

        ImageButton addExpBtn = findViewById(R.id.addnewTrans);
        addExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddExpenseActivity.class));
            }
        });
        exportBtn = findViewById(R.id.exportBtn);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDataActivity.csvExport(expensesList, realm);
                ExportDataActivity.xlsxExport(expensesList);
                Toast.makeText(getApplicationContext(), "Данные экспортированы в файлы " +
                        "с расширениями json, csv и xlsx в папку Загрузки",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}