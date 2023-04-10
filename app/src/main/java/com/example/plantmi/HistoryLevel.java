package com.example.plantmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// history activity of water tank level
public class HistoryLevel extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton exitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historylevel);

        recyclerView = findViewById(R.id.historyLevelRecyclerView);
        exitBtn = findViewById(R.id.exitbtnLevel);

        RecyclerView.Adapter<HistoryAdapter.HistoryHolder> adapter = new HistoryAdapter(this, LocalDataAdd.historyDataLevel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // to exit history of water tank level back to PlantStatus
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoryLevel.this, PlantStatus.class);
                startActivity(i);
            }
        });
    }
}

