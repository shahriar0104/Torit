package com.example.anik.ssparen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminNotification extends AppCompatActivity {

    private TextView mDisplayName,mDisplayUsage;
    private Button notifyButton;
    private DatabaseReference notificationdatabase;
    private FirebaseAuth firebaseAuth;
    private String curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        curr_user=user.getUid();

        final String user_id = getIntent().getStringExtra("user_id");
        final String userName = getIntent().getStringExtra("userName");
        final String usage = getIntent().getStringExtra("usage");
        mDisplayName=(TextView) findViewById(R.id.user_name);
        mDisplayUsage=(TextView) findViewById(R.id.usagePercent);
        mDisplayName.setText("Flat Owner: "+userName);
        mDisplayUsage.setText("Usage: "+usage+"%");

        notifyButton=(Button) findViewById(R.id.notifyButton);
        notifyButton.setText("Send Notification to "+userName);
        notificationdatabase= FirebaseDatabase.getInstance().getReference().child("notifications");

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> notificationData=new HashMap<String, String>();
                notificationData.put("from",curr_user);
                notificationData.put("type","request");
                notificationdatabase.child(user_id).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminNotification.this,"Notification Has Sent",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
