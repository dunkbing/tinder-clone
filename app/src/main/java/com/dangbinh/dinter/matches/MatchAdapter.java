package com.dangbinh.dinter.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dangbinh.dinter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangbinh on 18/11/2020.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder>{
    private List<MatchObject> matchesList = new ArrayList<>();
    private final Context context;

    public MatchAdapter(Context context){
        this.context = context;
    }

    public void setMatchesList(List<MatchObject> matchesList) {
        this.matchesList.clear();
        this.matchesList.addAll(matchesList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MatchViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(String.format("%s", matchesList.get(position).getName()));
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        if (this.matchesList == null) return 0;
        return this.matchesList.size();
    }
}
