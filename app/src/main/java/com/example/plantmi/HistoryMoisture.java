package com.example.plantmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryMoisture extends AppCompatActivity {
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ImageButton exitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historymoisture);

        recyclerView = findViewById(R.id.historyMoistureRecyclerView);
        exitBtn = findViewById(R.id.exitbtnMoisture);


        RecyclerView.Adapter<HistoryAdapter.HistoryHolder> adapter = new HistoryAdapter(this, PlantProfilePage.historyDataMoisture);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoryMoisture.this, PlantStatus.class);
                startActivity(i);
            }
        });

    }
}
