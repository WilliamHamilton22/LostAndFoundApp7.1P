package com.example.lostandfoundapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AdvertDetailActivity extends AppCompatActivity {

    TextView textPostType;
    TextView textName;
    TextView textPhone;
    TextView textDescription;
    TextView textDate;
    TextView textLocation;
    TextView textCategory;
    TextView textPostedTime;
    ImageView imageDetail;


    Button buttonRemoveAdvert;

    AppDatabase db;
    Advert selectedAdvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);

        textPostType = findViewById(R.id.textPostType);
        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        textDescription = findViewById(R.id.textDescription);
        textDate = findViewById(R.id.textDate);
        textLocation = findViewById(R.id.textLocation);
        buttonRemoveAdvert = findViewById(R.id.buttonRemoveAdvert);
        textCategory = findViewById(R.id.textCategory);
        textPostedTime = findViewById(R.id.textPostedTime);
        imageDetail = findViewById(R.id.imageDetail);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "lost-found-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        int advertId = getIntent().getIntExtra("advertId", -1);

        if (advertId != -1) {
            selectedAdvert = db.advertDao().getAdvertById(advertId);

            if (selectedAdvert != null) {
                textPostType.setText("Post Type: " + selectedAdvert.postType);
                textName.setText("Name: " + selectedAdvert.name);
                textPhone.setText("Phone: " + selectedAdvert.phone);
                textDescription.setText("Description: " + selectedAdvert.description);
                textDate.setText("Date: " + selectedAdvert.date);
                textLocation.setText("Location: " + selectedAdvert.location);
                textCategory.setText("Category: " + selectedAdvert.category);
                textPostedTime.setText("Posted: " + selectedAdvert.postedTime);

                if (selectedAdvert.imageUri != null && !selectedAdvert.imageUri.isEmpty()) {
                    imageDetail.setImageURI(Uri.parse(selectedAdvert.imageUri));
                }
            }
        }
        buttonRemoveAdvert.setOnClickListener(v -> {
            if (selectedAdvert != null) {
                db.advertDao().deleteAdvert(selectedAdvert);

                Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}