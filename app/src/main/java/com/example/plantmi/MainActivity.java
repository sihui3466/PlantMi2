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
        waterButton=findViewById(R.id.waterButton);
        user=auth.getCurrentUser();
        
        waterRef = FirebaseDatabase.getInstance().getReference().child("water_plant").child("value");
        waterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                waterRef.setValue(true);
            }
            waterRef.setValue(false);
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
