package com.example.spotwarn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignupActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvLogin;

    FirebaseAuth auth;
    DatabaseReference database;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);  // ✅ Correct layout

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Admin");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(AdminSignupActivity.this, AdminLoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter Full Name");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid Email");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter Phone Number");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Create User with FirebaseAuth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = auth.getCurrentUser().getUid();
                    User user = new User(name, email, phone);

                    database.child(userId).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AdminSignupActivity.this, "Registration Successful! Please login.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminSignupActivity.this, AdminLoginActivity.class));  // ✅ Correct redirection
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(AdminSignupActivity.this, "Database Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(AdminSignupActivity.this, "Signup Failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    // Model class
    public static class User {
        public String name, email, phone;

        public User() {} // Empty constructor required

        public User(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
    }
}
