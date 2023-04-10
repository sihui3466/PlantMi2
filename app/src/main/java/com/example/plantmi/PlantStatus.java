package com.example.plantmi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class PlantStatus extends AppCompatActivity {

    View slidingBar;
    DatabaseReference rootDatabaseReference;
    DatabaseReference descRootDatabaseReference;
    DatabaseReference nameRootDatabaseReference;
    private TextView moistureData;
    private TextView lightData;
    ImageButton editBtn;
    Button historyMoistureBtn, historyLightBtn, historyTemperatureBtn;
    TextView nameOfPlant, descOfPlant;
    SensorLight sensorLight;
    SensorSoil sensorSoil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantstatus);

        slidingBar = findViewById(R.id.plantStatusSlidingBar);
        editBtn = findViewById(R.id.edit);
        historyMoistureBtn = findViewById(R.id.historyMoistureButton);
        historyLightBtn = findViewById(R.id.historyLightButton);
        historyTemperatureBtn = findViewById(R.id.historyTemperatureButton);
        nameOfPlant = findViewById(R.id.name);
        descOfPlant = findViewById(R.id.desc);

        slidingBar.setOnTouchListener(new OnSwipeTouchListener(PlantStatus.this) {
            public void onSwipeBottom() {
                Intent i = new Intent(PlantStatus.this, PlantProfilePage.class);
                startActivity(i);
                overridePendingTransition( R.anim.slide_from_top, R.anim.slide_in_top );
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlantStatus.this, EditPlant.class);
                startActivity(i);
                finish();
            }
        });

        historyMoistureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryMoisture.class);
                startActivity(i);
                finish();
            }
        });
        historyLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryLight.class);
                startActivity(i);
                finish();
            }
        });

        historyTemperatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryTemp.class);
                startActivity(i);
                finish();
            }
        });

        rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //databaseReference = rootDatabaseReference.child("sensor_data");
        moistureData = findViewById(R.id.moistureLevelValue);
        lightData = findViewById(R.id.lightIntensityValue);

        rootDatabaseReference.child("sensor_soil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorSoil = snapshot.getValue(SensorSoil.class);
                Log.d("Firebase",sensorSoil.getValue().toString());
                double d = Double.parseDouble(sensorSoil.getValue().toString());
                double value = Math.round( (100 - ((d/4095)*100)) );
                moistureData.setText(Double.toString(value) + "%");
                if (value<=20){
                    Toast.makeText(PlantStatus.this, "Remember to water mi!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(PlantStatus.this, "Failed to get Moisture Level data.", Toast.LENGTH_SHORT).show();
            }
        });

        rootDatabaseReference.child("sensor_light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorLight = snapshot.getValue(SensorLight.class);
                Log.d("Firebase",sensorLight.getValue().toString());
                lightData.setText(sensorLight.getValue().toString() + "lux");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(PlantStatus.this, "Failed to get Light Intensity data.", Toast.LENGTH_SHORT).show();
            }
        });

        rootDatabaseReference.child("sensor_air").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorLight = snapshot.getValue(SensorLight.class);
                Log.d("Firebase",sensorLight.getValue().toString());
                lightData.setText(sensorLight.getValue().toString() + "lux");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(PlantStatus.this, "Failed to get Light Intensity data.", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userUID = user.getUid();
        nameRootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("plants").child(userUID).child("plantname");
        descRootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("plants").child(userUID).child("plantdesc");
        nameRootDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = snapshot.getValue().toString();
                    nameOfPlant.setText(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        descRootDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = snapshot.getValue().toString();
                    descOfPlant.setText(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
