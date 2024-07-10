package com.example.moneymanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class GraphActivity extends AppCompatActivity {
    int pricePoint;
    String[] categories = { "Еда", "Транспорт", "Развлечения", "Работа", "Учеба", "Здоровье", "Остальное"};
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_graph);


        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Expense> expensesList = realm.where(Expense.class).findAll();

        LineChart lineChart = findViewById(R.id.lineChart);
        ArrayList<Entry> entiers = new ArrayList<>();
        entiers.add(new Entry(0, 0));
        int i = 1;
        for (Expense expense : expensesList) {
            pricePoint = expense.getPrice();
            entiers.add(new Entry(i, pricePoint));
            i+=1;
        }

        LineDataSet lineDataSet = new LineDataSet(entiers, "Расходы");

        lineDataSet.setColors(Color.rgb(250, 70, 121));
        lineDataSet.setCircleColor(Color.BLACK);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setTextSize(8);
        lineChart.animateY(1000);
        lineChart.invalidate();



        Spinner spinner = findViewById(R.id.categoryinput);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                item = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);



        //Когда конкретная категория не выбрана, то рисуется график с данными из первой категории, то есть категории "Еда"
        RealmResults<Expense> expListWithCtg = realm.where(Expense.class).equalTo("category", "Еда").findAll();
        LineChart lineChartWithCtg = findViewById(R.id.lineChartForCtg);
        ArrayList<Entry> entiersCtg = new ArrayList<>();
        entiersCtg.add(new Entry(0, 0));
        i = 1;
        for (Expense expense : expListWithCtg) {
            pricePoint = expense.getPrice();
            entiersCtg.add(new Entry(i, pricePoint));
            i+=1;
        }

        LineDataSet lineDataSet2 = new LineDataSet(entiersCtg, "Расходы в категории Еда");

        lineDataSet2.setColors(Color.BLUE);
        lineDataSet2.setCircleColor(Color.BLACK);
        LineData lineData2 = new LineData(lineDataSet2);
        lineChartWithCtg.setData(lineData2);
        lineChartWithCtg.getDescription().setEnabled(false);
        lineChartWithCtg.getLegend().setTextSize(8);
        lineChartWithCtg.animateY(1000);
        lineChartWithCtg.invalidate();

        Button chooseCtgBtn = findViewById(R.id.ctgBtn);
        chooseCtgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Expense> expListWithCtg = realm.where(Expense.class).equalTo("category", item).findAll();
                LineChart lineChartWithCtg = findViewById(R.id.lineChartForCtg);
                ArrayList<Entry> entiersCtg2 = new ArrayList<>();
                entiersCtg2.add(new Entry(0, 0));
                int i = 1;
                for (Expense expense : expListWithCtg) {
                    pricePoint = expense.getPrice();
                    entiersCtg2.add(new Entry(i, pricePoint));
                    i+=1;
                }

                LineDataSet lineDataSet2 = new LineDataSet(entiersCtg2, "Расходы в категории "+item);

                lineDataSet2.setColors(Color.BLUE);
                lineDataSet2.setCircleColor(Color.BLACK);
                LineData lineData2 = new LineData(lineDataSet2);
                lineChartWithCtg.setData(lineData2);
                lineChartWithCtg.getDescription().setEnabled(false);
                lineChartWithCtg.getLegend().setTextSize(8);
                lineChartWithCtg.animateY(1000);
                lineChartWithCtg.invalidate();
            }
        });

        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GraphActivity.this, MenuActivity.class));
            }
        });
    }

}
