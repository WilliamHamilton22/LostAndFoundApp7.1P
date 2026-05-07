package com.example.lostandfoundapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AdvertDao {

    @Insert
    void insertAdvert(Advert advert);

    @Query("SELECT * FROM adverts")
    List<Advert> getAllAdverts();

    @Query("SELECT * FROM adverts WHERE category LIKE '%' || :category || '%'")
    List<Advert> getAdvertsByCategory(String category);

    @Query("SELECT * FROM adverts WHERE id = :advertId LIMIT 1")
    Advert getAdvertById(int advertId);

    @Delete
    void deleteAdvert(Advert advert);
}