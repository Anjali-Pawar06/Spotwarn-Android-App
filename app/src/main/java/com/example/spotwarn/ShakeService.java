package com.example.spotwarn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.*;

import java.util.*;

public class ShakeService extends Service implements SensorEventListener {

    private static final String CHANNEL_ID = "ShakeChannel";
    private static final float SHAKE_THRESHOLD = 6.2f; // Lower threshold for better detection
    private long lastShakeTime = 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<String> phoneNumbers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        showRunningNotification();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        phoneNumbers = loadNumbers();
        fetchFromFirebase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        Toast.makeText(this, "Shake service stopped!", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @SuppressLint("ForegroundServiceType")
    private void showRunningNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Shake to Safety Active")
                .setContentText("Shake detection is ON for your safety 💛")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(100, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, "Shake Detection Service",
                            NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        double acceleration = Math.sqrt(
                e.values[0] * e.values[0] +
                        e.values[1] * e.values[1] +
                        e.values[2] * e.values[2]
        ) - SensorManager.GRAVITY_EARTH;

        Log.d("SHAKE", "Acceleration: " + acceleration);

        if (acceleration > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();
            if (now - lastShakeTime > 3000) {  // 3s cooldown
                lastShakeTime = now;
                Toast.makeText(this, "Shake Detected! Sending SOS...", Toast.LENGTH_SHORT).show();
                sendEmergencySms();
            }
        }
    }

    private void sendEmergencySms() {
        if (phoneNumbers.isEmpty()) {
            Log.e("NUMBERS", "No numbers found!");
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS Permission Missing!", Toast.LENGTH_LONG).show();
            return;
        }

        String message = "🚨 SOS! I am in danger. Please help me immediately! - SpotWarn";

        SmsManager smsManager = SmsManager.getDefault();

        for (String num : phoneNumbers) {
            try {
                smsManager.sendTextMessage(num, null, message, null, null);
                Log.d("SOS", "SMS sent to: " + num);
            } catch (Exception e) {
                Log.e("SOS", "SMS failed: " + e.getMessage());
            }
        }

        Toast.makeText(this, "✅ Emergency SMS Sent!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor s, int a) {}

    private void fetchFromFirebase() {
        SharedPreferences prefs = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE);
        String user = prefs.getString("userCode", null);
        if (user == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Info").child(user);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                phoneNumbers.clear();
                for (int i = 1; i <= 5; i++) {
                    String n = snapshot.child("contact" + i).getValue(String.class);
                    if (n != null && !n.isEmpty()) phoneNumbers.add(n);
                }
                saveNumbers(phoneNumbers);
                Log.d("NUMBERS", "Loaded: " + phoneNumbers);
            }
            @Override public void onCancelled(DatabaseError error) {}
        });
    }

    private void saveNumbers(List<String> list) {
        getSharedPreferences("SpotWarnInfo", MODE_PRIVATE)
                .edit().putString("phoneNumbers", String.join(",", list)).apply();
    }

    private List<String> loadNumbers() {
        String saved = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE)
                .getString("phoneNumbers", "");
        return saved.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(saved.split(",")));
    }
}
