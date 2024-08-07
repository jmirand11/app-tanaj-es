package com.example.tanaj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ActivityRead extends AppCompatActivity {
    private Spinner book;
    private Spinner chapter;
    private Spinner verse;

    private ImageButton imb;
    private PopupMenu popupMenu;
    private Intent intent;

    private ImageView play;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Handler handler;

    private DataBaseHelper db;
    private List<String> books;
    private List<String> chapters;
    private List<String> verses;
    private int bookLabel;
    private int bookSelec;
    private int chapSelec;
    private int versSelec;
    private int startVerse = 0;
    private boolean start = true;
    private int ptl;

    private int b;
    private int c;
    private int v;

    // Image buttons
    private ImageButton back;
    private ImageButton next;

    // Show text from database
    List<VerseModel> list;
    RecyclerView recyclerView;
    FlexboxLayoutManager layoutManager;
    RelativeLayout rl;

    public static final String MYPREFERENCES = "nightModePrefs";

    SharedPreferences sharedPreferences;
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        try {
            getSupportActionBar().hide();
        }catch(Exception e) {
            Log.d("ERROR", "Error hiding the action bar");
        }

        recyclerView = findViewById(R.id.read_vp);
        rl = findViewById(R.id.rl_read);
        rl.setVisibility(View.GONE);
        
        // Set loading
        startLoading();

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        ptl = sharedPreferences.getInt("ptl", 3);
        readTransliterations();

        // Load database
        db = new DataBaseHelper(getApplicationContext());

        mediaPlayer = new MediaPlayer();

        // Options for spinners
        configSpinners();

        // Options for navigation buttons
        configNavigation();

        // Options for settings
        configOptions();

        // Options for mediaplayer
        configAudio();

        // Options for search in Strong's dictionary
        confiSearch();
    }

    private ProgressDialog progress;
    private void startLoading() {

    }

    private ImageButton search;
    private ProgressDialog mProgressDialog;
    private void confiSearch() {
        search = findViewById(R.id.read_search);





        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog = new ProgressDialog(ActivityRead.this);
                // Set progressdialog title
                mProgressDialog.setTitle("Concordancia Strong");
                // Set progressdialog message
                mProgressDialog.setMessage("Cargando...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCancelable(false);
                // Show progressdialog
                mProgressDialog.show();

                Intent intent = new Intent(ActivityRead.this, ActivitySearch.class);
                startActivity(intent);
            }
        });
    }

    private void configNavigation() {
        // Options for buttons
        next = findViewById(R.id.read_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                seekBar.setVisibility(View.INVISIBLE);
                seekBar.setProgress(0);
                play.setImageResource(R.drawable.ic_play);

                if(mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        mediaPlayer.stop();


                    }
                }

                // Check number of verses
                int nv = verse.getAdapter().getCount() - 1;
                // Current position
                int cv = verse.getSelectedItemPosition();
                // Move to the next verse
                if (cv < nv){
                    verse.setSelection(cv+1);
                }else{
                    // Check number of chapters
                    int nc = chapter.getAdapter().getCount() - 1;
                    // Current position
                    int cc = chapter.getSelectedItemPosition();
                    // Move to the next chapter
                    if(cc < nc){
                        chapter.setSelection(cc+1);
                    }else{
                        // Check number of books
                        int nb = book.getAdapter().getCount()-1;
                        // Current book
                        int cb = book.getSelectedItemPosition();
                        // Move to the next book
                        if(cb < nb){
                            book.setSelection(cb+1);
                        }else{
                            // Return to the first book
                            book.setSelection(0);
                        }
                    }
                }
            }
        });

        back = findViewById(R.id.read_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                seekBar.setVisibility(View.INVISIBLE);
                seekBar.setProgress(0);
                play.setImageResource(R.drawable.ic_play);

                if(mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        mediaPlayer.stop();
                    }
                }

                // Number of verse, chapter, book
                int nv, nc, nb;
                // Current verse, chapter book
                int cv, cc, cb;
                // Current position
                cv = verse.getSelectedItemPosition();
                // Move to the previous verse
                if (cv > 0){
                    verse.setSelection(cv-1);
                }else{
                    // Number of chapters
                    nc = chapter.getAdapter().getCount()-1;
                    // Current chapter
                    cc = chapter.getSelectedItemPosition();
                    if(cc > 0){
                        // Move to previous chapter
                        chapter.setSelection(cc-1);
                        // Get number of verses
                        delayedVerse();
                    }else{
                        // Number of books
                        nb = book.getAdapter().getCount()-1;
                        // Current selected book
                        cb = book.getSelectedItemPosition();
                        if(cb > 0){
                            book.setSelection(cb-1);
                            delayedChapter();
                        }else{
                            book.setSelection(nb);
                            delayedChapter();
                        }
                    }
                }
            }
        });
    }

    private void delayedVerse(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int p = verse.getAdapter().getCount() - 1;
                verse.setSelection(p);
            }
        }, 100);
    }

    private void delayedChapter(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int p = chapter.getAdapter().getCount() - 1;
                chapter.setSelection(p);
                delayedVerse();
            }
        }, 100);
    }

    private void configSpinners(){
        //db
        final DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());

        bookLabel = sharedPreferences.getInt("s03l", 0);
        bookSelec = sharedPreferences.getInt("bookSelec", 0);
        chapSelec = sharedPreferences.getInt("chapSelec", 0);
        versSelec = sharedPreferences.getInt("versSelec", 0);

        books  = dbhelper.getBooks(bookLabel);

        if(bookLabel == 1){
            books = applyFormatTl(ptl, books);
        }

        // Widgets
        book = findViewById(R.id.sp_book);
        b = bookSelec + 1;
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, books.toArray(new String[books.size()]));
        book.setAdapter(adapter1);
        book.setSelection(bookSelec);

        // Config chapters
        chapter = findViewById(R.id.sp_chapter);

        verse = findViewById(R.id.sp_verse);

        verse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("versSelec", position);
                editor.apply();

                v = position + 1;

                if(startVerse <= 2){
                    startVerse++;
                    if(startVerse == 2){
                        verse.setSelection(versSelec);
                    }
                }

                getTextFromDb();

                if(mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }

                fetchAudioFromFirebase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Current chapter
                c = position + 1;

                if (start){
                    chapter.setSelection(chapSelec);
                    start = false;
                }

                // Get chapters
                verses = dbhelper.getVerses(b, c);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<>(ActivityRead.this, R.layout.spinner_item, verses);
                adapter3.notifyDataSetChanged();
                verse.setAdapter(adapter3);
                verse.setSelection(0);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("chapSelec", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        book.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Set current book number
                b = position + 1;

                // Get chapters
                chapters = dbhelper.getChapters(b);
                adapter2 = new ArrayAdapter<>(ActivityRead.this, R.layout.spinner_item, chapters);
                chapter.setAdapter(adapter2);
                chapter.setSelection(0);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("bookSelec", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getTextFromDb() {

        // Recycler view
        int bk = book.getSelectedItemPosition() + 1;
        int cp = chapter.getSelectedItemPosition() + 1;
        int vr = verse.getSelectedItemPosition() + 1;

        list = db.getVerse(bk, cp, vr);
        String tl;

        for(VerseModel vm : list){
            tl = vm.getTransliteration();
            tl = Transliteration.applyFormatSample(ptl, tl, equivalences);
            vm.setTransliteration(tl);
            tl = vm.getsTransl();
            tl = Transliteration.applyFormatSample(ptl, tl, equivalences);
            vm.setsTransl(tl);
        }



        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());

        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);

        VerseAdapter adapter =  new VerseAdapter(list);
        recyclerView.setAdapter(adapter);

    }

    private List<String> applyFormatTl(int pos, List<String> l){
        List<String> L = new ArrayList<>();

        for(int i = 0; i < l.size(); i++){
            L.add(Transliteration.applyFormatSample(pos, l.get(i), equivalences));
        }

        return L;
    }

    List<String> equivalences;
    List<Integer> modifiable;
    Set<String> setTl;
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

    private void configOptions(){
        // Options of the Popupmenu
        imb = findViewById(R.id.read_options);
        popupMenu = new PopupMenu(this, imb);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_settings, popupMenu.getMenu());
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.action_setting):
                        intent = new Intent(ActivityRead.this, Settings000.class);
                        break;
                    case (R.id.action_about):
                        intent  = new Intent(ActivityRead.this, ActivityAbout.class);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
                return false;
            }
        });
    }

    private void configAudio(){
        play = findViewById(R.id.read_play);
        seekBar = findViewById(R.id.read_prog);
        handler = new Handler();

        play.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);

        fetchAudioFromFirebase();
    }

    private  int p;
    private void fetchAudioFromFirebase() {
        String url = genAudioUrl();

        int[] audios = {R.raw.b001_c001_v001, R.raw.b001_c001_v002, R.raw.b001_c001_v003, R.raw.b001_c001_v004};
        Random r = new Random();
        p = audios[r.nextInt(audios.length)];

        //mediaPlayer = MediaPlayer.create(this, p);
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    // Download url of file
                    final String url = uri.toString();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            play.setVisibility(View.VISIBLE);
                            seekBar.setVisibility(View.VISIBLE);
                            seekBar.setMax(mp.getDuration());
                            changeProgress();
                        }
                    });

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            play.setImageResource(R.drawable.ic_play);
                            Log.d("test", "finished");
                        }
                    });
                    // wait for media player to get prepare
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", e.getMessage());
                    }
                });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAction();
            }
        });


    }

    private String genAudioUrl() {
        String url;

        int nbook, nchap, nvers;
        nbook = book.getSelectedItemPosition() + 1;
        nchap = chapter.getSelectedItemPosition() + 1;
        nvers = verse.getSelectedItemPosition() + 1;

        String sbook, schap, svers;

        if(nbook > 1 || nchap > 2){
            nbook = 1;
            nchap = 1;
            nvers = 1;

        }

        sbook = Integer.toString(nbook);
        schap = Integer.toString(nchap);
        svers = Integer.toString(nvers);

        sbook = formatSelection("b", sbook) + "_";
        schap = formatSelection("c", schap) + "_";
        svers = formatSelection("v", svers) + ".mp3";

        url = "gs://tanaj-f8346.appspot.com/audios/" + sbook + schap + svers;

        Log.d("test", url);

        return url;
    }
    
    private String formatSelection(String label, String tag){
        String s = "";

        switch (tag.length()){
            case 1:
                s = label + "00" + tag;
                break;
            case 2:
                s = label + "0" + tag;
                break;
            case 3:
                s = label + tag;
                break;
            default:
                s = label + "000";
                break;
        }
        
        return s;
    }

    private void playAction() {
        if(mediaPlayer.getDuration()==0){
            mediaPlayer.pause();
            Toast.makeText(ActivityRead.this, "Base de datos no disponible", Toast.LENGTH_SHORT).show();
            play.setImageResource(R.drawable.ic_play);
            return;
        }

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            play.setImageResource(R.drawable.ic_play);
        }else{
            mediaPlayer.start();
            play.setImageResource(R.drawable.ic_pause);
            changeProgress();
        }
    }

    private void changeProgress() {
        try{
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
        }catch (Exception e){
            seekBar.setProgress(0);
            e.printStackTrace();
        }

        if (mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeProgress();
                }
            };
            handler.postDelayed(runnable, 100);
        }
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        rl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            mProgressDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}