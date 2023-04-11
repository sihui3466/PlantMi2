package com.example.plantmi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyBroadcastReceiver extends BroadcastReceiver {

    DatabaseReference waterRef;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Water Dispensing!", Toast.LENGTH_LONG).show();
        waterRef = FirebaseDatabase.getInstance().getReference().child("water_plant").child("value");
        waterRef.setValue(true);
    }
}
