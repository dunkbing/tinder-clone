package com.dangbinh.dinter.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dangbinh.dinter.R;

import java.util.List;

/**
 * Created by dangbinh on 18/11/2020.
 */

public class CardItemAdapter extends ArrayAdapter<CardItem>{

    public CardItemAdapter(Context context, int resourceId, List<CardItem> items){
        super(context, resourceId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CardItem cardItem = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(cardItem.getName());
        if ("default".equals(cardItem.getProfileImageUrl())) {
            Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
        } else {
            Glide.with(image.getContext()).clear(image);
            Glide.with(convertView.getContext()).load(cardItem.getProfileImageUrl()).into(image);
        }

        return convertView;
    }
}
