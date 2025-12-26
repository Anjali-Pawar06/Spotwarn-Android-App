package com.example.spotwarn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PoliceInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PoliceAdapter policeAdapter;
    private List<PoliceModel> policeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_info);

        recyclerView = findViewById(R.id.recyclerViewPolice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        policeList = new ArrayList<>();
        policeAdapter = new PoliceAdapter(policeList);
        recyclerView.setAdapter(policeAdapter);

        loadPoliceData();
    }

    private void loadPoliceData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Police");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                policeList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    PoliceModel model = child.getValue(PoliceModel.class);
                    if (model != null) {
                        policeList.add(model);
                    }
                }
                policeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PoliceInfoActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}