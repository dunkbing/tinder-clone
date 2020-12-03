package com.dangbinh.dinter.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dangbinh.dinter.R;

public class ChatActivity extends AppCompatActivity {
    private ChatAdapter mChatAdapter;

    private EditText mSendEditText;
    private NestedScrollView scrollView;

    ChatObjectViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        scrollView = findViewById(R.id.nested_scroll_view);

        String matchId = getIntent().getExtras().getString("matchId");
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(ChatObjectViewModel.class);
        mChatAdapter = new ChatAdapter();

        viewModel.getChatObjects(matchId).observe(this, chatObjects -> {
            Log.d("ChatActivity", chatObjects.size()+"");
            mChatAdapter.setChatList(chatObjects);
        });

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.edit_text_message);
        Button mSendButton = findViewById(R.id.button_send);

        mSendButton.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();
        viewModel.sendMessage(sendMessageText);
        mSendEditText.setText(null);
        scrollView.smoothScrollTo(0, scrollView.getChildAt(0).getHeight());
    }

}
