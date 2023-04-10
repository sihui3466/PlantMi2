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

// class to show plant's realtime status
// users are also able to edit plant name and description here
public class PlantStatus extends AppCompatActivity {

    View slidingBar;
    DatabaseReference rootDatabaseReference, descRootDatabaseReference, nameRootDatabaseReference;
    private TextView moistureData, lightData, tempData, humidData, levelData;
    ImageButton editBtn;
    Button historyMoistureBtn, historyLightBtn, historyTemperatureBtn, historyHumidityBtn, historyWaterTankBtn;
    TextView nameOfPlant, descOfPlant;
    SensorLight sensorLight;
    SensorSoil sensorSoil;
    SensorAir sensorAir;
    SensorLevel sensorLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantstatus);

        slidingBar = findViewById(R.id.plantStatusSlidingBar);
        editBtn = findViewById(R.id.edit);
        historyMoistureBtn = findViewById(R.id.historyMoistureButton);
        historyLightBtn = findViewById(R.id.historyLightButton);
        historyTemperatureBtn = findViewById(R.id.historyTemperatureButton);
        historyHumidityBtn = findViewById(R.id.historyHumidityButton);
        historyWaterTankBtn = findViewById(R.id.historyWaterTankButton);

        nameOfPlant = findViewById(R.id.name);
        descOfPlant = findViewById(R.id.desc);

        rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        moistureData = findViewById(R.id.moistureLevelValue);
        lightData = findViewById(R.id.lightIntensityValue);
        tempData = findViewById(R.id.temperatureValue);
        humidData = findViewById(R.id.humidityValue);
        levelData = findViewById(R.id.waterTankValue);

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

        // click button to go from PlantStatus to History activity of plant moisture level
        historyMoistureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryMoisture.class);
                startActivity(i);
                finish();
            }
        });
        // click button to go from PlantStatus to History activity of light intensity
        historyLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryLight.class);
                startActivity(i);
                finish();
            }
        });

        // click button to go from PlantStatus to History activity of temperature
        historyTemperatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryTemp.class);
                startActivity(i);
                finish();
            }
        });

        // click button to go from PlantStatus to History activity of humidity level
        historyHumidityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryHumid.class);
                startActivity(i);
                finish();
            }
        });

        // click button to go from PlantStatus to History activity of water tank level
        historyWaterTankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantStatus.this, HistoryLevel.class);
                startActivity(i);
                finish();
            }
        });

        // to display real time data of plant status
        rootDatabaseReference.child("sensor_soil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorSoil = snapshot.getValue(SensorSoil.class);
                Log.d("FirebaseReal",sensorSoil.getValue().toString());
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

        // to display real time data of plant status
        rootDatabaseReference.child("sensor_light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorLight = snapshot.getValue(SensorLight.class);
                Log.d("FirebaseReal",sensorLight.getValue().toString());
                lightData.setText(sensorLight.getValue().toString() + " lux");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(PlantStatus.this, "Failed to get Light Intensity data.", Toast.LENGTH_SHORT).show();
            }
        });

        // to display real time data of plant status
        rootDatabaseReference.child("sensor_air").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorAir = snapshot.getValue(SensorAir.class);
                Log.d("FirebaseReal",sensorAir.getTemperature().toString());
                tempData.setText(sensorAir.getTemperature().toString() + " C");
                Log.d("FirebaseReal",sensorAir.getTemperature().toString());
                humidData.setText(sensorAir.getHumidity().toString() + " %");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(PlantStatus.this, "Failed to get Temperature data.", Toast.LENGTH_SHORT).show();
                Toast.makeText(PlantStatus.this, "Failed to get Humidity data.", Toast.LENGTH_SHORT).show();
            }
        });

        // to display real time data of plant status
        rootDatabaseReference.child("sensor_level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorLevel = snapshot.getValue(SensorLevel.class);
                Log.d("FirebaseReal",sensorLevel.getValue().toString());
                double d = Double.parseDouble(sensorLevel.getValue().toString());
                double value = Math.round((d / 4095) * 100);
                levelData.setText(value + " %");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(PlantStatus.this, "Failed to get Water Tank Level data.", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUID = user.getUid();
        nameRootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("plants").child(userUID).child("plantname");
        descRootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("plants").child(userUID).child("plantdesc");
        // to display name of plant stored in firebase
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
                Toast.makeText(PlantStatus.this, "Error in setting Name of plant", Toast.LENGTH_SHORT).show();
            }
        });

        // to display description of plant stored in firebase
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
                Toast.makeText(PlantStatus.this, "Error in setting Description of plant", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
