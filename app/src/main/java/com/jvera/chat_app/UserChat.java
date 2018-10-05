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
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        ref_user_friend = new Firebase(Constants.api_url_messages + UserDetails.username + "_" + UserDetails.chat_with);
        ref_friend_user = new Firebase(Constants.api_url_messages + UserDetails.chat_with + "_" + UserDetails.username);

        ref_user_friend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.chat_with + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
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

        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layout_params.weight = 1.0f;

        if(type == 1) {
            layout_params.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            layout_params.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(layout_params);
        layout.addView(textView);
        scroll_view.fullScroll(View.FOCUS_DOWN);
    }
}
