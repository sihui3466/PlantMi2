package com.example.plantmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button timeBtn, waterButton;
    FirebaseAuth auth;
    FirebaseUser user;
    private ImageView imageView;

    DatabaseReference waterRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        timeBtn=findViewById(R.id.setTimeButton);
//        waterButton=findViewById(R.id.waterButton);
        user=auth.getCurrentUser();
        //TODO 1.6 for timeButton, invoke the setOnClickListener method
//        timeBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(MainActivity.this, PlantProfilePage.class);
//                startActivity(intent);
//            }
//        });
        
//        waterRef = FirebaseDatabase.getInstance().getReference().child("water_plant").child("value");
//        waterButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                waterRef.setValue(true);
//            }
//        });

    }

}
