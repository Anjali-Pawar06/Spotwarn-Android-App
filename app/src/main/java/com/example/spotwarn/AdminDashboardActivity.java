package com.example.spotwarn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardAddParking, cardViewParking, cardAddTimeline, cardViewTimeline, cardLogout;
    private FirebaseAuth mAuth;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Link UI elements
        cardAddParking = findViewById(R.id.cardAddParking);
        cardViewParking = findViewById(R.id.cardViewParking);
        cardAddTimeline = findViewById(R.id.cardAddTimeline);
        cardViewTimeline = findViewById(R.id.cardViewTimeline);
        cardLogout = findViewById(R.id.cardLogout);

        // Handle Add Parking click
        cardAddParking.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddParkingActivity.class);
            startActivity(intent);
        });

        // Handle View Parking click
        cardViewParking.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ViewParkingActivity.class);
            startActivity(intent);
        });

        // Handle Add Timeline click
        cardAddTimeline.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // Handle View Timeline click
        cardViewTimeline.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, EventTimelineActivity.class);
            startActivity(intent);
        });

        // Handle Logout click
        cardLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(AdminDashboardActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }
}
