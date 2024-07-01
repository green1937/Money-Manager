package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;

public class BarActivity extends AppCompatActivity {
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_bar);

        graph = (GraphView) findViewById(R.id.graph);


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
