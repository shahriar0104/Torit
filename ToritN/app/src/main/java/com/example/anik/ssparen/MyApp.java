package com.example.anik.ssparen;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mahdi on 10/6/2017.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
