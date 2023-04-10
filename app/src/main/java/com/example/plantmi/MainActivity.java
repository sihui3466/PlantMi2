package com.example.plantmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    }

        public void popupTime(View view){
        TimePickerDialog.OnTimeSetListener setTime=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourSelect, int minuteSelect) {
                hour=hourSelect;
                minute=minuteSelect;
                time=String.format(Locale.getDefault(),"%02d:%02d",hour,minute);
                timeBtn.setText("Watering at "+time);
            }
        };
        Date currentTime = Calendar. getInstance(). getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedTime = dateFormat.format(currentTime);

//        if (formattedTime==time){
//            Toast.makeText(MainActivity.this,"Watering start", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        else{
//            Toast.makeText(MainActivity.this, "Login Failed. Please try again ^.^", Toast.LENGTH_SHORT);
//        }
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, setTime,hour,minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

}
