package com.example.tanaj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    // Preferences
    public static final String MYPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    public static final String KEY_FIRSTTIME = "firstTime";
    public static int DELAY = 1000;
    SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize preferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Check if night mode activated
        checkNightModeActivate();

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // Set title of activity
        setTitle("Tanaj");

        // Check if the user if configuring the app
        intent = getIntent();

        // Launch Activity Read
        //doWork();
        Handler handler = new Handler();
        handler.postDelayed(launchActivity, DELAY);
    }

    private void startReadActivity() {
        ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(MainActivity.this);
        // Set progressdialog title
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        // Show progressdialog
        mProgressDialog.show();

        intent = new Intent(MainActivity.this, ActivityRead.class);
        startActivity(intent);
    }

    private final Runnable launchActivity = new Runnable() {
        @Override
        public void run() {
            startReadActivity();
        }
    };

    private void checkNightModeActivate() {
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}