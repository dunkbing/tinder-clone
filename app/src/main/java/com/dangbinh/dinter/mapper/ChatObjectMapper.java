package com.dangbinh.dinter.mapper;

import android.util.Log;

import com.dangbinh.dinter.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatObjectMapper extends FirebaseMapper<Map<String, String>, ChatObject> {
    @Override
    public ChatObject map(Map<String, String> map) {
        ChatObject chatObject = new ChatObject();
        chatObject.setCurrentUser(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(map.get("createdByUser")));
        chatObject.setMessage(map.get("text"));
        Log.d("ChatObjectMapper", map.get("text"));
        return chatObject;
    }

    @Override
    public List<ChatObject> mapList(DataSnapshot dataSnapshot) {
        List<ChatObject> list = new ArrayList<>();
        Log.d("ChatObjectMapper", dataSnapshot.getKey());
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            Log.d("ChatObjectMapper", item.getValue().toString());
            list.add(map(item));
        }
        return list;
    }
}
