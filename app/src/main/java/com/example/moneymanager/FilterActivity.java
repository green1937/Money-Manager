package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;


public class FilterActivity extends AppCompatActivity {
    CalendarView calender;
    EditText eTxt;
    String item;
    String[] categories = { "Все", "Еда", "Транспорт", "Развлечения", "Работа", "Учеба", "Здоровье", "Остальное"};
    //EditText eTxt2;
    CalendarView calender2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_exp);

        //возвращаемся назад на главный экран
        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilterActivity.this, MainActivity.class));
            }
        });

        calender = (CalendarView) findViewById(R.id.calendarView1);
        eTxt = (EditText) findViewById(R.id.startDate);
        //eTxt2 = (EditText) findViewById(R.id.endDate);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                if ((month + 1 < 10) &&(dayOfMonth >= 10)) {
                    eTxt.setText(dayOfMonth + ".0" + (month + 1) + "." + year);
                }
                if ((dayOfMonth < 10) && (month + 1 < 10)) {
                    eTxt.setText("0" + dayOfMonth + ".0" + (month + 1) + "." + year);
                }
                if ((month + 1 >= 10) && (dayOfMonth < 10)) {
                    eTxt.setText("0" + dayOfMonth + "." + (month + 1) + "." + year);
                }
                if ((month + 1 >= 10) && (dayOfMonth >= 10)) {
                    eTxt.setText(dayOfMonth + "." + (month + 1) + "." + year);
                }
            }

        });

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


        //переходим на главный экран
        MaterialButton applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Отправка данных
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);

                EditText price1Input = findViewById(R.id.price1Input); //сумма ОТ
                int price1 = 0;
                if (!price1Input.getText().toString().isEmpty()) {
                    price1 = Integer.parseInt(price1Input.getText().toString());
                }
                intent.putExtra("price1Filt", price1);

                EditText price2Input = findViewById(R.id.price2Input); //сумма ДО
                int price2 = 0;
                if (!price2Input.getText().toString().isEmpty()) {
                    price2 = Integer.parseInt(price2Input.getText().toString());
                }
                intent.putExtra("price2Filt", price2);

                intent.putExtra("ctgFilt", item);

                EditText startDate = findViewById(R.id.startDate); //дата
                String date = null;
                if (!startDate.getText().toString().isEmpty()) {
                    date = startDate.getText().toString();
                }
                intent.putExtra("dateFilt", date);
                FilterActivity.this.startActivity(intent);

            }
        });
    }
}
