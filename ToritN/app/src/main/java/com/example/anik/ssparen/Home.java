package com.example.anik.ssparen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="EmailPassword";

    private Button signUp;
    private  Button login;
    private EditText editTextEmail,editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference tokenDatabase;
    private String email,emailCheck;
    private FirebaseUser user,userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            finish();
            email=user.getEmail();
            emailCheck="sparen@gmail.com";
            if(email.equals(emailCheck))
                startActivity(new Intent(getApplicationContext(),AdminHouse.class));
            else
                startActivity(new Intent(getApplicationContext(),Input.class));
        }


        tokenDatabase= FirebaseDatabase.getInstance().getReference().child("Residents");
        progressBar=(ProgressBar) findViewById(R.id.indeterminateBar);
        login=(Button) findViewById(R.id.button);
        editTextEmail=(EditText) findViewById(R.id.etEm);
        editTextPassword=(EditText) findViewById(R.id.etPass);
        editTextPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        signUp=(Button) findViewById(R.id.button2);

        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void userLogin(){
        final String email=editTextEmail.getText().toString().trim();
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

                            String curr_user=firebaseAuth.getCurrentUser().getUid();
                            userLogin=firebaseAuth.getInstance().getCurrentUser();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            String emailLogin=userLogin.getEmail();
                            String emailCheckLogin="sparen@gmail.com";
                            if(emailLogin.equals(emailCheckLogin)){
                                finish();
                                login.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(getApplicationContext(),AdminHouse.class));
                            }
                            else{
                                tokenDatabase.child(curr_user).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),Input.class));
                                    }
                                });
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Home.this, "User authentication Failed. Check your email,password and internet connection.",
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
        if(view == signUp){
            startActivity(new Intent(this, Registration.class));
        }
    }


    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };
}
