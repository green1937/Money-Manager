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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
import retrofit2.http.GET;


//import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Request;
import okhttp3.Response;

public class AddExpenseActivity extends AppCompatActivity {

    CalendarView calender;
    EditText eTxt;
    String item;
    String item2;
    String[] categories = { "Еда", "Транспорт", "Развлечения", "Работа", "Учеба", "Здоровье", "Остальное"};
    String[] currencies = {"RUB", "EUR", "USD", "NOK", "JPY"};
    Uri selectedImage;
    //String eurT;
    Double usdValue, eurValue, nokValue, jpyValue;


    Button get;
    TextView answer;



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

        /*EditText priceinput = findViewById(R.id.priceinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);
        EditText timeInput = eTxt;*/
        Button saveBtn = findViewById(R.id.savebtn);

        //Realm.init(getApplicationContext());
        //Realm realm = Realm.getDefaultInstance();
        answer = findViewById(R.id.answer);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=1;
                try {
                    getHttpResponse();
                    System.out.println("eqwdfdg1");
                    flag=0;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*System.out.println("eqwdfdg2");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String photoUri = selectedImage.toString();
                int price = Integer.parseInt(priceinput.getText().toString());
                String description = descriptionInput.getText().toString();
                String dateExp = timeInput.getText().toString();
                System.out.println("eqwdfdg3" + eurValue + "price" + price);
                if ((!item2.equals("RUB")) && (flag==0)) { //если валюта выбрана не рубли, то переводим в рубли
                    if (item2.equals("EUR"))  price = (int) Math.round( price * eurValue);
                    if (item2.equals("USD"))  price = (int) Math.round( price * usdValue);
                    if (item2.equals("NOK"))  price = (int) Math.round( price * nokValue);
                    if (item2.equals("JPY"))  price = (int) Math.round( price * jpyValue);
                }
                if (flag==1) {
                    Toast.makeText(getApplicationContext(), "Текущий расход будет сохранен в валюте рубли!", Toast.LENGTH_SHORT).show();
                }

                item2="RUB";
                realm.beginTransaction();
                Expense expense = realm.createObject(Expense.class);
                expense.setPrice(price);
                expense.setCurrency(item2);
                expense.setPhoto_uri(photoUri);
                expense.setDescription(description);
                expense.setDateExp(dateExp);
                expense.setCategory(item);

                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Расход сохранен", Toast.LENGTH_SHORT).show();
                finish();
                */
            }
        });


    }

    public void getHttpResponse() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://www.cbr-xml-daily.ru/daily_json.js")
                .method("GET", null)
                .addHeader("Cookie", "__cfduid=dc5403bef7ac2ab2cb8ead288d39f653e1586600122")
                .build();
        client.newCall(request).enqueue(new Callback() {
            // если запрос неудачный
            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                System.out.println("ASDFGFHHGGGGGGGGGGGGGGGGGGGGGGGGGGGG" + mMessage);
                 answer.post(new Runnable() {
                    @Override
                    public void run() {
                        answer.setText(mMessage);
                    }
                });

            }

            // В случае успешного запроса
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();

                try {
                    JSONObject json = new JSONObject(mMessage);
                    JSONObject json2 = json.getJSONObject("Valute");
                    JSONObject usd = json2.getJSONObject("USD");
                    JSONObject eur = json2.getJSONObject("EUR");
                    JSONObject nok = json2.getJSONObject("NOK");
                    JSONObject jpy = json2.getJSONObject("JPY");

                    String usdT = usd.getString("Value");
                    String eurT = eur.getString("Value");
                    String nokT = nok.getString("Value");
                    String jpyT = jpy.getString("Value");
                    System.out.println("ASDFGFHHGGGGGGGGGGGGGGGGGGGGGGGGGGGG" + jpyT);
                    checkCurrency(usdT, eurT, nokT, jpyT);

                    // Выводим значение

                    answer.post(new Runnable() {
                        @Override
                        public void run() {
                            answer.setText(usdT);
                            System.out.println(eurT + "  " + nokT + "  " + jpyT);

                        }
                    });


                } catch (JSONException e) {
                    //
                }
            }
        });
    }


    public void checkCurrency(String usdT, String eurT, String nokT, String jpyT) {
        eurValue = Double.valueOf(eurT);
        usdValue = Double.valueOf(usdT);
        nokValue = Double.valueOf(nokT);
        jpyValue = Double.valueOf(jpyT);
        System.out.println("eqwdfdg0" + eurValue);
        EditText priceinput = findViewById(R.id.priceinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);
        EditText timeInput = eTxt;
        Button saveBtn = findViewById(R.id.savebtn);
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        String photoUri = selectedImage.toString();
        int price = Integer.parseInt(priceinput.getText().toString());
        String description = descriptionInput.getText().toString();
        String dateExp = timeInput.getText().toString();
        System.out.println("eqwdfdg3" + eurValue + "price" + price);
        if (!item2.equals("RUB")) { //если валюта выбрана не рубли, то переводим в рубли
            if (item2.equals("EUR"))  price = (int) Math.round( price * eurValue);
            if (item2.equals("USD"))  price = (int) Math.round( price * usdValue);
            if (item2.equals("NOK"))  price = (int) Math.round( price * nokValue);
            if (item2.equals("JPY"))  price = (int) Math.round( price * jpyValue);
        }

        item2="RUB";
        realm.beginTransaction();
        Expense expense = realm.createObject(Expense.class);
        expense.setPrice(price);
        expense.setCurrency(item2);
        expense.setPhoto_uri(photoUri);
        expense.setDescription(description);
        expense.setDateExp(dateExp);
        expense.setCategory(item);

        realm.commitTransaction();
        //Toast.makeText(getApplicationContext(), "Расход сохранен", Toast.LENGTH_SHORT).show();
        finish();

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
