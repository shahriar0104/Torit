package com.example.anik.ssparen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PowerCalculate extends AppCompatActivity implements View.OnClickListener{

    private static int SPLASH_TIME_OUT=3000;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,databaseReference1,checkDatabase;
    private EditText powerEdit;
    private Button calculateButton;
    private ArrayList<Integer> myArrayDate;
    private ArrayList<Integer> myArrayValue;
    private int valueInt=0,myValueInt=0,dateInt=0,children=0;
    private String pushId,id,sizeShare;
    private ProgressBar pbar;
    ValueEventListener myevent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_calculate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),Input.class));
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        id=user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Residents");
        databaseReference1=FirebaseDatabase.getInstance().getReference("Residents").child(id).child("consume");


        powerEdit=(EditText) findViewById(R.id.powerEdit);
        calculateButton=(Button) findViewById(R.id.calculateButton);
        pbar = (ProgressBar) findViewById(R.id.indeterminateBar);

        databaseReference.child(id).child("size").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sizeShare=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       myevent = databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myArrayDate=new ArrayList<Integer>();
                myArrayValue=new ArrayList<Integer>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        retriveTest post = postSnapshot.getValue(retriveTest.class);
                        children=(int) dataSnapshot.getChildrenCount();
                        if(children>0){
                            Integer mark=Integer.valueOf(post.getValues());
                            Integer diya=Integer.valueOf(post.getDay());

                            myArrayDate.add(diya);
                            myArrayValue.add(mark);
                            pushId=postSnapshot.getKey();
                        }
                    }
                //progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PowerCalculate.this, "Ooops! Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });

        calculateButton.setOnClickListener(this);
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

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

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void calculate(){
            String riser=powerEdit.getText().toString();
            Date date=new Date();
            String day = (String) DateFormat.format("dd",date);
            FirebaseUser user=firebaseAuth.getCurrentUser();
            //String id=databaseReference.push().getKey();
            String id=user.getUid();

            final Map<String, Object> proUpdate = new HashMap<String, Object>();
            final Map<String, String> post1 = new HashMap<String, String>();
            final Intent powerIntent=new Intent(PowerCalculate.this,ShowGraph.class);
            //progressBar.setVisibility(View.VISIBLE);

            //post1.put(day, riser);
            //databaseReference.child(id).child("consume").push().setValue(post1);

            if(myArrayDate.size()!=0){
                valueInt=Integer.valueOf(riser);
                myValueInt=myArrayValue.get(myArrayValue.size()-1);
                dateInt=Integer.valueOf(day);
            }

            if(children>0 && valueInt<=myValueInt){
                Toast.makeText(this,"Meter Reading is Incorrect. Please Check Again",Toast.LENGTH_SHORT).show();
                calculateButton.setVisibility(View.VISIBLE);
                pbar.setVisibility(View.INVISIBLE);
                return;
                //progressBar.setVisibility(View.INVISIBLE);
            }
            else if(children>0 && dateInt==myArrayDate.get(myArrayDate.size()-1)){

                post1.put("day", day);
                post1.put("values", riser);
               databaseReference.child(id).child("consume").child(pushId).setValue(post1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PowerCalculate.this, "Net ok", Toast.LENGTH_SHORT).show();
                        calculateButton.setVisibility(View.VISIBLE);
                        pbar.setVisibility(View.INVISIBLE);
                        powerIntent.putExtra("size",sizeShare);
                        finish();
                        startActivity(powerIntent);

                    }
                });

            }

            else{
                post1.put("day", day);
                post1.put("values", riser);
                databaseReference1.push().setValue(post1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        calculateButton.setVisibility(View.VISIBLE);
                        pbar.setVisibility(View.INVISIBLE);
                        powerIntent.putExtra("size",sizeShare);
                        finish();
                        startActivity(powerIntent);
                    }
                });

            }


        }

    @Override
    protected void onStop() {
        databaseReference1.removeEventListener(myevent);

        super.onStop();
    }

    @Override
    protected void onPause() {
        databaseReference1.removeEventListener(myevent);

        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if(view == calculateButton){
            boolean connect=haveNetworkConnection();
            if(connect==true) {
                String value = powerEdit.getText().toString();

                if(TextUtils.isEmpty(value)){
                    Toast.makeText(this, "Please enter a value.", Toast.LENGTH_SHORT).show();
                    return;
                }
                pbar.setVisibility(View.VISIBLE);
                calculateButton.setVisibility(View.INVISIBLE);
                calculate();

            }
            else
                Toast.makeText(PowerCalculate.this, "Internet Connection Unavailable",
                        Toast.LENGTH_SHORT).show();
            //finish();
            //startActivity(new Intent(this, ShowGraph.class));
        }
    }
}
