package com.example.lostandfoundapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    AppDatabase db;
    List<Advert> advertList;

    FusedLocationProviderClient fusedLocationClient;
    double radiusKm = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "lost-found-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        advertList = db.advertDao().getAllAdverts();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String radiusText = getIntent().getStringExtra("radius");

        if (radiusText != null && !radiusText.isEmpty()) {
            radiusKm = Double.parseDouble(radiusText);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
       float[] results = new float[1];
       android.location.Location.distanceBetween(
               lat1,
               lon1,
               lat2,
               lon2,
               results
       );

       return results[0] / 1000.0;
    }
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (radiusKm > 0) {
            showAdvertsWithinRadius();
        } else {
            showAllAdverts();
        }
    }

    private void showAllAdverts() {
        for (Advert advert : advertList) {
            if (advert.latitude != 0.0 && advert.longitude != 0.0) {
                LatLng advertLocation = new LatLng(advert.latitude, advert.longitude);

                googleMap.addMarker(new MarkerOptions()
                        .position(advertLocation)
                        .title(advert.postType + ": " + advert.name)
                        .snippet(advert.location));
            }
        }

        moveCameraToFirstAdvert();
    }
    private void showAdvertsWithinRadius() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    300);

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double userLat = location.getLatitude();
                        double userLng = location.getLongitude();

                        int markerCount = 0;

                        for (Advert advert : advertList) {
                            if (advert.latitude != 0.0 && advert.longitude != 0.0) {

                                double distance = calculateDistanceKm(
                                        userLat,
                                        userLng,
                                        advert.latitude,
                                        advert.longitude
                                );

                                if (distance <= radiusKm) {
                                    LatLng advertLocation = new LatLng(advert.latitude, advert.longitude);

                                    googleMap.addMarker(new MarkerOptions()
                                            .position(advertLocation)
                                            .title(advert.postType + ": " + advert.name)
                                            .snippet("Distance: " + Math.round(distance * 100.0) / 100.0 + " km"));

                                    markerCount++;
                                }
                            }
                        }

                        LatLng userLocation = new LatLng(userLat, userLng);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));

                        if (markerCount == 0) {
                            Toast.makeText(this, "No adverts found within this radius", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Could not get current location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moveCameraToFirstAdvert() {
        for (Advert advert : advertList) {
            if (advert.latitude != 0.0 && advert.longitude != 0.0) {
                LatLng firstLocation = new LatLng(advert.latitude, advert.longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 13));
                break;
            }
        }
    }
}

