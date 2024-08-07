package com.example.tanaj;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAbout extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txt = findViewById(R.id.about_txt);

        SpannableString string = new SpannableString("Text with strikethrough span");
        string.setSpan(new StrikethroughSpan(), 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.setText(string);

    }
}