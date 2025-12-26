package com.example.spotwarn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UnsafeSpotActivity extends AppCompatActivity {
    private Button btnAddSpot, btnViewSpots;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_spots);

        btnAddSpot = findViewById(R.id.btnAddSpot);
        btnViewSpots = findViewById(R.id.btnViewSpots);

        // Navigate to Add Spot
        btnAddSpot.setOnClickListener(v -> {
            Intent intent = new Intent(UnsafeSpotActivity.this, AddUnsafeSpotActivity.class);
            startActivity(intent);
        });

        // Navigate to View Spots
        btnViewSpots.setOnClickListener(v -> {
            Intent intent = new Intent(UnsafeSpotActivity.this, ViewUnsafeSpotsActivity.class);
            startActivity(intent);
        });
    }
}
