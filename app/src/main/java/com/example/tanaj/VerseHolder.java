package com.example.tanaj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VerseHolder extends RecyclerView.ViewHolder {
    // Textviews
    private final TextView it00;
    private final TextView it01;
    private final TextView it02;
    private final TextView it03;
    private final TextView it04;

    // Settings
    private final SharedPreferences sharedPreferences;
    private static final String MYPREFERENCES = "nightModePrefs";
    // Hebrew font
    private Typeface tf;
    private final int steh;
    private final int stes;
    private final int stet;
    private final int stet1;
    // Visibility of fields
    private final ArrayList<Boolean> check;

    public VerseHolder(final View itemView){
        super(itemView);

        // Textviews with information
        it00 = itemView.findViewById(R.id.it_00);
        it01 = itemView.findViewById(R.id.it_01);
        it02 = itemView.findViewById(R.id.it_02);
        it03 = itemView.findViewById(R.id.it_03);
        it04 = itemView.findViewById(R.id.it_04);

        // Read preferences
        sharedPreferences = itemView.getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Hebrew text
        steh = sharedPreferences.getInt("steh", 30) + 8;
        int fteh = sharedPreferences.getInt("fteh", 2);
        tf = getFont(fteh);

        // Translated text
        stes = sharedPreferences.getInt("stes", 12) + 8;

        // Transliterated text and menus
        stet = sharedPreferences.getInt("stet", 12) + 8;
        stet1 = (int)(stet*0.9);

        // Read preferences of visibility
        check = new ArrayList<>();
        // Strong
        check.add(sharedPreferences.getBoolean("s03_ch0", true));
        // Translation
        check.add(sharedPreferences.getBoolean("s03_ch1", true));
        // Transliteration
        check.add(sharedPreferences.getBoolean("s03_ch2", true));
        // Morphology
        check.add(sharedPreferences.getBoolean("s03_ch3", true));
    }

    public void bindData(final VerseModel viewModel){
        // Strongs text
        it00.setText(viewModel.getStrong());
        it00.setTextSize(stet1);
        if(!check.get(0)){
            it00.setVisibility(View.GONE);
        }
        it00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("Strong " + viewModel.getStrong());

                // set the custom layout
                final View customLayout =  LayoutInflater.from(itemView.getContext()).inflate(R.layout.layout_strong, null);
                builder.setView(customLayout);

                configStrong(customLayout, viewModel);

                builder.setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Hebrew text
        it01.setText(Html.fromHtml(viewModel.getHebrew()));
        it01.setTextSize(steh);
        it01.setTypeface(tf);

        // Spanish text
        it02.setText(viewModel.getTranslation());
        it02.setTextSize(stes);
        if(!check.get(1)){
            it02.setVisibility(View.GONE);
        }

        // Transliteration
        it03.setText(viewModel.getTransliteration());
        it03.setTextSize(stet);
        if(!check.get(2)){
            it03.setVisibility(View.GONE);
        }

        // Morphology
        it04.setText(viewModel.getMorphology());
        it04.setTextSize(stet1);
        if(!check.get(3)){
            it04.setVisibility(View.GONE);
        }

        // Configure Lemma listener
        it04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                //builder.setTitle("Categoría gramatical \n" + viewModel.getMorphology());

                // set the custom layout
                final View customLayout =  LayoutInflater.from(itemView.getContext()).inflate(R.layout.layout_lemma, null);
                builder.setView(customLayout);

                TextView title = customLayout.findViewById(R.id.lm_tx01);
                title.setText(viewModel.getMorphology());

                TextView message;
                message = customLayout.findViewById(R.id.lm_tx00);

                String m = viewModel.getGrammar();

                message.setText(m);

                builder.setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private Typeface getFont(int position){
        switch (position) {
            case 0:
                tf = Typeface.SANS_SERIF;
                break;
            case 1:
                tf = ResourcesCompat.getFont(itemView.getContext(), R.font.hadasah);
                break;
            case 2:
                tf = ResourcesCompat.getFont(itemView.getContext(), R.font.sbl);
                break;
            case 3:
                tf = ResourcesCompat.getFont(itemView.getContext(), R.font.stam);
                break;
            default:
                tf = ResourcesCompat.getFont(itemView.getContext(), R.font.sbl);
                break;
        }
        return tf;
    }

    private void configStrong(final View v, final VerseModel vm){
        // Field: hebrew
        // Stron fields
        TextView sf_00 = v.findViewById(R.id.str_hebrew);
        sf_00.setText(vm.getsHebrew());
        sf_00.setTextSize(steh);
        sf_00.setTypeface(tf);

        // Field: transliteration
        TextView sf_01 = v.findViewById(R.id.str_translit);
        sf_01.setText(vm.getsTransl());

        // Field: translation
        TextView sf_02 = v.findViewById(R.id.str_def);
        sf_02.setText(vm.getsDefint());

        // Field: Lemma
        TextView sf_03 = v.findViewById(R.id.str_lemma);
        sf_03.setText(vm.getsLemma());

        // Field: Origin
        TextView sf_04 = v.findViewById(R.id.str_origin);
        sf_04.setText(vm.getsOrigin());

        // Field: More information
        TextView sf_05 = v.findViewById(R.id.str_lfreq);
        if(vm.getStrong().equals("-")){
            sf_05.setVisibility(View.GONE);
        }
        sf_05.setPaintFlags(sf_05.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        sf_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Strong preferences
                startSearch(v, vm);
            }
        });
    }

    private void startSearch(final View v, VerseModel vm) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int ref = Integer.parseInt(vm.getStrong().replace("h", ""));
        editor.putInt("sref", ref);
        editor.apply();

        Toast.makeText(v.getContext(),"Cargando...", Toast.LENGTH_LONG).show();

        // Start Activity
        Intent intent = new Intent(v.getContext(), ActivitySearch.class);
        v.getContext().startActivity(intent);
    }

}
