package com.example.tanaj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Settings000 extends AppCompatActivity {

    // General widgets
    private LinearLayout dynamicContent;
    private TabLayout tabLayout;
    private View wizardView;

    // Shared Preferences
    private SharedPreferences sharedPreferences;
    public static final String MYPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    public static final String KEY_FIRSTTIME = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings000);

        // Read sharedpreferences
        // Get shared preferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Check first time
        firstTime = sharedPreferences.getBoolean(KEY_FIRSTTIME, true);

        // Get widgets
        tabLayout = findViewById(R.id.tabs);
        dynamicContent = findViewById(R.id.s004_dc);

        // Add configurations
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_3));

        // Select first tab
        tabLayout.selectTab(tabLayout.getTabAt(0));
        actionsTab00();

        // Configure widgets
        configureWidgets();

        // Configure back button
        configureBack();

    }

    private void configureBack() {
        ImageButton back = findViewById(R.id.s000_bback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    boolean firstTime;
    private void isFirstTime(int rid) {
        Button bnext = findViewById(rid);
        if(firstTime){
           bnext.setText(R.string.btnNext);
        }else{
            bnext.setText(R.string.btnNext);
        }

        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tabPosition = tabLayout.getSelectedTabPosition();
                // Set actions
                if(tabPosition < 2){
                    tabLayout.selectTab(tabLayout.getTabAt(tabPosition + 1));
                }else{
                    goBack();
                }
            }
        });
    }

    // Actions for view
    int stes;
    int stet, stet1;
    private ArrayList<TextView> s03_txt;
    private ArrayList<CheckBox> s03_chk;
    private ArrayList<Boolean> s03_ch;
    int s03_i;
    private ArrayList<TextView> s03_bn;
    private String[] books;
    int i;
    int ptl;
    private void actionsTab02() {
        dynamicContent.removeAllViews();
        wizardView = getLayoutInflater()
                .inflate(R.layout.activity_settings003, dynamicContent, false);

        dynamicContent.addView(wizardView);

        // Actions for next button
        isFirstTime(R.id.s003_bnext);

        // Load fonts settings hebrew
        steh = sharedPreferences.getInt("steh", 30) + 8;
        fteh = sharedPreferences.getInt("fteh", 2);
        tf = getFont(fteh);

        // Spanish - User language
        stes = sharedPreferences.getInt("stes", 12) + 8;

        // Transliteration
        stet = sharedPreferences.getInt("stet", 12) + 8;
        stet1 = (int) (stet * 0.9);

        s03_i = sharedPreferences.getInt("s03l", 0);

        // User preferences of checkboxes
        s03_ch = new ArrayList<>();
        s03_ch.add(sharedPreferences.getBoolean("s03_ch0", true));
        s03_ch.add(sharedPreferences.getBoolean("s03_ch1", true));
        s03_ch.add(sharedPreferences.getBoolean("s03_ch2", true));
        s03_ch.add(sharedPreferences.getBoolean("s03_ch3", true));

        // Get widgets
        // Checkboxes
        s03_chk = new ArrayList<>();
        s03_chk.add((CheckBox) findViewById(R.id.s03_chk00));
        s03_chk.add((CheckBox) findViewById(R.id.s03_chk01));
        s03_chk.add((CheckBox) findViewById(R.id.s03_chk02));
        s03_chk.add((CheckBox) findViewById(R.id.s03_chk03));

        // Textviews
        s03_txt = new ArrayList<>();
        s03_txt.add((TextView) findViewById(R.id.s03_tx00));
        s03_txt.add((TextView) findViewById(R.id.s03_tx01));
        s03_txt.add((TextView) findViewById(R.id.s03_tx02));
        s03_txt.add((TextView) findViewById(R.id.s03_tx03));
        s03_txt.add((TextView) findViewById(R.id.s03_tx04));

        // Set text sizes and transliteration
        s03_txt.get(4).setTextSize(steh);
        s03_txt.get(4).setTypeface(tf);
        String text = (String) s03_txt.get(2).getText();
        Log.d("test", "before: " + text);

        // Get transliteration preferences
        readTransliterations();
        ptl = sharedPreferences.getInt("ptl", 0);

        // Update text
        text = Transliteration.applyFormatSample(ptl, text, equivalences);
        Log.d("test", "after: " + text);
        s03_txt.get(2).setText(text);

        // Custom text
        s03_txt.get(0).setTextSize(stet1);
        s03_txt.get(1).setTextSize(stes);
        s03_txt.get(2).setTextSize(stet);
        s03_txt.get(3).setTextSize(stet1);

        // Set checkbox statuses
        for(i = 0; i < s03_ch.size(); i++){
            // Set saved status
            s03_chk.get(i).setChecked(s03_ch.get(i));

            if(! s03_chk.get(i).isChecked()){
                s03_txt.get(i).setVisibility(View.GONE);
            }else{
                s03_txt.get(i).setVisibility(View.VISIBLE);
            }
            // Set widgets actions
            s03_displayTxt(s03_chk.get(i), i);
        }

        // User preferences of book names
        s03_bn = new ArrayList<>();
        s03_bn.add((TextView) findViewById(R.id.s03_tx05));
        s03_bn.add((TextView) findViewById(R.id.s03_tx06));
        s03_bn.add((TextView) findViewById(R.id.s03_tx07));
        s03_bn.add((TextView) findViewById(R.id.s03_tx08));
        s03_bn.add((TextView) findViewById(R.id.s03_tx09));

        s03_configBookPrefs();
    }

    private Spinner s03_sp01;
    private void s03_configBookPrefs() {

        // Get widget
        s03_sp01 = findViewById(R.id.s03_sp01);

        s03_sp01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBookOptions(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s03_sp01.setSelection(s03_i);

    }

    private void getBookOptions(int pos) {
        String text;

        switch (pos){
            case 0:
                books = getResources().getStringArray(R.array.booksDF);
                break;
            case 1:
                books = getResources().getStringArray(R.array.booksHT);
                break;
            case 2:
                books = getResources().getStringArray(R.array.booksHE);
                break;
            default:
                books = getResources().getStringArray(R.array.booksDF);
                break;
        }

        for(int i = 0; i < s03_bn.size(); i++){
            // Set text
            if(pos == 1){
                text = books[i];
                text = Transliteration.applyFormatSample(ptl, text, equivalences);
                text = text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
            }else{
                text = books[i];
            }
            s03_bn.get(i).setText(text);

            // Set format
            if(pos == 2){
                s03_bn.get(i).setTextSize(steh);
                s03_bn.get(i).setTypeface(tf);
            }else{
                s03_bn.get(i).setTextSize(stes);
                s03_bn.get(i).setTypeface(Typeface.DEFAULT);
            }
        }

        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putInt("s03l", pos);
        editor.apply();
    }

    private void s03_displayTxt(CheckBox checkBox, final int position){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update visibility
                if(isChecked){
                    s03_txt.get(position).setVisibility(View.VISIBLE);
                }else{
                    s03_txt.get(position).setVisibility(View.GONE);
                }
                // Save preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String label = "s03_ch" + position;
                Log.d("label", label);
                editor.putBoolean(label, isChecked);
                editor.apply();
            }
        });
    }

    private void readTransliterations(){
        // Read configurations
        setTl =  new LinkedHashSet<>();
        setTl = sharedPreferences.getStringSet("setTl", null);
        equivalences = new ArrayList<>();
        modifiable = new ArrayList<>();

        if (setTl == null){
            setTl = Transliteration.createTransliterations();
        }

        // Read transliteration options
        equivalences.addAll(setTl);
        if(setTl == null){
            saveTransliterations();
        }
        modifiable = Transliteration.getModifiables(equivalences);

    }

    private TextView[] s02_txtHe = new TextView[5];
    private TextView[] s02_txtTl = new TextView[5];
    private String [] s02_txtOl =  new String[5];
    private Spinner s02_sp01;
    private void actionsTab01() {
        // Setup view
        dynamicContent.removeAllViews();
        wizardView = getLayoutInflater()
                .inflate(R.layout.activity_settings002, dynamicContent, false);

        dynamicContent.addView(wizardView);

        // Get TextView Widgets
        s002_actions001();

        // Get Spinner and transliteration settings
        s002_actions000();

        // Actions for next button
        isFirstTime(R.id.s002_bnext);
    }

    Spinner p02_tl, p02_opt1, p02_opt2;
    LinearLayout s002_l;
    List<String> equivalences;
    List<Integer> modifiable;
    Set<String> setTl;
    int pos01, pos02 = 0;
    private void s002_actions000() {
        // Read configurations
        readTransliterations();

        // LinearLayout
        s002_l = findViewById(R.id.s002_l0);
        p02_opt1 = findViewById(R.id.p02_opt01);
        p02_opt2 = findViewById(R.id.p02_opt02);

        Toast toast;
        // There is not transliterations
        if (setTl == null){
            setTl = Transliteration.createTransliterations();
            // toast = Toast.makeText(getApplicationContext(), "Null transliteration", Toast.LENGTH_SHORT);
            // toast.show();
        }

        // Get transliteration widget
        p02_tl = findViewById(R.id.s002_sp000);
        int ptl = sharedPreferences.getInt("ptl", 3);
        p02_tl.setSelection(ptl);

        p02_tl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formatTL(position);
                if(position == 6){
                    s002_l.setVisibility(View.VISIBLE);
                    s002_formatOptions01();
                }else{
                    s002_l.setVisibility(View.GONE);
                }
                saveSPInt("ptl", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveSPInt(String name, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public void s002_formatOptions01(){
        List<String> l01 = new ArrayList<>();
        for(int i : modifiable){
            String temp = equivalences.get(i);
            String [] values = temp.split(";");
            Log.d("Temp", values[0] + " -> " + values[8] + " : " + values[7]);
            l01.add(values[8]);
        }

        pos01 = sharedPreferences.getInt("ptl01", 0);

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, l01);

        // Set options of consonants
        p02_opt1.setAdapter(adapter);
        p02_opt1.setSelection(0);
        tf = getFont(fteh);


        // Set actions
        p02_opt1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos01 = position;
                s002_formatOptions002(position);
                int s = (int) ((TextView) parent.getChildAt(0)).getTextSize();
                ((TextView) parent.getChildAt(0)).setTextSize( (int) (s * 0.8));
                ((TextView) parent.getChildAt(0)).setTypeface(tf);
                saveSPInt("ptl01", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    List<String> values;
    public void s002_formatOptions002(int position){
        // Check position of modifiable character
        int k = modifiable.get(position);
        // Get string of equivalences of character
        String q = equivalences.get(k);
        // Array of all values
        String [] v = q.split(";");
        // List with unique values
        values = new ArrayList<>();
        // Add the custom value
        values.add(v[7]);

        for(int i = 1; i < v.length - 1; i++){
            if(!values.contains(v[i]) ){
                values.add(v[i]);
            }
        }

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, values);

        // Set options to customization
        p02_opt2.setAdapter(adapter);
        p02_opt2.setSelection(0);

        p02_opt2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos02 = position;
                int s = (int) ((TextView) parent.getChildAt(0)).getTextSize();
                ((TextView) parent.getChildAt(0)).setTextSize( (int) (s * 0.8));
                s002_customEquivalence(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
    }

    private void s002_customEquivalence(int position) {
        // Get value to update
        int i = modifiable.get(pos01);
        String j = equivalences.get(i);

        // Update
        List<String> options = Arrays.asList(equivalences.get(i).split(";"));
        options.set(7, values.get(position));
        StringBuilder newValue = new StringBuilder(options.get(0));
        for(int k = 1; k < options.size(); k++){
            newValue.append(";");
            newValue.append(options.get(k));
        }

        // Add update
        equivalences.set(i, newValue.toString());

        // Set format to text sample
        formatTL(6);

        // Save custom user options
        saveTransliterations();

    }

    private void saveTransliterations() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Clear old settings
        setTl.clear();
        // Add new settings
        setTl.addAll(equivalences);
        // Save settings
        editor.putStringSet("setTl", setTl);
        // Apply changes
        editor.apply();
    }

    private void formatTL(int position){
        String text;
        for (int i = 0; i < s02_txtTl.length; i++){
            text = s02_txtOl[i];
            text = Transliteration.applyFormatSample(position, text, equivalences);
            s02_txtTl[i].setText(text);
        }
    }

    int steh;
    int fteh;
    private void s002_actions001() {
        // Hebrew widgets
        s02_txtHe[0] = findViewById(R.id.s002_txm000a);
        s02_txtHe[1] = findViewById(R.id.s002_txm001a);
        s02_txtHe[2] = findViewById(R.id.s002_txm002a);
        s02_txtHe[3] = findViewById(R.id.s002_txm003a);
        s02_txtHe[4] = findViewById(R.id.s002_txm004a);

        // Translitered widgets
        s02_txtTl[0] = findViewById(R.id.s002_txm000b);
        s02_txtTl[1] = findViewById(R.id.s002_txm001b);
        s02_txtTl[2] = findViewById(R.id.s002_txm002b);
        s02_txtTl[3] = findViewById(R.id.s002_txm003b);
        s02_txtTl[4] = findViewById(R.id.s002_txm004b);

        // Set original transliteration
        s02_txtOl[0] = getResources().getString(R.string.s002_sample00b);
        s02_txtOl[1] = getResources().getString(R.string.s002_sample01b);
        s02_txtOl[2] = getResources().getString(R.string.s002_sample02b);
        s02_txtOl[3] = getResources().getString(R.string.s002_sample03b);
        s02_txtOl[4] = getResources().getString(R.string.s002_sample04b);

        // Read font sizes
        steh = sharedPreferences.getInt("steh", 30) + 8;
        int stet = sharedPreferences.getInt("stet", 12) + 8;

        // Read fonts
        fteh = sharedPreferences.getInt("fteh", 2);
        tf = getFont(fteh);

        for (TextView tx : s02_txtHe) {
            tx.setTypeface(tf);
            tx.setTextSize(steh);
        }

        for (TextView tx : s02_txtTl) {
            tx.setTextSize(stet);
        }
    }

    // Theme selection
    private SwitchCompat s001_switch;
    private TextView s01_tes, s01_tess, s01_tet, s01_tets, s01_teh, s01_tehs;
    Typeface tf;
    private String a;
    private void actionsTab00() {
        // Setup view
        dynamicContent.removeAllViews();
        wizardView = getLayoutInflater()
                .inflate(R.layout.activity_settings001, dynamicContent, false);

        dynamicContent.addView(wizardView);

        // Configure switch
        s001_actions00();

        // Configure user language
        s001_actions01();

        // Configure transliteration options
        s001_actions02();

        // Configure hebrew options
        s001_actions03();
    }

    private void s001_actions03() {
        // Get widets
        s01_teh  = findViewById(R.id.s001_teh);
        s01_tehs = findViewById(R.id.s001_tehs);
        SeekBar s01_seh = findViewById(R.id.s001_steh);
        Spinner p01_teh = findViewById(R.id.s001_pteh);

        // Read user configuration
        int steh = sharedPreferences.getInt("steh", 30);

        // Set text
        a = getString(R.string.settings001_str005) + " " + (steh + 8);
        s01_teh.setText(a);
        s01_seh.setProgress(steh);
        s01_tehs.setTextSize(steh + 8);

        // Set font
        int fteh = sharedPreferences.getInt("fteh", 2);
        p01_teh.setSelection(fteh);

        s01_seh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Set text
                a = getString(R.string.settings001_str005) + " " + (progress + 8);
                s01_teh.setText(a);
                s01_tehs.setTextSize(progress + 8);
                s001_save(progress, "steh");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        p01_teh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fteh", position);
                editor.apply();

                tf = getFont(position);

                s01_tehs.setTypeface(tf);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Typeface getFont(int position){
        switch (position) {
            case 0:
                tf = Typeface.SANS_SERIF;
                break;
            case 1:
                tf = ResourcesCompat.getFont(getApplicationContext(), R.font.hadasah);
                break;
            case 2:
                tf = ResourcesCompat.getFont(getApplicationContext(), R.font.sbl);
                break;
            case 3:
                tf = ResourcesCompat.getFont(getApplicationContext(), R.font.stam);
                break;
            default:
                tf = ResourcesCompat.getFont(getApplicationContext(), R.font.sbl);
                break;
        }
        return tf;
    }

    private void s001_actions02(){
        // Get widets
        s01_tet  = findViewById(R.id.s001_tet);
        s01_tets = findViewById(R.id.s001_tets);
        SeekBar s01_set = findViewById(R.id.s001_stet);

        // Read user configuration
        int stet = sharedPreferences.getInt("stet", 12);

        // Set text
        a = getString(R.string.settings001_str005) + " " + (stet + 8);
        s01_tet.setText(a);
        s01_set.setProgress(stet);
        s01_tets.setTextSize(stet + 8);

        s01_set.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Set text
                a = getString(R.string.settings001_str005) + " " + (progress + 8);
                s01_tet.setText(a);
                s01_tets.setTextSize(progress + 8);
                s001_save(progress, "stet");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void s001_actions01() {
        // Get widets
        s01_tes  = findViewById(R.id.s001_tes);
        s01_tess = findViewById(R.id.s001_tess);
        SeekBar s01_ses = findViewById(R.id.s001_stes);

        // Read user configuration
        int stes = sharedPreferences.getInt("stes", 12);

        // Set text
        a = getString(R.string.settings001_str005) + " " + (stes + 8);
        s01_tes.setText(a);
        s01_ses.setProgress(stes);
        s01_tess.setTextSize(stes + 8);

        s01_ses.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Set text
                a = getString(R.string.settings001_str005) + " " + (progress + 8);
                s01_tes.setText(a);
                s01_tess.setTextSize(progress + 8);
                s001_save(progress, "stes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void s001_save(int progress, String var){
        // Save language settings
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(var, progress);
        editor.apply();
    }

    private void s001_actions00(){
        // Check if it is first time
        isFirstTime(R.id.s001_bnext);

        s001_switch = findViewById(R.id.s01_sw00);
        checkNightModeActivate(s001_switch);

        s001_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    s001_switch.setText(R.string.settings000_str003);
                    saveNightModeState(true);
                }else{
                    s001_switch.setText(R.string.settings000_str002);
                    saveNightModeState(false);
                }
            }
        });

    }

    private void saveNightModeState(boolean nightMode) {
        // Save setings
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();

        // Apply changes
        finishAffinity();
        Intent intent = new Intent(Settings000.this, MainActivity.class);
        intent.putExtra("goToSettings", true);
        startActivity(intent);
    }

    private void checkNightModeActivate(SwitchCompat sw00) {
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)){
            sw00.setChecked(true);
            sw00.setText(R.string.settings000_str003);
        }else{
            sw00.setChecked(false);
            sw00.setText(R.string.settings000_str002);
        }
    }

    private void configureWidgets() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position){
                    case 0:
                        actionsTab00();
                        break;
                    case 1:
                        actionsTab01();
                        break;
                    default:
                        actionsTab02();
                        break;

                }
                Log.d("Tablayout: ", String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myString = "1;;’;a;א";

                String [] myArray = myString.split(";");

                for (String s : myArray){
                    System.out.println("Test me: " + s);
                }

                boolean a = myArray[1].equals("");
                Log.d("Test me: 1 ", String.valueOf(a));

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        goBack();
    }

    private void goBack(){
        Toast.makeText(getApplicationContext(), R.string.btnSaveMessage, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings000.this, ActivityRead.class);
        startActivity(intent);
    }
}