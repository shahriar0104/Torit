package com.example.anik.ssparen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;

import java.util.Calendar;
import java.util.Date;

public class Input extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="Input";
    private ProgressDialog Dialog;
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail,redirectHere;
    private Button inputPower,cmpneighbour,graphpre;
    private DatabaseReference databaseReference;
    private ProgressBar progressIng;
    private static int SPLASH_TIME_OUT=3000;
    ValueEventListener myevent,myevent2;
    DatabaseReference connectedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar calendar = Calendar.getInstance();
        if(calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 5);
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //textViewUserEmail.setText("Welcome "+demo);
        //textViewUserEmail.setText("Welcome "+user.getEmail());
        redirectHere=(TextView) findViewById(R.id.redirectHere);
        progressIng=(ProgressBar) findViewById(R.id.progressIng);
        inputPower=(Button) findViewById(R.id.inputPower);
        inputPower.setOnClickListener(this);
        cmpneighbour=(Button) findViewById(R.id.cmpneighbour);
        cmpneighbour.setOnClickListener(this);
        graphpre=(Button) findViewById(R.id.graphpre);
        graphpre.setOnClickListener(this);
        textViewUserEmail=(TextView) findViewById(R.id.textViewUserEmail);

        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        myevent = connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toast.makeText(Input.this, "Succesfully connected to Firebase Servers.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Input.this, "Failed to connect to Firebase Servers, Running in offline mode. Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });


        databaseReference=FirebaseDatabase.getInstance().getReference("Residents");

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getInstance().getCurrentUser();
        /*if(user!=null){
            String uid=user.getUid();
            textViewUserEmail.setText("Welcome! "+uid);
            //databaseReference.child(uid).child("Residents").child("name").setValue("ollee");
        }*/
        final String uid=user.getUid();

       databaseReference=FirebaseDatabase.getInstance().getReference("Residents").child(uid).child("name");
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, Home.class));
        }
        myevent2 = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name=dataSnapshot.getValue().toString();
                    textViewUserEmail.setText("Welcome! "+name);
                    cmpneighbour.setVisibility(View.VISIBLE);
                    graphpre.setVisibility(View.VISIBLE);
                    inputPower.setVisibility(View.VISIBLE);
                }
                else{
                    textViewUserEmail.setText("Please Update your Information.");
                    cmpneighbour.setVisibility(View.INVISIBLE);
                    graphpre.setVisibility(View.INVISIBLE);
                    inputPower.setVisibility(View.INVISIBLE);
                    redirectHere.setVisibility(View.VISIBLE);
                    progressIng.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            startActivity(new Intent(Input.this, ProfileUpdate.class));
                        }
                    },SPLASH_TIME_OUT);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(Input.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                textViewUserEmail.setText("Select Your Service ");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_update:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //finish();
                startActivity(new Intent(this, ProinfoUpdate.class));

                return true;

            case R.id.logout:
                firebaseAuth.signOut();
                Intent intentstop = new Intent(this, AlarmReceiver.class);
                PendingIntent senderstop = PendingIntent.getBroadcast(this,
                        0, intentstop, 0);
                AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManagerstop.cancel(senderstop);
                finish();
                startActivity(new Intent(this, Home.class));
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        connectedRef.removeEventListener(myevent);
        databaseReference.removeEventListener(myevent2);

        super.onStop();
    }

    @Override
    protected void onPause() {
        connectedRef.removeEventListener(myevent);
        databaseReference.removeEventListener(myevent2);

        super.onPause();
    }

    @Override
    public void onClick(View view) {
       if(view == inputPower){
           //firebaseAuth.signOut();
           //finish();
           startActivity(new Intent(this, PowerCalculate.class));
       }
        if(view == cmpneighbour){
            //firebaseAuth.signOut();
            //finish();
            startActivity(new Intent(this, NeighbourGraph.class));
        }
        if(view == graphpre){
            //finish();
            startActivity(new Intent(this, GraphShow.class));
        }
    }


}
