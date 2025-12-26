package com.example.spotwarn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TouristDashboardActivity extends AppCompatActivity {

    private CardView cardUnsafeZones, cardTraffic, cardParking, cardTimeline, cardEmergency;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_dashboard);

        // Initialize cards
        cardUnsafeZones = findViewById(R.id.cardUnsafeZones);
        cardTraffic = findViewById(R.id.cardTraffic);
        cardParking = findViewById(R.id.cardParking);
        cardTimeline = findViewById(R.id.cardTimeline);
        cardEmergency = findViewById(R.id.cardEmergency);

        // Click listeners
        cardUnsafeZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristDashboardActivity.this, UnsafeSpotActivity.class));
            }
        });

        cardTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristDashboardActivity.this, TrafficInfoActivity.class));
            }
        });

        cardParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristDashboardActivity.this, ParkingSpotsActivity.class));
            }
        });

        cardTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristDashboardActivity.this, EventTimelineActivity.class));
            }
        });

        cardEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristDashboardActivity.this, EmergencyActivity.class));
            }
        });
    }
}