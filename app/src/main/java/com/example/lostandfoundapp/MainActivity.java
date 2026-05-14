package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button buttonCreateAdvert;
    Button buttonShowItems;
    Button buttonShowMap;
    EditText editRadius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        buttonCreateAdvert = findViewById(R.id.buttonCreateAdvert);
        buttonShowItems = findViewById(R.id.buttonShowItems);
        buttonShowMap = findViewById(R.id.buttonShowMap);
        editRadius = findViewById(R.id.editRadius);

        buttonCreateAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAdvertActivity.class);
            startActivity(intent);
        });

        buttonShowItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(intent);
        });

        buttonShowMap.setOnClickListener(v -> {
            String radiusText = editRadius.getText().toString();

            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra("radius", radiusText);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}