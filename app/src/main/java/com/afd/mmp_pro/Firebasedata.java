package com.afd.mmp_pro;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Firebasedata extends Application {
        @Override
        public void onCreate() {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            super.onCreate();
        }
    }

