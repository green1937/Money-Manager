package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ExportDataActivity extends AppCompatActivity {
    int pricePoint;
    String[] fileExtension = { ".csv", ".json", ".xlsx"};
    String item;
    private final static String FILE_NAME = "output.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);



        Spinner spinner = findViewById(R.id.fileinput);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fileExtension);
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



        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        Gson g = new Gson();

        Button exportBtn = findViewById(R.id.saveFileBtn);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.equals(".json")) {

                    try {
                        File path = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);
                        File output = new File(path, "output" + ".json");
                        System.out.println(output + "ЦЙЦ");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
                        RealmResults<Expense> results = realm.where(Expense.class).findAll();
                        Iterator<Expense> iterator = results.iterator();
                        writer.write("{\"Output\": {");
                        int id=0;
                        while (iterator.hasNext()) {
                            Expense next = realm.copyFromRealm(iterator.next());
                            writer.write("\"" + id + "\": ");
                            writer.write(g.toJson(next));
                            writer.write(",");
                            id+=1;
                        }
                        writer.write("}}");

                        writer.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        ImageButton menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExportDataActivity.this, MenuActivity.class));
            }
        });
    }
}
