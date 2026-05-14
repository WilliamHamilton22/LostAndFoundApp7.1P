package com.example.lostandfoundapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Advert.class}, version = 5)

public abstract class AppDatabase extends RoomDatabase {

    public abstract AdvertDao advertDao();
}
