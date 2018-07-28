package com.example.anik.ssparen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Calendar;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    private Button bt1;
    private EditText etEmail;
    private EditText etCp;
    private EditText etPass;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.indeterminateBar);
        bt1=(Button) findViewById(R.id.regButton);
        etEmail=(EditText) findViewById(R.id.regEmail);
        etPass=(EditText) findViewById(R.id.regPassword);
        etCp=(EditText) findViewById(R.id.regPassword1);

        bt1.setOnClickListener(this);
    }

    private void registerUser(){
        String email=etEmail.getText().toString().trim();
        String password=etPass.getText().toString().trim();
        String cpassword=etCp.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.equals(password,cpassword)){
            Toast.makeText(this, "Error! Passwords do not match .",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(this, "Password must be 6 or More Characters",Toast.LENGTH_SHORT).show();
            return;
        }

        bt1.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           // Toast.makeText(Registration.this, "Registration Succesfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent myIntent=new Intent(Registration.this, LoginHere.class);
                                    startActivity(myIntent);

                            //bt1.setVisibility(View.INVISIBLE);
                            //progressBar.setVisibility(View.VISIBLE);
                        }
                        //else
                          //  Toast.makeText(Registration.this, "Registration  Unsuccesful...", Toast.LENGTH_SHORT).show();
                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Registration.this, "User with this email already exist!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(Registration.this, "Registration  Unsuccesful...", Toast.LENGTH_SHORT).show();
                            //progressDialog.hide();
                            bt1.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);

                            return;
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == bt1){
            registerUser();
        }
    }
}
