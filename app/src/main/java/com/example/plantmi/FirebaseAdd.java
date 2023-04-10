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
import java.util.HashMap;

public class FirebaseAdd {
    DatabaseReference rootDatabaseReference, userHistoryMoisture, userHistoryLight, userHistoryTemp;

    SensorSoil sensorSoil;

    FirebaseAdd(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUID = user.getUid();
        rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userHistoryMoisture = FirebaseDatabase.getInstance().getReference().child("history_moisture").child(userUID);
        userHistoryLight = FirebaseDatabase.getInstance().getReference().child("history_light").child(userUID);
        userHistoryTemp = FirebaseDatabase.getInstance().getReference().child("history_temp").child(userUID);



        rootDatabaseReference.child("sensor_soil").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Moisture Level data", task.getException());
                }
                else {
                    sensorSoil = task.getResult().getValue(SensorSoil.class);
                    Log.d("Firebase", sensorSoil.getValue().toString());
                    double d = Double.parseDouble(sensorSoil.getValue().toString());
                    double value = Math.round( (100 - ((d/4095)*100)) );
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String s = timestamp + ":  " + value + "%";
//                    HashMap newHistory = new HashMap<>();
//                    newHistory.put("new", s);
//                    userHistoryMoisture.updateChildren(newHistory);
                    userHistoryMoisture.push().setValue(s);
                }
            }
        });



    }


    }


