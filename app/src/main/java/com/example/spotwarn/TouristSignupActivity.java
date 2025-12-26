package com.example.spotwarn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TouristSignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etPhone;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_signup);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Tourists");

        etName = findViewById(R.id.etTouristName);
        etEmail = findViewById(R.id.etTouristEmail);
        etPassword = findViewById(R.id.etTouristPassword);
        etPhone = findViewById(R.id.etTouristPhone);
        btnRegister = findViewById(R.id.btnTouristRegister);

        btnRegister.setOnClickListener(v -> registerTourist());
    }

    private void registerTourist() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        HashMap<String, String> touristData = new HashMap<>();
                        touristData.put("name", name);
                        touristData.put("email", email);
                        touristData.put("phone", phone);

                        database.child(uid).setValue(touristData)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(this, "Tourist Registered Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, TouristSignupActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Database Error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
