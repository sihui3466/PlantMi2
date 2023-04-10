package com.example.plantmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//history activity of light intensity
public class HistoryLight extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton exitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historylight);

        recyclerView = findViewById(R.id.historyLightRecyclerView);
        exitBtn = findViewById(R.id.exitbtnLight);

        RecyclerView.Adapter<HistoryAdapter.HistoryHolder> adapter = new HistoryAdapter(this, LocalDataAdd.historyDataLight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // to exit history of light intensity back to PlantStatus
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoryLight.this, PlantStatus.class);
                startActivity(i);
            }
        });

    }
}

