package com.example.moneymanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class AddExpenseActivity extends AppCompatActivity {
    CalendarView calender;
    EditText eTxt;
    String item;
    String item2;
    String[] categories = { "Еда", "Транспорт", "Развлечения", "Работа", "Учеба", "Здоровье", "Остальное"};
    String[] currencies = {"RUB", "EUR", "USD", "NOK", "JPY"};
    Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);


        calender = (CalendarView) findViewById(R.id.calendarView1);
        eTxt = (EditText) findViewById(R.id.timeexpensetext);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                eTxt.setText(dayOfMonth + "." + (month + 1) + "." + year);
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




        Spinner spinner2 = findViewById(R.id.currencyinput);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies);
        // Определяем разметку для использования при выборе элемента
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner2.setAdapter(adapter2);
        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                item2 = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner2.setOnItemSelectedListener(itemSelectedListener2);



        //Получение фотографии из галереи
        Button gallery = findViewById(R.id.photoBtn);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        System.out.println("seeeeeee 2 ----- " + selectedImage);


        EditText priceinput = findViewById(R.id.priceinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);
        EditText timeInput = eTxt;
        Button saveBtn = findViewById(R.id.savebtn);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("seeeeeee 3 ----- " + selectedImage);
                String photoUri = selectedImage.toString();
                System.out.println("seeeeeee 4 ----- " + photoUri);
                int price = Integer.parseInt(priceinput.getText().toString());
                String description = descriptionInput.getText().toString();
                String dateExp = timeInput.getText().toString();

                realm.beginTransaction();
                Expense expense = realm.createObject(Expense.class);
                expense.setPrice(price);
                expense.setCurrency(item2);
                expense.setPhoto_uri(photoUri);
                expense.setDescription(description);
                expense.setDateExp(dateExp);
                expense.setCategory(item);

                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Expense saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.photoInput);
            imageView.setImageURI(selectedImage);
            System.out.println("seeeeeee 1 ----- " + selectedImage);
        }



    }
}
