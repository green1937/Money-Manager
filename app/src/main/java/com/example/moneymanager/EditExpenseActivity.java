package com.example.moneymanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditExpenseActivity extends AppCompatActivity {

    CalendarView calender;
    String item;
    String item2;
    String[] categories = { "Еда", "Транспорт", "Развлечения", "Работа", "Учеба", "Дом", "Одежда", "Спорт", "Здоровье", "Остальное"};
    String[] currencies = {"RUB", "EUR", "USD", "NOK", "JPY"};

    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        EditText timeInput = (EditText) findViewById(R.id.timeexpensetext);
        EditText priceinput = findViewById(R.id.priceinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);

        Button updateBtn = findViewById(R.id.savebtn);
        Bundle arguments = getIntent().getExtras();

        /*Календарь*/
        calender = (CalendarView) findViewById(R.id.calendarView1);


        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                timeInput.setText(dayOfMonth + "." + (month + 1) + "." + year);
            }

        });

        Spinner spinner = findViewById(R.id.categoryinput);
        String category = arguments.getString("category");
        //отсортировать список категорий так, чтобы первым элементом был из аргументс!
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                categories[i] = categories[0];
                categories[0] = category;
            }
        }
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
        String currency = arguments.getString("currency");
        //отсортировать список валют так, чтобы первым элементом был из аргументс!
        for (int i = 0; i < currencies.length; i++) {
            if (currencies[i].equals(currency)) {
                currencies[i] = currencies[0];
                currencies[0] = currency;
            }
        }
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



        ImageView imageView = findViewById(R.id.photoInput);
        String photoUri = arguments.getString("photo_uri");
        if(photoUri!= null) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(photoUri);

            // on below line we are setting bitmap to our image view.
            imageView.setImageBitmap(imgBitmap);
            //Uri photo = Uri.parse(photoUri);
            //imageView.setImageURI(photo);
        }

        //Получение фотографии из галереи
        Button gallery = findViewById(R.id.photoBtn);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });





        int price = Integer.parseInt(arguments.get("price").toString());
        String description = arguments.getString("description");
        String dateExp = arguments.get("dateExp").toString();



        priceinput.setText(String.valueOf(price));
        descriptionInput.setText(description);
        timeInput.setText(dateExp);


        Realm.init(getApplicationContext());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();

                int price_new = Integer.parseInt(priceinput.getText().toString());
                String description_new  = descriptionInput.getText().toString();
                String dateExp_new  = timeInput.getText().toString();

                realm.beginTransaction();
                Expense expense = realm.createObject(Expense.class);
                expense.setPrice(price_new);
                expense.setCurrency(item2);
                expense.setDescription(description_new);
                expense.setDateExp(dateExp_new);
                expense.setCategory(item);

                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Расход сохранен", Toast.LENGTH_SHORT).show();

                realm.beginTransaction();
                Expense expense_old = realm.where(Expense.class).equalTo("price", price).findFirst();
                expense_old.deleteFromRealm();
                realm.commitTransaction();
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