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


public class UserChat extends AppCompatActivity {
    private static final String TAG = "Debug" ;
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText message_area;
    @BindView(R.id.scrollView) ScrollView scroll_view;

    Firebase ref_user_friend, ref_friend_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        Log.i(TAG, "onCreate UserChat");
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        ref_user_friend = new Firebase(Helper.api_url_user_messages_friend());
        ref_friend_user = new Firebase(Helper.api_url_friend_messages_user());

        ref_user_friend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String username = map.get("user").toString();

                //TODO : Add timestamp for each message

                int type = Constants.message_type_self; //message from us
                if(!username.equals(UserDetails.username)){ //message from someone else
                    type = Constants.message_type_other;
                }
                Helper.add_message_box(UserChat.this, layout, scroll_view, message, type);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @OnClick(R.id.sendButton)
    public void onClick(View v) {
        String messageText = message_area.getText().toString();

        if(!messageText.equals("")){
            Map<String, String> map = new HashMap<>();
            map.put("message", messageText);
            map.put("user", UserDetails.username);
            ref_user_friend.push().setValue(map);
            ref_friend_user.push().setValue(map);
            message_area.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart UserChat");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume UserChat");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause UserChat");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop UserChat");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy UserChat");
    }
}
