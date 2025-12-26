package com.example.spotwarn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainDashboard extends AppCompatActivity {

    private Button btnUser, btnPolice, btnAdmin;
    private Spinner spinnerLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply saved locale before loading layout
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));
        setContentView(R.layout.activity_main_dashboard);

        btnUser = findViewById(R.id.btnUser);
        btnPolice = findViewById(R.id.btnPolice);
        btnAdmin = findViewById(R.id.btnAdmin);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        String[] languages = {"English", "हिन्दी"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        String currentLang = LocaleHelper.getLanguage(this);
        spinnerLanguage.setSelection(currentLang.equals("hi") ? 1 : 0);

        spinnerLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedLang = position == 1 ? "hi" : "en";
                if (!selectedLang.equals(currentLang)) {
                    LocaleHelper.setLanguage(MainDashboard.this, selectedLang);
                    recreate(); // reload in new language
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        btnUser.setOnClickListener(v -> startActivity(new Intent(MainDashboard.this, UserDashboard.class)));
        btnPolice.setOnClickListener(v -> startActivity(new Intent(MainDashboard.this, LoginActivity.class)));
        btnAdmin.setOnClickListener(v -> startActivity(new Intent(MainDashboard.this, AdminLogin.class)));
    }

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }
}
