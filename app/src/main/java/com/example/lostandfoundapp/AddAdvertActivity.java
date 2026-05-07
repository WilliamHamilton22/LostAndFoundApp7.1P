package com.example.lostandfoundapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddAdvertActivity extends AppCompatActivity {

    RadioGroup radioGroupType;
    RadioButton radioLost;
    RadioButton radioFound;

    EditText editName;
    EditText editPhone;
    EditText editDescription;
    EditText editDate;
    EditText editLocation;

    EditText editCategory;

    Button buttonSaveAdvert;
    Button buttonChooseImage;

    ImageView imagePreview;
    Uri selectedImageUri;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_advert);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "lost-found-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        radioGroupType = findViewById(R.id.radioGroupType);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        editLocation = findViewById(R.id.editLocation);
        editCategory = findViewById(R.id.editCategory);

        buttonSaveAdvert = findViewById(R.id.buttonSaveAdvert);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imagePreview = findViewById(R.id.imagePreview);

        buttonChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 100);
        });

        buttonSaveAdvert.setOnClickListener(v -> {

            String postType;
            if (radioLost.isChecked()) {
                postType = "Lost";
            } else {
                postType = "Found";
            }

            String name = editName.getText().toString();
            String phone = editPhone.getText().toString();
            String description = editDescription.getText().toString();
            String date = editDate.getText().toString();
            String location = editLocation.getText().toString();
            String category = editCategory.getText().toString();


            String postedTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());


            if (selectedImageUri == null) {
                Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
                return;
            }

            String imageUriString = selectedImageUri.toString();

            Advert advert = new Advert(postType, name, phone, description, date, location, category, postedTime, imageUriString);

            db.advertDao().insertAdvert(advert);

            Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();

            finish();

        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            getContentResolver().takePersistableUriPermission(
                    selectedImageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            imagePreview.setImageURI(selectedImageUri);
        }
    }
}