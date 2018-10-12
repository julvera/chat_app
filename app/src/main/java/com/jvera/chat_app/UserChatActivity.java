package com.jvera.chat_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.Firebase;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserChatActivity extends AppCompatActivity {
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText messageArea;
    @BindView(R.id.scrollView) ScrollView scrollView;
    private static final String TAG = "Debug" ;
    private static final int PICK_IMAGE_REQUEST = 1;

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
    public void onClickSendActions(View v) {
        Database.sendMessages(messageArea, refUserFriend, refFriendUser);
    }

    /** Use camera*/
    @OnClick(R.id.cameraButton)
    public void onClickCameraActions(View v) {
        //TODO something?
    }

    /** browse gallery to send pictures*/
    @OnClick(R.id.galleryButton)
    public void onClickGalleryActions(View v) {
        chooseImage();
    }

    /** click on Gallery logic*/
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {return;}

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK)
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //TODO: actually something
//                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }
}
