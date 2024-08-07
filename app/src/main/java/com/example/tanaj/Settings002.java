package com.example.tanaj;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings002 extends AppCompatActivity {

    private TextView [] txtSampleEs = new TextView[5];
    private TextView [] txtSampleHe = new TextView[5];

    // Shared Preferencies
    private SharedPreferences sharedPreferences;
    // Preferences
    private static final String MYPREFERENCES = "nightModePrefs";

    // Get fonts and sizes
    private int stet;
    private int steh;
    private int fteh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings002);

        // Load Shared Preferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Read Transliteration size
        // Set fonts option
        getTextSize();

        // Set transliteration size

        // Get Textviews
        findTxtSamples();
    }

    private void setTransliterations(){
        for (TextView tx : txtSampleEs){
            tx.setTextSize(stet);
        }
    }

    private void getTextSize(){
        stet = sharedPreferences.getInt("stet", 12);
        steh = sharedPreferences.getInt("steh", 24);
        fteh = sharedPreferences.getInt("fteh", 2);
    }

    private void findTxtSamples() {
        // Hebrew text
        txtSampleHe[0] = findViewById(R.id.s002_txm000a);
        txtSampleHe[1] = findViewById(R.id.s002_txm001a);
        txtSampleHe[2] = findViewById(R.id.s002_txm002a);
        txtSampleHe[3] = findViewById(R.id.s002_txm003a);
        txtSampleHe[4] = findViewById(R.id.s002_txm004a);

        // Spanish text
        txtSampleEs[0] = findViewById(R.id.s002_txm000b);
        txtSampleEs[1] = findViewById(R.id.s002_txm001b);
        txtSampleEs[2] = findViewById(R.id.s002_txm002b);
        txtSampleEs[3] = findViewById(R.id.s002_txm003b);
        txtSampleEs[4] = findViewById(R.id.s002_txm004b);
    }


}