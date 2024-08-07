package com.example.tanaj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StrongAdapter extends RecyclerView.Adapter {

    private final List<StrongModel> cites = new ArrayList<>();

    public StrongAdapter(List<StrongModel> cites) {
        if(cites != null){
            this.cites.addAll(cites);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new StrongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((StrongHolder) holder).bindData(cites.get(position));
    }

    @Override
    public int getItemCount() {
        return cites.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item2;
    }
}
