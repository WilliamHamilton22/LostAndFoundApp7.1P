package com.example.lostandfoundapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "adverts")
public class Advert {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String postType;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;

    public String category;
    public String postedTime;
    public String imageUri;

    public double latitude;
    public double longitude;

    public Advert(String postType, String name, String phone, String description, String date, String location, String category, String postedTime, String imageUri, double latitude, double longitude) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.postedTime = postedTime;
        this.imageUri = imageUri;
        this.longitude = longitude;
        this.latitude = latitude;

    }
}
