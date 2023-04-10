package com.example.plantmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button timeBtn;
    ImageButton waterButton, backBtn;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference rootDatabaseReference;
    SensorSoil sensorSoil;
    DatabaseReference waterRef;
    private int hour,minute;
    private String time;
    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        timeBtn = findViewById(R.id.setTimeButton);
        waterButton = findViewById(R.id.waterButton);
        progressBar = findViewById(R.id.progressBar);
        backBtn = findViewById(R.id.backBtn);
        user = auth.getCurrentUser();

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlantProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

        waterRef = FirebaseDatabase.getInstance().getReference().child("water_plant").child("value");
        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterRef.setValue(true);
            }

        });
        rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        rootDatabaseReference.child("sensor_soil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorSoil = snapshot.getValue(SensorSoil.class);
                Log.d("Firebase", sensorSoil.getValue().toString());
                double d = Double.parseDouble(sensorSoil.getValue().toString());
                double value = Math.round((100 - ((d / 4095) * 100)));
                progressBar.setProgress((int) value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Timer.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
