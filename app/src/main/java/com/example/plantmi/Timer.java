package com.example.plantmi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;

public class Timer extends AppCompatActivity {

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    ImageButton exitBtn;
    Button timerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        exitBtn = findViewById(R.id.exitbtntimer);
        timerBtn = findViewById(R.id.toggleButton);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Timer.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        timerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Timer.this,MainActivity.class);
//                startActivity(i);
//            }
//        });

    }

    public void OnClicked(View view) {
        long time;
        Toast.makeText(Timer.this, "TIMER ON", Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();

        // calendar is called to get current time in hour and minute
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

        // using intent i have class AlarmReceiver class which inherits
        // BroadcastReceiver
        Intent intent = new Intent(this, MyBroadcastReceiver.class);

        // we call broadcast using pendingIntent
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() > time) {
            // setting time as AM and PM
            if (Calendar.AM_PM == 0)
                time = time + (1000 * 60 * 60 * 12);
            else
                time = time + (1000 * 60 * 60 * 24);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        Intent i = new Intent(Timer.this,MainActivity.class);
        startActivity(i);

    }
}
