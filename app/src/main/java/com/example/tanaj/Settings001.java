package com.example.tanaj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings001 extends AppCompatActivity {

    // Define widgets
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw00;

    // Preferences
    public static final String MYPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings001);

        // Change title
        setTitle("Configuración");

        // Initialize preferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Get widgets
        sw00 = findViewById(R.id.s01_sw00);

        // Actions for switch Theme
        sw00.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sw00.setText(R.string.settings000_str003);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                }else{
                    sw00.setText(R.string.settings000_str002);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                }
            }
        });

    }

    private void checkNightModeActivate() {
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)){
            sw00.setChecked(true);
            sw00.setText(R.string.settings000_str003);
        }else{
            sw00.setChecked(false);
            sw00.setText(R.string.settings000_str002);
        }
    }


    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }
}