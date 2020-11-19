package com.dangbinh.dinter.Chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dangbinh.dinter.mapper.ChatObjectMapper;
import com.dangbinh.dinter.repository.FirebaseDatabaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatObjectViewModel extends ViewModel {
    private MutableLiveData<List<ChatObject>> chatObjects;
    private ChatObjectRepository repository;
    public LiveData<List<ChatObject>> getChatObjects(String matchId) {
        if (chatObjects == null) {
            chatObjects = new MutableLiveData<>();
            repository = new ChatObjectRepository(matchId);
        }
        return chatObjects;
    }



    private class ChatObjectRepository extends FirebaseDatabaseRepository<ChatObject> {

        private String chatId;
        public ChatObjectRepository(String matchId) {
            super(new ChatObjectMapper());
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("connections")
                    .child("matches")
                    .child(matchId)
                    .child("ChatId")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            chatId = snapshot.getValue().toString();
                            Log.d("ChatObjectViewModel", chatId);
                            databaseReference = databaseReference.child(chatId);
                            Log.d("ChatObjectViewModel", databaseReference.getKey());
                            loadMessages();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        private void loadMessages() {
            this.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<ChatObject>() {
                @Override
                public void onSuccess(List<ChatObject> result) {
                    chatObjects.setValue(result);
                }

                @Override
                public void onError(Exception e) {
                    chatObjects.setValue(null);
                }
            });
        }

        @Override
        protected String getRootNode() {
            return "Chat";
        }
    }
}
