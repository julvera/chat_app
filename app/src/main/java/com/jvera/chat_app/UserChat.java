package com.jvera.chat_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText message_area;
    @BindView(R.id.scrollView) ScrollView scroll_view;

    Firebase ref_user_friend, ref_friend_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        ref_user_friend = new Firebase(Helper.api_url_user_messages_friend());
        ref_friend_user = new Firebase(Helper.api_url_friend_messages_user());

        ref_user_friend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                //TODO : Add timestamp for each message

                if(userName.equals(UserDetails.username)){
                    addMessageBox( message, 1);
                }
                else{
                    // addMessageBox(UserDetails.chatWith + ": " + message, 2);
                    addMessageBox(message, 2);
                }
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

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(UserChat.this);
        textView.setText(message);

        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_params.weight = 1.0f;

        if(type == 1) {
            layout_params.gravity = Gravity.START;
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setBackgroundResource(R.drawable.bubble_left);
        }
        else{
            layout_params.gravity = Gravity.END;
            textView.setTextColor(getResources().getColor(R.color.colorBackgroundChat));
            textView.setBackgroundResource(R.drawable.bubble_right);
        }

        textView.setLayoutParams(layout_params);
        layout.addView(textView);

        scroll_view.post(new Runnable() {
            @Override
            public void run() {
                scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
