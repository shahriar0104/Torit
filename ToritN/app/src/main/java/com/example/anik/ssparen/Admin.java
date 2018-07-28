package com.example.anik.ssparen;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Admin extends AppCompatActivity {

    private RecyclerView mUsersList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Residents");
        mUsersList=(RecyclerView) findViewById(R.id.admin_panel);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        final String house_no = getIntent().getStringExtra("house_no");

        FirebaseRecyclerAdapter<residents, UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<residents, UsersViewHolder>(
                residents.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, residents res, int position) {

                if (house_no.equals(res.getHouse())) {
                    usersViewHolder.setName(res.getName());
                    usersViewHolder.setFlat(res.getFlat());
                    int use = res.getUsage();
                    final String usage = String.valueOf(use);
                    usersViewHolder.setUsage(usage);
                    final String user_id = getRef(position).getKey();
                    final String userName = res.getName();
                    //final int usage=res.getUsage();

                    usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent profileIntent = new Intent(Admin.this, AdminNotification.class);
                            profileIntent.putExtra("user_id", user_id);
                            profileIntent.putExtra("userName", userName);
                            profileIntent.putExtra("usage", usage);
                            startActivity(profileIntent);
                        }
                    });
                }
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_AllUsers:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, Home.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name){
            TextView residentOwner = (TextView) mView.findViewById(R.id.ownerName);
            residentOwner.setText(name);
        }

        public void setFlat(String flat){
            TextView residentFlat = (TextView) mView.findViewById(R.id.flatNo);
            residentFlat.setText(flat);
        }

        public void setUsage(String usage){
            TextView usePercentage = (TextView) mView.findViewById(R.id.usagePercentage);
            usePercentage.setText(usage+"%");
        }
    }
}





