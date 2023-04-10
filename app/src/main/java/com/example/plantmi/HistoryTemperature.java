package com.example.plantmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryTemperature extends AppCompatActivity {
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ImageButton exitBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historytemperature);

        recyclerView = findViewById(R.id.historyTemperatureRecyclerView);
        exitBtn = findViewById(R.id.exitbtnTemperature);

        RecyclerView.Adapter<HistoryAdapter.HistoryHolder> adapter = new HistoryAdapter(this, PlantProfilePage.historyDataLight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoryTemperature.this, PlantStatus.class);
                startActivity(i);
            }
        });

    }
}
