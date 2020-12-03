package com.dangbinh.dinter.matches;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dangbinh.dinter.data.mapper.MatchesObjectMapper;
import com.dangbinh.dinter.data.repository.FirebaseDatabaseRepository;
import com.dangbinh.dinter.data.repository.OnGetDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchViewModel extends ViewModel {
    private MutableLiveData<List<MatchObject>> matches;
    List<MatchObject> matchesObjects = new ArrayList<>();
    public LiveData<List<MatchObject>> getMatches() {
        MatchesRepository repository = new MatchesRepository();
        if (matches == null) {
            matches = new MutableLiveData<>();
            matches.setValue(matchesObjects);
            matches.postValue(matchesObjects);
            repository.getUserMatchId();
            Log.d("MatchesViewModel", matchesObjects.size()+"");
        }
        return matches;
    }

    public class MatchesRepository extends FirebaseDatabaseRepository<MatchObject> implements OnGetDataListener {

        public MatchesRepository() {
            super(new MatchesObjectMapper());
        }

        private void getUserMatchId() {
            DatabaseReference matchDb = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(getCurrentUId())
                    .child("connections")
                    .child("matches");
            Log.d("MatchesViewModel", matchDb.getKey() + " " + getCurrentUId());
            matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for(DataSnapshot match : dataSnapshot.getChildren()){
                            Log.d("MatchesViewModel", "matchId: "+match.getKey());
                            fetchMatchInformation(match.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

                OnGetDataListener listener = new OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }
                };
            });
        }

        private void fetchMatchInformation(String key) {
            databaseReference = databaseReference.child(key);
            Log.d("MatchesViewModel", databaseReference.getKey());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        MatchObject object = new MatchObject();
                        object.setUserId(key);
                        object.setName(snapshot.child("name").getValue().toString());
                        object.setProfileImageUrl(snapshot.child("profileImageUrl").getValue().toString());
                        matchesObjects.add(object);
                        matches.postValue(matchesObjects);
                        Log.d("MatchesViewModel", object.getUserId());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        protected String getRootNode() {
            return "Users";
        }

        @Override
        public void onSuccess(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFailure() {

        }
    }
}
