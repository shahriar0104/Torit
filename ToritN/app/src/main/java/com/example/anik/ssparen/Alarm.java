package com.example.anik.ssparen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Alarm extends AppCompatActivity implements View.OnClickListener{

    private String day1="20",day2="25",Uid,usage,day;
    private int usageInt;
    TextView textAlarm;
    TextView textMonth;
    Button inputReading;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        textAlarm=(TextView) findViewById(R.id.textAlarm);
        textMonth=(TextView) findViewById(R.id.textMonth);
        inputReading=(Button) findViewById(R.id.inputReading);
        inputReading.setOnClickListener(this);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Residents").child(Uid).child("usage");

        Date date=new Date();
        day = (String) android.text.format.DateFormat.format("dd",date);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    usage=dataSnapshot.getValue().toString();
                    usageInt=Integer.valueOf(usage);

                    if(day1.equals(day) || day2.equals(day)){
                        textMonth.setVisibility(View.VISIBLE);
                        if(usageInt <= 100)
                            textMonth.setText(" Great! You are in GREEN zone...\n Keep it up for the rest of the days of the month");
                        else if(usageInt > 100)
                            textMonth.setText("You are already in RED zone... Please decrease your usage");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == inputReading){
            finish();
            startActivity(new Intent(this, PowerCalculate.class));
        }
    }
}
