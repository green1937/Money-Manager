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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ExportDataActivity extends AppCompatActivity {
    int pricePoint;
    String[] fileExtension = { ".csv", ".xlsx"};
    String item;
    private final static String FILE_NAME = "output.json";

    RealmResults<Expense> expensesList;
    Realm realm;

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
        realm = Realm.getDefaultInstance();
        expensesList = realm.where(Expense.class).findAll();
        Button exportBtn = findViewById(R.id.saveFileBtn);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.equals(".csv")) {
                    csvExport(expensesList, realm);
                }
                if (item.equals(".xlsx")) {
                    xlsxExport(expensesList);
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

    /*
    Функция экспорта истории расхода в файл с расширением csv
     */
    public static void csvExport(RealmResults<Expense> expensesList, Realm realm) {

        Gson g = new Gson();
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File output = new File(path, "output" + ".json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(output, false));

            Iterator<Expense> iterator = expensesList.iterator();
            writer.write("[");
            int id=0;
            int size = expensesList.size();//realm.where(Expense.class).findAll().size();
            while (iterator.hasNext()) {
                Expense next = realm.copyFromRealm(iterator.next());
                //writer.write("\"" + id + "\": ");
                writer.write(g.toJson(next));
                if(id<size-1) {
                    writer.write(",");}
                id+=1;
            }
            writer.write("]");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        JsonNode jsonTree;
        try {
            jsonTree = new ObjectMapper().readTree(new File(path, "output" + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(csvSchemaBuilder::addColumn);
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN,true);
        try {
            csvMapper.writerFor(JsonNode.class)
                    .with(csvSchema)
                    .writeValue(new File(path, "output" + ".csv"), jsonTree);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    Функция экспорта истории расхода в файл с расширением xlsx
     */
    public static void xlsxExport(RealmResults<Expense> expensesList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("expence Details");

        Map<String, Object[]> data = new TreeMap<String, Object[]>();

        data.put("1", new Object[] { "PRICE", "CURRENCY", "CATEGORY", "DESCRIPTION", "DATE", "PHORO_URI" });

        int price;
        String currency, ctg, descr, date, photo;
        int i = 2;

        for (Expense expense : expensesList) {
            price = expense.getPrice();
            currency = expense.getCurrency();
            ctg = expense.getCategory();
            descr = expense.getDescription();
            date = expense.getDateExp();
            photo = expense.getPhoto_uri();
            data.put(String.valueOf(i), new Object[] { price, currency, ctg, descr, date, photo });
            i+=1;
        }


        Set<String> keyset = data.keySet();
        int rownum = 0;

        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;

            for (Object obj : objArr) {

                Cell cell = row.createCell(cellnum++);

                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                }

                else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                }
            }
        }

        try {

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            FileOutputStream out = new FileOutputStream(new File(path, "output.xlsx"));
            workbook.write(out);
            out.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
