package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    ListView listViewAdverts;
    AppDatabase db;

    List<Advert> advertList;
    ArrayList<String> advertTitles;
    ArrayAdapter<String> adapter;

    EditText editFilterCategory;
    Button buttonFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_list);

        listViewAdverts = findViewById(R.id.listViewAdverts);
        editFilterCategory = findViewById(R.id.editFilterCategory);
        buttonFilter = findViewById(R.id.buttonFilter);

        listViewAdverts.setOnItemClickListener((parent, view, position, id) -> {
            Advert selectedAdvert = advertList.get(position);

            Intent intent = new Intent(ItemListActivity.this, AdvertDetailActivity.class);
            intent.putExtra("advertId", selectedAdvert.id);
            startActivity(intent);
        });

        buttonFilter.setOnClickListener(v -> {
            String category = editFilterCategory.getText().toString();

            if (category.isEmpty()) {
                advertList = db.advertDao().getAllAdverts();
            } else {
                advertList = db.advertDao().getAdvertsByCategory(category);
            }
            advertTitles.clear();

            for (Advert advert : advertList) {
                advertTitles.add(advert.postType + ": " + advert.name + " (" + advert.category + ")");
            }
            adapter.notifyDataSetChanged();
        });

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "lost-found-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        advertList = db.advertDao().getAllAdverts();

        advertTitles = new ArrayList<>();

        for (Advert advert : advertList) {
            advertTitles.add(advert.postType + ": " + advert.name);
        }
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                advertTitles
        );

        listViewAdverts.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}