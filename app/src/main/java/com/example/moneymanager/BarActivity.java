package com.example.moneymanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class BarActivity extends AppCompatActivity {
    String dateStart, dateEnd;
    LocalDate startLocalDate, endLocalDate;
    LineChart lineChartForDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
    BarChart barChart;
    PieChart pieChart;

    ArrayList<BarEntry> entries;
    ArrayList<BarEntry> entries1;
    int priceCtg1, priceCtg2, priceCtg3, priceCtg4, priceCtg5, priceCtg6, priceCtg7;
    int[] colors = {
            Color.rgb(78, 173, 0),
            Color.rgb(251, 165, 10),
            Color.rgb(250, 70, 121),
            Color.rgb(251, 212, 10),
            Color.rgb(153, 177, 255),
            Color.rgb(192, 250, 75),
            Color.rgb(159, 204, 204)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_bar);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Period.class).findAll().size() > 0) {

            priceCtg1 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Еда").findAll().sum("price").toString());
            priceCtg2 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Транспорт").findAll().sum("price").toString());
            priceCtg3 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Развлечения").findAll().sum("price").toString());
            priceCtg4 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Работа").findAll().sum("price").toString());
            priceCtg5 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Учеба").findAll().sum("price").toString());
            priceCtg6 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Здоровье").findAll().sum("price").toString());
            priceCtg7 = Integer.parseInt(realm.where(Expense.class).equalTo("category", "Остальное").findAll().sum("price").toString());


            barChart = findViewById(R.id.graph);
            drawBarChart(priceCtg1, priceCtg2, priceCtg3, priceCtg4, priceCtg5, priceCtg6, priceCtg7);


            //КРУГОВАЯ ДИАГРАММА
            pieChart = findViewById(R.id.cyrcleDiagram);
            drawPieChart(priceCtg1, priceCtg2, priceCtg3, priceCtg4, priceCtg5, priceCtg6, priceCtg7);


            MaterialButton startDateBtn = findViewById(R.id.DateStart);
            MaterialButton endDateBtn = findViewById(R.id.DateEnd);
            TextView startDateView = findViewById(R.id.tvStart);
            TextView endDateView = findViewById(R.id.tvEnd);

            Button dateBtn = findViewById(R.id.dateBtn);


            startLocalDate = LocalDate.now(); //Сегодняшняя дата
            dateStart = realm.where(Period.class).findAll().last().getDateStart();
            dateEnd = startLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            startDateView.setText("-");
            endDateView.setText("-");


            startDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Выберите дату начала").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();
                    materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            dateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(selection));
                            startDateView.setText(MessageFormat.format("{0}", dateStart));
                        }
                    });
                    materialDatePicker.show(getSupportFragmentManager(), "tag");
                }
            });

            endDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Выберите дату конца").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();
                    materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            dateEnd = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(selection));
                            endDateView.setText(MessageFormat.format("{0}", dateEnd));
                        }
                    });
                    materialDatePicker.show(getSupportFragmentManager(), "tag");
                }
            });

            dateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateStart.compareTo(dateEnd) > 0) { //Если дата начала больше даты конца -> меняем местами
                        String s = dateStart;
                        dateStart = dateEnd;
                        dateEnd = s;
                    }
                    startLocalDate = LocalDate.parse(dateStart, formatter);
                    endLocalDate = LocalDate.parse(dateEnd, formatter);

                    endLocalDate = endLocalDate.plusDays(1);
                    int priceCtg1 = 0, priceCtg2 = 0, priceCtg3 = 0, priceCtg4 = 0, priceCtg5 = 0, priceCtg6 = 0, priceCtg7 = 0;
                    while (startLocalDate.isBefore(endLocalDate)) {
                        String date = startLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

                        priceCtg1 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Еда").equalTo("dateExp", date).findAll().sum("price").toString());
                        priceCtg2 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Транспорт").equalTo("dateExp", date).findAll().sum("price").toString());
                        priceCtg3 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Развлечения").equalTo("dateExp", date).findAll().sum("price").toString());
                        priceCtg4 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Работа").equalTo("dateExp", date).findAll().sum("price").toString());
                        priceCtg5 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Учеба").equalTo("dateExp", date).findAll().sum("price").toString());
                        priceCtg6 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Здоровье").equalTo("dateExp", date).findAll().sum("price").toString());
                        priceCtg7 += Integer.parseInt(realm.where(Expense.class).equalTo("category", "Остальное").equalTo("dateExp", date).findAll().sum("price").toString());

                        startLocalDate = startLocalDate.plusDays(1);
                    }
                    drawBarChart(priceCtg1, priceCtg2, priceCtg3, priceCtg4, priceCtg5, priceCtg6, priceCtg7);
                    drawPieChart(priceCtg1, priceCtg2, priceCtg3, priceCtg4, priceCtg5, priceCtg6, priceCtg7);
                }
            });
        }



        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BarActivity.this, MenuActivity.class));
            }
        });

    }

    public void drawBarChart (int priceCtg1, int priceCtg2, int priceCtg3, int priceCtg4, int priceCtg5, int priceCtg6, int priceCtg7) {
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
    }


    public void drawPieChart(int priceCtg1, int priceCtg2, int priceCtg3, int priceCtg4, int priceCtg5, int priceCtg6, int priceCtg7) {
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
    }

}
