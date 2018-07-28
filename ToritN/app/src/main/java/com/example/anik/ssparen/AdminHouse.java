package com.example.anik.ssparen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHouse extends AppCompatActivity {

    private RecyclerView mHouseList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_house);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOfHouse);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Residents");
        mHouseList=(RecyclerView) findViewById(R.id.admin_panel_house);
        mHouseList.setHasFixedSize(true);
        mHouseList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<residents, AdminHouse.HouseViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<residents, AdminHouse.HouseViewHolder>(
                residents.class,
                R.layout.house_list,
                AdminHouse.HouseViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(AdminHouse.HouseViewHolder houseViewHolder, residents res, int position) {

                houseViewHolder.setHouse(res.getHouse());
                final String houseNo=res.getHouse();

                houseViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent=new Intent(AdminHouse.this, Admin.class);
                        profileIntent.putExtra("house_no",houseNo);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        mHouseList.setAdapter(firebaseRecyclerAdapter);
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

    public static class HouseViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public HouseViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setHouse(String house){
            TextView houseNo = (TextView) mView.findViewById(R.id.houseNo);
            houseNo.setText(house);
        }
    }
}
