package com.example.spotwarn;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private EditText etDate, etEventName;
    private Button btnAddEvent;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etDate = findViewById(R.id.etDate);
        etEventName = findViewById(R.id.etEventName);
        btnAddEvent = findViewById(R.id.btnAddEvent);

        dbRef = FirebaseDatabase.getInstance().getReference("events");

        btnAddEvent.setOnClickListener(v -> addEvent());
    }

    private void addEvent() {
        String date = etDate.getText().toString().trim();
        String event = etEventName.getText().toString().trim();

        if (date.isEmpty() || event.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventId = dbRef.push().getKey();

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("date", date);
        eventData.put("eventName", event);

        assert eventId != null;
        dbRef.child(eventId)
                .setValue(eventData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "✅ Event Added Successfully!", Toast.LENGTH_SHORT).show();
                    etDate.setText("");
                    etEventName.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
