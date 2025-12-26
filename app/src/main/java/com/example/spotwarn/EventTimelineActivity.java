package com.example.spotwarn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventTimelineActivity extends AppCompatActivity {

    private RecyclerView recyclerEvents;
    private DatabaseReference dbRef;
    private ArrayList<EventModel> eventList;
    private EventAdapter adapter;

    private ProgressBar progressBar;
    private TextView txtEmpty;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_timeline);

        recyclerEvents = findViewById(R.id.recyclerEvents);
        progressBar = findViewById(R.id.progressBar);
        txtEmpty = findViewById(R.id.txtEmpty);

        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));

        dbRef = FirebaseDatabase.getInstance().getReference("events");
        eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList);
        recyclerEvents.setAdapter(adapter);

        loadEvents();
    }

    private void loadEvents() {
        progressBar.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);

                eventList.clear();

                if (!snapshot.exists()) {
                    txtEmpty.setVisibility(View.VISIBLE);
                    txtEmpty.setText("No events found.");
                    adapter.notifyDataSetChanged();
                    return;
                }

                for (DataSnapshot ds : snapshot.getChildren()) {
                    EventModel event = ds.getValue(EventModel.class);
                    eventList.add(event);
                }

                adapter.notifyDataSetChanged();
                txtEmpty.setVisibility(eventList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EventTimelineActivity.this, "⚠️ Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
