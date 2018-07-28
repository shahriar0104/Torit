package com.example.anik.ssparen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationShow extends AppCompatActivity {

    private String Uid,usage;
    private int usageInt;
    private TextView notification;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_show);

        notification=(TextView) findViewById(R.id.notification);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Residents").child(Uid).child("usage");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    usage=dataSnapshot.getValue().toString();
                    usageInt=Integer.valueOf(usage);

                    if(usageInt <= 100)
                        notification.setText("Congrates!!! You have used your electricity sufficiently.\nIf u continue to do so like previous month\nwe will reward you");
                    else if(usageInt > 100)
                        notification.setText("Please reduce ur usage.\nOtherwise we will Cut ur Line.");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //notification.setText("Please reduce ur usage.\nOtherwise we will Cut ur Line.");
    }
}
