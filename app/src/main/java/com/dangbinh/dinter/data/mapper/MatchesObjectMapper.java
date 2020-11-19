package com.dangbinh.dinter.data.mapper;

import android.util.Log;

import com.dangbinh.dinter.matches.MatchObject;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchesObjectMapper extends FirebaseMapper<Map<String, String>, MatchObject> {
    @Override
    public MatchObject map(Map<String, String> map) {
        Log.d("MatchesObjectMapper", map.toString());
        MatchObject match = new MatchObject();
        match.setName(map.get("name"));
        match.setProfileImageUrl(map.get("profileImageUrl"));
        return match;
    }

    @Override
    public List<MatchObject> mapList(DataSnapshot dataSnapshot) {
        List<MatchObject> matches = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            MatchObject object = map(item);
            object.setUserId(item.getKey());
            matches.add(object);
        }
        return super.mapList(dataSnapshot);
    }
}
