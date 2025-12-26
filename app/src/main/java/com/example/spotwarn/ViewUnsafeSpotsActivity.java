package com.example.spotwarn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewUnsafeSpotsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_unsafe_spots);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbRef = FirebaseDatabase.getInstance().getReference("UnsafeSpots");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        loadSpots();
    }

    private void loadSpots() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMap.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UnsafeSpot spot = ds.getValue(UnsafeSpot.class);

                    if (spot == null) continue;

                    LatLng location = new LatLng(spot.latitude, spot.longitude);

                    float markerColor;
                    int circleColor;
                    int radius;

                    switch (spot.severity) {
                        case "High":
                            markerColor = BitmapDescriptorFactory.HUE_RED;
                            circleColor = 0x55FF0000;
                            radius = 150;
                            break;
                        case "Medium":
                            markerColor = BitmapDescriptorFactory.HUE_YELLOW;
                            circleColor = 0x55FFFF00;
                            radius = 120;
                            break;
                        default:
                            markerColor = BitmapDescriptorFactory.HUE_GREEN;
                            circleColor = 0x5500FF00;
                            radius = 80;
                    }

                    mMap.addCircle(new CircleOptions()
                            .center(location)
                            .radius(radius)
                            .fillColor(circleColor)
                            .strokeWidth(2));

                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(spot.locationName)
                            .snippet(spot.reason)
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
