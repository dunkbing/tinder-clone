package com.dangbinh.dinter.Chat;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangbinh.dinter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangbinh on 18/11/2020.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<ChatObject> chatList = new ArrayList<>();

    public ChatAdapter(){

    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new ChatViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        Log.d("ChatAdapter", position+"/"+chatList.size());
//        Log.d("ChatAdapter", chatList.get(position).getMessage());
        if(chatList.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setText(chatList.get(position).getMessage());
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        } else {
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mMessage.setText(chatList.get(position).getMessage());
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }
    }

    public void setChatList(List<ChatObject> chatList) {
        this.chatList.clear();
        this.chatList.addAll(chatList);
        this.notifyDataSetChanged();
        Log.d("ChatAdapter", "notify changed");
    }

    @Override
    public int getItemCount() {
        if (this.chatList == null) return 0;
        return this.chatList.size();
    }
}
