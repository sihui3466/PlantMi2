package com.example.plantmi;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

public class FirebaseAdd {
    DatabaseReference rootDatabaseReference, userHistoryMoisture, userHistoryLight, userHistoryTemp, userHistoryHumid, userHistoryLevel;

    SensorSoil sensorSoil;
    SensorLight sensorLight;
    SensorAir sensorAir;
    SensorLevel sensorLevel;

    FirebaseAdd() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUID = user.getUid();
        rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userHistoryMoisture = FirebaseDatabase.getInstance().getReference().child("history_moisture").child(userUID);
        userHistoryLight = FirebaseDatabase.getInstance().getReference().child("history_light").child(userUID);
        userHistoryTemp = FirebaseDatabase.getInstance().getReference().child("history_temp").child(userUID);
        userHistoryHumid = FirebaseDatabase.getInstance().getReference().child("history_humid").child(userUID);
        userHistoryLevel = FirebaseDatabase.getInstance().getReference().child("history_level").child(userUID);

        rootDatabaseReference.child("sensor_soil").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("FirebaseAdd", "Error in getting Moisture Level data", task.getException());
                } else {
                    sensorSoil = task.getResult().getValue(SensorSoil.class);
                    Log.d("FirebaseAdd", sensorSoil.getValue().toString());
                    double d = Double.parseDouble(sensorSoil.getValue().toString());
                    double value = Math.round((100 - ((d / 4095) * 100)));
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String s = timestamp + ":  " + value + "%";
//                    HashMap newHistory = new HashMap<>();
//                    newHistory.put("new", s);
//                    userHistoryMoisture.updateChildren(newHistory);
                    userHistoryMoisture.push().setValue(s);
                }
            }
        });

        rootDatabaseReference.child("sensor_light").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("FirebaseAdd", "Error in getting Light Intensity data", task.getException());
                } else {
                    sensorLight = task.getResult().getValue(SensorLight.class);
                    Log.d("FirebaseAdd", sensorLight.getValue().toString());
                    String value = sensorLight.getValue().toString();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String s = timestamp + ":  " + value + "lux";
                    userHistoryLight.push().setValue(s);
                }
            }
        });

        rootDatabaseReference.child("sensor_air").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("FirebaseAdd", "Error in getting Temperature & Humidity data", task.getException());
                } else {
                    sensorAir = task.getResult().getValue(SensorAir.class);
                    Log.d("FirebaseAdd", sensorAir.getTemperature().toString());
                    String value1 = sensorAir.getTemperature().toString();
                    String value2 = sensorAir.getHumidity().toString();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String s1 = timestamp + ":  " + value1 + "C";
                    String s2 = timestamp + ":  " + value2 + "%";
                    userHistoryTemp.push().setValue(s1);
                    userHistoryHumid.push().setValue(s2);
                }
            }
        });

        rootDatabaseReference.child("sensor_level").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("FirebaseAdd", "Error in getting Water Level data", task.getException());
                } else {
                    sensorLevel = task.getResult().getValue(SensorLevel.class);
                    Log.d("FirebaseAdd", sensorLevel.getValue().toString());
                    double d = Double.parseDouble(sensorLevel.getValue().toString());
                    double value = Math.round((d / 4095) * 100);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String s = timestamp + ":  " + value + "%";
                    userHistoryLevel.push().setValue(s);
                }
            }
        });

    }
}


