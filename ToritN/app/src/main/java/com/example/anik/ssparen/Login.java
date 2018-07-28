package com.example.anik.ssparen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="EmailPassword";

    private  Button login;
    private EditText editTextEmail,editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference tokenDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),Input.class));
        }
        //databaseReference= FirebaseDatabase.getInstance().getReference("Residents");

        tokenDatabase= FirebaseDatabase.getInstance().getReference().child("Residents");
        progressBar=(ProgressBar) findViewById(R.id.indeterminateBar);
        login=(Button) findViewById(R.id.button);
        editTextEmail=(EditText) findViewById(R.id.etEm);
        editTextPassword=(EditText) findViewById(R.id.etPass);
        login.setOnClickListener(this);

    }

    private void userLogin(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }

        login.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final String curr_user=firebaseAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            //finish();
                            tokenDatabase.child(curr_user).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    String uid=tokenDatabase.child(curr_user).getKey();
                                    tokenDatabase.child(curr_user).child("userId").setValue(uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                            startActivity(new Intent(getApplicationContext(),ProfileUpdate.class));
                                        }
                                    });
                                }
                            });
                            //startActivity(new Intent(getApplicationContext(), ProfileUpdate.class));
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Password or Email is Incorrect",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            //updateUI(null);
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if(view == login){
            userLogin();
        }
    }
}
