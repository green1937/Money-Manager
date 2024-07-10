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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class GraphActivity extends AppCompatActivity {
    int pricePoint;
    String[] categories = { "Еда", "Транспорт", "Развлечения", "Работа", "Учеба", "Здоровье", "Остальное"};
    String item;
    String dateStart, dateEnd;
    LocalDate startLocalDate, endLocalDate;
    LineChart lineChartForDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
    ArrayList<Entry> entiersDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_graph);

        lineChartForDate = findViewById(R.id.lineChartForDate);

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


        MaterialButton startDateBtn = findViewById(R.id.DateStart);
        MaterialButton endDateBtn = findViewById(R.id.DateEnd);
        TextView startDateView = findViewById(R.id.tvStart);
        TextView endDateView = findViewById(R.id.tvEnd);

        Button dateBtn = findViewById(R.id.dateBtn);


        startLocalDate = LocalDate.now(); //Сегодняшняя дата
        dateStart = startLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        dateEnd = dateStart;
        startDateView.setText(dateStart);
        endDateView.setText(dateEnd);
        entiersDate = new ArrayList<>();
        entiersDate.add(new Entry(0, 0));
        RealmResults<Expense> expDate = realm.where(Expense.class).equalTo("dateExp", dateStart).findAll();
        i=1;
        for (Expense expense : expDate) {
            pricePoint = expense.getPrice();
            entiersDate.add(new Entry(i, pricePoint));
            i+=1;
        }
        drawGraphForDate(entiersDate);


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
                entiersDate = new ArrayList<>();
                int i = 1;
                entiersDate.add(new Entry(0, 0));
                endLocalDate = endLocalDate.plusDays(1);
                while(startLocalDate.isBefore(endLocalDate)) {
                    String date = startLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    RealmResults<Expense> expDate = realm.where(Expense.class).equalTo("dateExp", date).findAll();

                    for (Expense expense : expDate) {
                        pricePoint = expense.getPrice();
                        entiersDate.add(new Entry(i, pricePoint));
                        i+=1;
                    }
                    startLocalDate = startLocalDate.plusDays(1);
                }
                drawGraphForDate(entiersDate);
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


    public void drawGraphForDate(ArrayList<Entry> entiersDate) {
        LineDataSet lineDataSet = new LineDataSet(entiersDate, "Расходы по выбранным датам");
        lineDataSet.setColors(Color.rgb(70, 250, 121));
        lineDataSet.setCircleColor(Color.BLACK);
        LineData lineData = new LineData(lineDataSet);
        lineChartForDate.setData(lineData);
        lineChartForDate.getDescription().setEnabled(false);
        lineChartForDate.getLegend().setTextSize(8);
        lineChartForDate.animateY(1000);
        lineChartForDate.invalidate();
    }

}
