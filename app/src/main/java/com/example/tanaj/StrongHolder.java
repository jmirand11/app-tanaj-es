package com.example.tanaj;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StrongHolder extends RecyclerView.ViewHolder {

    // Textviews
    private final TextView txt;

    // User preferences
    private SharedPreferences sharedPreferences;
    private static final String MYPREFERENCES = "nightModePrefs";

    public StrongHolder(@NonNull View itemView) {
        super(itemView);

        // Read preferences
        sharedPreferences = itemView.getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Get widget
        txt = itemView.findViewById(R.id.ct_00);
    }

    public void bindData(final StrongModel strongModel){
        txt.setText(strongModel.getCite());
        //Log.d("quote", strongModel.getCite());

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToNewCite(strongModel);
            }
        });
    }

    private void goToNewCite(StrongModel s) {
        // Save cites
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Toast.makeText(itemView.getContext(), s.getCite(), Toast.LENGTH_SHORT).show();
    }
}
