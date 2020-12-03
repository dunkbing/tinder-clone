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

import java.util.Calendar;
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
        String[] profiles = cardItem.getProfile().split("\n");
        String bornStr = profiles[0];
        String joined = profiles[2];
        TextView name = convertView.findViewById(R.id.name);
        TextView measure = convertView.findViewById(R.id.text_view_measure);
        TextView textViewJoined = convertView.findViewById(R.id.text_view_joined);
        ImageView image = convertView.findViewById(R.id.image);
        int born = 0;
        try {
            born = Integer.parseInt(bornStr.substring(bornStr.length() - 4));
            name.setText(String.format("%s(%d)", cardItem.getName(), Calendar.getInstance().get(Calendar.YEAR)-born));
        } catch (Exception e) {
            name.setText(String.format("%s", cardItem.getName()));
        }
        measure.setText(profiles[1].replace("Measurements: ", ""));
        textViewJoined.setText(String.format("Joined: %s", joined.substring(joined.length() - 4)));
        if ("default".equals(cardItem.getProfileImageUrl())) {
            Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
        } else {
            Glide.with(image.getContext()).clear(image);
            Glide.with(convertView.getContext()).load(cardItem.getProfileImageUrl()).into(image);
        }

        return convertView;
    }
}
