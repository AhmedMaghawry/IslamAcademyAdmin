package com.ezzat.islamacademyadmin;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class IslamAcademyAdmin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
