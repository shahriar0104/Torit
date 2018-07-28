package com.example.anik.ssparen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginHere extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;
    private static int SPLASH_TIME_OUT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_here);
        firebaseAuth=FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(LoginHere.this, Login.class));
            }
        },SPLASH_TIME_OUT);
    }

}
