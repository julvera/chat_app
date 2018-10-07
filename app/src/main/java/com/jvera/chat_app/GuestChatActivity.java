package com.jvera.chat_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuestChatActivity extends AppCompatActivity {
    private static final String TAG = "Debug" ;
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText messageArea;
    @BindView(R.id.scrollView) ScrollView scrollView;

    Firebase refGuestsMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_chat);
        Log.i(TAG, "onCreate GuestChatActivity");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
        refGuestsMessages = new Firebase(Constants.API_URL_GUESTS_MESSAGES);

        refGuestsMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String username = map.get("user").toString();

                //TODO : Add timestamp for each message

                int type = Constants.MESSAGE_TYPE_SELF; //message from us
                if(!username.equals(GuestDetails.username)){ //message from someone else
                    type = Constants.MESSAGE_TYPE_OTHER;
                    message = username + ": " + message;
                }
                Helper.addMessageBox(GuestChatActivity.this, layout, scrollView, message, type);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @OnClick(R.id.sendButton)
    public void onClick(View v) {
        String messageText = messageArea.getText().toString();

        if(!messageText.equals("")){
            Map<String, String> map = new HashMap<>();
            map.put("message", messageText);
            map.put("user", GuestDetails.username);
            refGuestsMessages.push().setValue(map);
            messageArea.setText("");
        }
    }
}