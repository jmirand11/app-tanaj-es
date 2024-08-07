package com.example.tanaj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ActivitySearch extends AppCompatActivity {

    // Config database
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try {
            // getSupportActionBar().hide();
            getSupportActionBar().setTitle("Concordancia Strong");
        }catch(Exception e) {
            Log.d("ERROR", "Error hiding the action bar");
        }

        // Instantiate db
        db = new DataBaseHelper(getApplicationContext());

        // Configure Hide
        configureHide();

        // Configure edit text
        setEditTextRange();

        // configure search button
        configSearch();

        // Read user preferences
        readPreferences();

        // Show back button
        showBackButton();

        // Perform first search
        goSearch();
    }

    // Show hide Textview
    private TextView hide;
    private LinearLayout linfo;
    private boolean isVisible;
    private void configureHide() {
        hide = findViewById(R.id.search_lbl1);
        linfo = findViewById(R.id.search_info);
        scroll = findViewById(R.id.search_scroll);

        isVisible = true;

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible){
                    hide.setText("▼ Información");
                    isVisible = false;
                    linfo.setVisibility(View.GONE);

                }else{
                    hide.setText("▲ Información");
                    isVisible = true;
                    linfo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showBackButton() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private SharedPreferences sharedPreferences;
    public static final String MYPREFERENCES = "nightModePrefs";
    private int sref;
    private int ptl;
    private int bookLabel;
    private List<String> books;
    private boolean theme;
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    private int bs;
    private int bd;
    private void readPreferences() {
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Get current theme
        theme = sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false);

        if(theme){
            bs = getResources().getColor(R.color.colorFillNight);
            bd = getResources().getColor(R.color.colorHeNight);
        }else{
            bs = getResources().getColor(R.color.colorFill);
            bd = getResources().getColor(R.color.colorHe);
        }

        // Get last searched reference
        sref = sharedPreferences.getInt("sref", 1);
        // Set text to edittext
        strong.setText(String.valueOf(sref));


        // Hebrew text
        steh = sharedPreferences.getInt("steh", 30) + 8;
        fteh = sharedPreferences.getInt("fteh", 2);
        tf = getFont(fteh);

        // Load transliteration preferences
        ptl = sharedPreferences.getInt("ptl", 3);
        readTransliterations();

        // Load book names
        DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
        bookLabel = sharedPreferences.getInt("s03l", 0);
        books  = dbhelper.getBooks(bookLabel);

        if(bookLabel == 1){
            books = applyFormatTl(ptl, books);
        }
    }

    private List<String> applyFormatTl(int pos, List<String> l){
        List<String> L = new ArrayList<>();

        for(int i = 0; i < l.size(); i++){
            L.add(Transliteration.applyFormatSample(pos, l.get(i), equivalences));
        }

        return L;
    }

    private ImageButton search;
    private void configSearch() {
        search = findViewById(R.id.search_go);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSearch();
            }
        });
    }

    private void goSearch(){

        String s = strong.getText().toString();
        if(s.length() > 0){
            int val = Integer.parseInt(s);
            Log.d("test", String.valueOf(val));
            Toast.makeText(getApplicationContext(), "Searching strong: " + val, Toast.LENGTH_LONG).show();
            hideKeyboard();

            scroll.setVisibility(View.VISIBLE);
            scroll.fullScroll(ScrollView.FOCUS_UP);

            getInfoStrong(val);

            saveSearch(val);
        }else{
            scroll.setVisibility(View.INVISIBLE);
        }

    }


    private TextView heb;
    private TextView trl;
    private TextView def;
    private TextView lmm;
    private TextView org;
    private TextView frq;
    private TextView nvr;
    private ProgressDialog progress;
    private  List<StrongModel> cites;
    public void getInfoStrong(int val) {
        // Convert Strong's ID to string
        String sid = Integer.toString(val);

        // Get widgets
        heb = findViewById(R.id.search_heb);
        trl = findViewById(R.id.search_trl);
        def = findViewById(R.id.search_def);
        lmm = findViewById(R.id.search_lmm);
        org = findViewById(R.id.search_org);

        // Set size
        heb.setTypeface(tf);
        heb.setTextSize(steh);

        // List with basic strong
        List<String> l1 = db.queryStrong(sid);

        String tl = l1.get(1);
        tl = Transliteration.applyFormatSample(ptl, tl, equivalences);

        heb.setText(l1.get(0));
        trl.setText(tl);
        //def.setText(l1.get(2));
        String temp = l1.get(2);
        temp = temp.replaceAll(" - ", "\n- ");
        def.setText(temp);
        lmm.setText(l1.get(3));

        String origin = l1.get(4);
        origin = origin.replaceAll(" - ", "\n- ");
        org.setText(origin);

        // Frequence of word
        frq = findViewById(R.id.search_freq);
        int f = db.freqStrong(sid);
        frq.setText("Frecuencia: ~" + f);

        // Number of verses
        nvr = findViewById(R.id.search_nver);
        List<List<String>> l2 = db.verseStrong(sid);

        cites = new ArrayList<>();

        for(int i=1; i <l2.size();i++){
            StrongModel s = new StrongModel(l2.get(i).get(0), l2.get(i).get(1), l2.get(i).get(2), books);
            cites.add(s);
        }

        nvr.setText("Número de versos: ~" + (cites.size()));

        Log.d("test", String.valueOf(cites.size()));

        setRecycler();

    }

    private ArrayList<String> data;
    ArrayAdapter<String> sd;
    public int TOTAL_LIST_ITEMS = 101;
    public int NUM_ITEMS_PAGE = 20;
    private int noOfBtns;
    private Button[] btns;
    private ListView listview;
    private TextView title;
    public void setRecycler() {
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        //linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        TOTAL_LIST_ITEMS = cites.size();

        listview = findViewById(R.id.search_recycler);


        title = findViewById(R.id.search_title);

        Btnfooter();

        data = new ArrayList<String>();

        /**
         * The ArrayList data contains all the list items

        for(int i=0;i<TOTAL_LIST_ITEMS;i++)
        {
            data.add("This is Item "+(i+1));
        }
         */

        for(int i=0; i <cites.size();i++){
            data.add( (i+1) + ". " + cites.get(i).getCite());
        }



        loadList(0);

        CheckBtnBackGroud(0);

    }

    private LinearLayout ll;
    private void Btnfooter() {
        int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

        ll = findViewById(R.id.btnLay);
        ll.removeAllViews();

        btns = new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(this);

            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));

            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }
    }

    private void CheckBtnBackGroud(int index) {


        title.setText("Página "+(index+1)+" de "+noOfBtns);
        for(int i=0;i<noOfBtns;i++)
        {


            if(index == 0){
                linfo.setVisibility(View.VISIBLE);
                hide.setText("▲ Información");
                isVisible = true;
            }else{
                linfo.setVisibility(View.GONE);
                hide.setText("▼ Información");
                isVisible = false;
            }
            
            scroll.fullScroll(View.FOCUS_UP);

            if(i==index)
            {
                btns[i].setBackgroundResource(R.drawable.button_shape);
                btns[i].setTextColor(bs);
            }
            else
            {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(bd);

            }
        }
    }

    private ArrayList<String> sort;
    int start;
    private void loadList(int number) {
         sort = new ArrayList<String>();

        start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<data.size())
            {
                sort.add(data.get(i));
            }
            else
            {
                break;
            }
        }
        sd = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                sort);
        listview.setAdapter(sd);
        Helper.getListViewSize(listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int p = position + start;
                Toast.makeText(getApplicationContext(), "verse: " + cites.get(p).getCite(), Toast.LENGTH_LONG).show();
                saveSelectedVerse(p);
                returnToRead();
            }
        });
    }

    private void returnToRead() {
        Intent intent = new Intent(ActivitySearch.this, ActivityRead.class);
        startActivity(intent);
    }


    private Typeface tf;
    private int fteh, steh;
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

    private void saveSearch(int val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Save strong ref
        editor.putInt("sref", val);
        editor.apply();
    }

    private void saveSelectedVerse(int p) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("bookSelec", cites.get(p).getBook()-1);
        editor.putInt("chapSelec", cites.get(p).getChapter()-1);
        editor.putInt("versSelec", cites.get(p).getVerse()-1);
        editor.apply();
    }

    private EditText strong;
    private NestedScrollView scroll;
    private void setEditTextRange() {
        scroll.setVisibility(View.INVISIBLE);
        strong = findViewById(R.id.search_sn);

        strong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strong.setCursorVisible(true);

                if(s.length()>0){
                    int value = Integer.parseInt(s.toString());

                    if(value < 1){
                        strong.setText("1");
                    }else if (value > 8674){
                        strong.setText("8674");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        strong.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    goSearch();
                    hideKeyboard();
                    scroll.fullScroll(ScrollView.FOCUS_UP);
                    return true;
                }
                return false;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
    }

    private List<String> equivalences;
    private List<Integer> modifiable;
    private Set<String> setTl;
    private void readTransliterations() {
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

}