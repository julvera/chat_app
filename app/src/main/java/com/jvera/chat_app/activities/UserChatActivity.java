package com.jvera.chat_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.Firebase;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserChatActivity extends AppCompatActivity {
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText messageArea;
    @BindView(R.id.scrollView) ScrollView scrollView;

    Firebase refUserFriend, refFriendUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);

        //Need to create references here to have the messages history on page load
        refFriendUser = DbHelper.generateFirebaseReference(
                Helper.api_url_friend_messages_user()
        );
        refUserFriend = Database.referenceMessages(
            this.getApplicationContext(),
            Helper.api_url_user_messages_friend(),
            layout,
            scrollView,
            true //user chat is private
        );
    }

    /** Send messages!*/
    @OnClick(R.id.sendButton)
    public void onClickSendActions(View v) {
        Database.sendMessage(messageArea, refUserFriend, refFriendUser);
    }

    @OnClick(R.id.uploadButton)
    public void onClickUploadActions(View v) {
        Helper.activityStarter(this, UploadImageActivity.class);
    }
}
