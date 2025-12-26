package com.example.spotwarn;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUnsafeSpotActivity extends AppCompatActivity {

    EditText etName, etReason, etLat, etLng;
    Spinner spSeverity;
    Button submitButton;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unsafe_spot);

        etName = findViewById(R.id.etLocation);
        etReason = findViewById(R.id.etReason);
        etLat = findViewById(R.id.etLatitude);
        etLng = findViewById(R.id.etLongitude);
        spSeverity = findViewById(R.id.spSeverity);
        submitButton = findViewById(R.id.btnSubmit);

        dbRef = FirebaseDatabase.getInstance().getReference("UnsafeSpots");

        submitButton.setOnClickListener(view -> saveSpot());
    }

    private void saveSpot() {
        String name = etName.getText().toString().trim();
        String reason = etReason.getText().toString().trim();
        String latStr = etLat.getText().toString().trim();
        String lngStr = etLng.getText().toString().trim();
        String severity = spSeverity.getSelectedItem().toString();

        if (name.isEmpty() || reason.isEmpty() || latStr.isEmpty() || lngStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);

        UnsafeSpot spot = new UnsafeSpot(name, reason, lat, lng, severity);

        dbRef.push().setValue(spot).addOnCompleteListener(task -> {
            Toast.makeText(this, "Spot Added ✅", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
