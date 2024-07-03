package com.example.moneymanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class BarActivity extends AppCompatActivity {
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_bar);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        int priceCtg1 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Еда").findAll().sum("price").toString());
        int priceCtg2 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Транспорт").findAll().sum("price").toString());
        int priceCtg3 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Развлечения").findAll().sum("price").toString());
        int priceCtg4 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Работа").findAll().sum("price").toString());
        int priceCtg5 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Учеба").findAll().sum("price").toString());
        int priceCtg6 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Здоровье").findAll().sum("price").toString());
        int priceCtg7 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Остальное").findAll().sum("price").toString());

        int[] colors = {
                Color.rgb(78, 173, 0),
                Color.rgb(251, 165, 10),
                Color.rgb(250, 70, 121),
                Color.rgb(251, 212, 10),
                Color.rgb(153, 177, 255),
                Color.rgb(192, 250, 75),
                Color.rgb(159, 204, 204)};

        BarChart barChart = findViewById(R.id.graph);
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(0, priceCtg1, "Еда"));
        entries1.add(new BarEntry(1, priceCtg2, "Транспорт"));
        entries1.add(new BarEntry(2, priceCtg3, "Развлечения"));
        entries1.add(new BarEntry(3, priceCtg4, "Работа"));
        entries1.add(new BarEntry(4, priceCtg5, "Учеба"));
        entries1.add(new BarEntry(5, priceCtg6, "Здоровье"));
        entries1.add(new BarEntry(6, priceCtg7, "Остальное"));

        BarDataSet barDataSet = new BarDataSet(entries1, "Категории");
        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();


        //КРУГОВАЯ ДИАГРАММА
        PieChart pieChart = findViewById(R.id.cyrcleDiagram);
        ArrayList<PieEntry> entiers = new ArrayList<>();
        entiers.add(new PieEntry(priceCtg1, "Еда"));
        entiers.add(new PieEntry(priceCtg2, "Транспорт"));
        entiers.add(new PieEntry(priceCtg3, "Развлечения"));
        entiers.add(new PieEntry(priceCtg4, "Работа"));
        entiers.add(new PieEntry(priceCtg5, "Учеба"));
        entiers.add(new PieEntry(priceCtg6, "Здоровье"));
        entiers.add(new PieEntry(priceCtg7, "Остальное"));


        PieDataSet pieDataSet = new PieDataSet(entiers, "Категории");

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.getCenterTextOffset();

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextSize(8);
        pieChart.animateY(1000);
        pieChart.invalidate();



        Button tableBtn = findViewById(R.id.tableBtn);
        tableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BarActivity.this, TableActivity.class));
            }
        });

        Button graphBtn = findViewById(R.id.graphBtn);
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BarActivity.this, GraphActivity.class));
            }
        });

        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BarActivity.this, MenuActivity.class));
            }
        });

    }
}
