package com.example.tanaj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VerseAdapter extends RecyclerView.Adapter {

    private final List<VerseModel> words = new ArrayList<>();

    public VerseAdapter(List<VerseModel> words) {
        if(words != null){
            this.words.addAll(words);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new VerseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VerseHolder) holder).bindData(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item;
    }
}
