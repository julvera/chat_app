package com.jvera.chat_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.Firebase;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserChatActivity extends AppCompatActivity {
    private static final String TAG = "Debug" ;
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText messageArea;
    @BindView(R.id.scrollView) ScrollView scrollView;

    Firebase refUserFriend, refFriendUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        Log.i(TAG, "onCreate UserChatActivity");
        ButterKnife.bind(this);

        refFriendUser = DbHelper.generateFirebaseReference(Helper.api_url_friend_messages_user());
        refUserFriend = Database.referenceMessages(
            this,
            Helper.api_url_user_messages_friend(),
            layout,
            scrollView,
            true //user chat is private
        );
    }

    /** Send messages!*/
    @OnClick(R.id.sendButton)
    public void sendMessage(View v) {
        Database.sendMessages(messageArea, refUserFriend, refFriendUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart UserChatActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume UserChatActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause UserChatActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop UserChatActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy UserChatActivity");
    }
}
