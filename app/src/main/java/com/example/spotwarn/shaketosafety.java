package com.example.spotwarn;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class shaketosafety extends AppCompatActivity {

    private ActivityResultLauncher<String[]> requestPermissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences p = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE);
        if (p.getString("phoneNumbers", "").isEmpty()) {
            startActivity(new Intent(this, FillInfo.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_shaketosafety);

        requestPermissionsLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> startServiceIfAllowed()
                );

        requestPermissionsLauncher.launch(new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE
        });
    }

    private void startServiceIfAllowed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            startForeground();
        } else {
            Toast.makeText(this, "SMS Permission required", Toast.LENGTH_LONG).show();
        }
    }

    private void startForeground() {
        Intent serviceIntent = new Intent(this, ShakeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        Toast.makeText(this, "Shake Safety ON ✅", Toast.LENGTH_SHORT).show();
    }
}
