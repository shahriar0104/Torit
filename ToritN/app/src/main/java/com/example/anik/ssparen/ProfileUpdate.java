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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileUpdate extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private EditText nameEdit,ageEdit,flatEdit,areaEdit,roomEdit,familyEdit,houseEdit;
    private Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Residents");

        FirebaseUser user=firebaseAuth.getCurrentUser();

        nameEdit=(EditText) findViewById(R.id.nameEdit);
        ageEdit=(EditText) findViewById(R.id.ageEdit);
        flatEdit=(EditText) findViewById(R.id.flatEdit);
        areaEdit=(EditText) findViewById(R.id.areaEdit);
        roomEdit=(EditText) findViewById(R.id.roomEdit);
        familyEdit=(EditText) findViewById(R.id.familyEdit);
        houseEdit=(EditText) findViewById(R.id.houseEdit);
        confirmButton=(Button) findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(this);


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

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter your Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(age)){
            Toast.makeText(this, "Please Enter your Age",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(flat)){
            Toast.makeText(this, "Please Enter your Flat No",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(area)){
            Toast.makeText(this, "Please Enter your Area",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(size)){
            Toast.makeText(this, "Please Enter your Flat Size(in sq/ft)",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(family)){
            Toast.makeText(this, "Enter Number of Member Live in that flat",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(house)){
            Toast.makeText(this, "Please Enter your House No",Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user=firebaseAuth.getCurrentUser();
        //String id=databaseReference.push().getKey();
        String id=user.getUid();

        userInformation userinfo=new userInformation(id,name,age,flat,area,size,family,"",house);
        //userInformation userinfo=new userInformation();

        databaseReference.child(id).setValue(userinfo);
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
