package com.example.anik.ssparen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProinfoUpdate extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,checkDatabase,userIdDatabase;
    private DatabaseReference databaseReference1;
    private EditText nameEdit,ageEdit,flatEdit,areaEdit,roomEdit,familyEdit,houseEdit;
    private Button confirmButton;
    private String Uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proinfo_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameEdit=(EditText) findViewById(R.id.nameEdit);
        ageEdit=(EditText) findViewById(R.id.ageEdit);
        flatEdit=(EditText) findViewById(R.id.flatEdit);
        areaEdit=(EditText) findViewById(R.id.areaEdit);
        roomEdit=(EditText) findViewById(R.id.roomEdit);
        familyEdit=(EditText) findViewById(R.id.familyEdit);
        houseEdit=(EditText) findViewById(R.id.houseEdit);
        confirmButton=(Button) findViewById(R.id.confirmButton);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Residents");
        FirebaseUser user=firebaseAuth.getCurrentUser();
        Uid=user.getUid();
        userIdDatabase=FirebaseDatabase.getInstance().getReference().child("Residents").child(Uid).child("userId");
        userIdDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==false){
                    userIdDatabase.setValue(Uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        checkDatabase=FirebaseDatabase.getInstance().getReference().child("Residents").child(Uid).child("consume");
        checkDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    confirmButton.setOnClickListener(ProinfoUpdate.this);
                }
                else{
                    checkDatabase.setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            confirmButton.setOnClickListener(ProinfoUpdate.this);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //confirmButton.setOnClickListener(this);
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
                finish();
                startActivity(new Intent(this, Home.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveUserInformation(){
        String name=nameEdit.getText().toString().toLowerCase().trim();
        String age=ageEdit.getText().toString().toLowerCase();
        String flat=flatEdit.getText().toString().toLowerCase();
        String area=areaEdit.getText().toString().toLowerCase().trim();
        String size=roomEdit.getText().toString().toLowerCase();
        String family=familyEdit.getText().toString().toLowerCase();
        String house=houseEdit.getText().toString().toLowerCase().trim();

        FirebaseUser user=firebaseAuth.getCurrentUser();
        //String id=databaseReference.push().getKey();
        String id=user.getUid();

        //userInformation userinfo=new userInformation(id,name,age,flat,area,size,family,"");
        //userInformation userinfo=new userInformation();

        databaseReference=databaseReference.child(id);
        Map<String, Object> proUpdate = new HashMap<String, Object>();
        if(!TextUtils.isEmpty(name)){
            proUpdate.put("name",name);
        }
        if(!TextUtils.isEmpty(age)){
            proUpdate.put("age",age);
        }
        if(!TextUtils.isEmpty(house)){
            proUpdate.put("house",house);
        }
        if(!TextUtils.isEmpty(flat)){
            proUpdate.put("flat",flat);
        }
        if(!TextUtils.isEmpty(area)){
            proUpdate.put("area",area);
        }
        if(!TextUtils.isEmpty(size)){
            proUpdate.put("size",size);
        }
        if(!TextUtils.isEmpty(family)){
            proUpdate.put("family",family);
        }
        databaseReference.updateChildren(proUpdate);
        //Toast.makeText(this, "Information saved...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Input.class));
    }


    /*public void calculate(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        //String id=databaseReference.push().getKey();
        String id=user.getUid();

        consumption con=new consumption("","");
        //userInformation userinfo=new userInformation();

        databaseReference.child(id).child("consume").setValue(con);
    }*/



    @Override
    public void onClick(View view) {
        if(view == confirmButton){
            saveUserInformation();
            //calculate();
        }
    }
}
