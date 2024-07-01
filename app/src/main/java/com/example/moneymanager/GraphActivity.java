package com.example.moneymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class GraphActivity extends AppCompatActivity {
    GraphView graph;
    int pricePoint;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_graph);

        graph = (GraphView) findViewById(R.id.graph);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Expense> expensesList = realm.where(Expense.class).findAll();
        long n = realm.where(Expense.class).count();
        int size = (int) n;
        DataPoint[] points = new DataPoint[size];
        int i = 0;

        for (Expense expense : expensesList) {
            pricePoint = expense.getPrice();
            points[i] = new DataPoint(i, pricePoint);
            i+=1;

        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);
    }

}
