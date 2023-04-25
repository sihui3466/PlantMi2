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

// class to get history data from firebase to add to local arraylists (to be display in recyclerview in history activity pages)
public class LocalDataAdd {
    DatabaseReference userHistoryMoisture, userHistoryLight, userHistoryTemp, userHistoryHumid, userHistoryLevel;
    static HistoryDataSource historyDataSourceMoisture;
    static HistoryDataSource historyDataSourceLight;
    static HistoryDataSource historyDataSourceTemp;
    static HistoryDataSource historyDataSourceHumid;
    static HistoryDataSource historyDataSourceLevel;

    LocalDataAdd(){
        historyDataSourceMoisture = new HistoryDataSource();
        historyDataSourceLight = new HistoryDataSource();
        historyDataSourceTemp = new HistoryDataSource();
        historyDataSourceHumid = new HistoryDataSource();
        historyDataSourceLevel = new HistoryDataSource();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUID = user.getUid();

        userHistoryMoisture = FirebaseDatabase.getInstance().getReference().child("history_moisture").child(userUID);
        userHistoryLight = FirebaseDatabase.getInstance().getReference().child("history_light").child(userUID);
        userHistoryTemp = FirebaseDatabase.getInstance().getReference().child("history_temp").child(userUID);
        userHistoryHumid = FirebaseDatabase.getInstance().getReference().child("history_humid").child(userUID);
        userHistoryLevel = FirebaseDatabase.getInstance().getReference().child("history_level").child(userUID);

        userHistoryMoisture.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Moisture Level history", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        historyDataSourceMoisture.addHistory(s);
                        Log.d("addHistMoisture", s);
                    }
                }
            }
        });

        userHistoryLight.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Light Intensity history", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        historyDataSourceLight.addHistory(s);
                        Log.d("addHistLight", s);
                    }
                }
            }
        });

        userHistoryTemp.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Temperature history", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        historyDataSourceTemp.addHistory(s);
                        Log.d("addHistTemp", s);
                    }
                }
            }
        });

        userHistoryHumid.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Humidity history", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        historyDataSourceHumid.addHistory(s);
                        Log.d("addHistHumid", s);
                    }
                }
            }
        });

        userHistoryLevel.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Water Level history", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        historyDataSourceLevel.addHistory(s);
                        Log.d("addHistLevel", s);
                    }
                }
            }
        });
    }

}
