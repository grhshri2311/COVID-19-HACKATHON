package com.gprs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class alarmremember extends AppCompatActivity {

    TextView textView,textView1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmremember);
        Intent intent=getIntent();

    textView=findViewById(R.id.textView15);
    textView1=findViewById(R.id.textView16);
    button=findViewById(R.id.button3);

    textView.setText(intent.getStringExtra("name"));
        textView1.setText(intent.getStringExtra("desc"));



    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            finish();
        }
    });

    }


}
