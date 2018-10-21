package com.jvera.chat_app.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.Firebase;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserChatActivity extends AppCompatActivity {
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.messageArea) EditText messageArea;
    @BindView(R.id.scrollView) ScrollView scrollView;
    private static final String TAG = "Debug" ;
    private static final int PICK_IMAGE_REQUEST = 1;
    String currentPhotoPath;
    private boolean imageSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        Log.i(TAG, "onCreate UserChatActivity");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    /** Send messages!*/
    @OnClick(R.id.sendButton)
    public void onClickSendActions(View v) {
        if(messageArea.getText().toString().equals("")){return;}

        Firebase refFriendUser = DbHelper.generateFirebaseReference(
            Helper.api_url_friend_messages_user()
        );
        Firebase refUserFriend = Database.referenceMessages(
            this,
            Helper.api_url_user_messages_friend(),
            layout,
            scrollView,
            true //user chat is private
        );

        if (imageSelected) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            byte[] byteFormat = stream.toByteArray();

            String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            Database.sendMessages(messageArea, refUserFriend, refFriendUser, encodedImage);

        } else {
            Database.sendMessages(messageArea, refUserFriend, refFriendUser, null);
        }
    }

    /** Use camera*/
    @OnClick(R.id.cameraButton)
    public void onClickCameraActions(View v) {
        //TODO something?
    }

    /** browse gallery to send pictures*/
    @OnClick(R.id.galleryButton)
    public void onClickGalleryActions(View v) {
        if (isExternalStorageAccessible()) {
            chooseImage();
        }
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
        String currentPhotoPath;
        if (data == null || resultCode != RESULT_OK) {return;}

        if(requestCode == PICK_IMAGE_REQUEST)
        {
            Uri filePath = data.getData();
            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                currentPhotoPath = getRealPathFromURI(filePath);
                //TODO: actually something
//                imageView.setImageURI(uri);
//                imageView.setImageBitmap(bitmap);
                imageSelected = true;
//                OutputStream imageFile = new FileOutputStream(filePath.getPath());
//                boolean worked = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageFile);
            } finally {}
//            catch (IOException e) {e.printStackTrace();}
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String realPath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);

        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            realPath = cursor.getString(columnIndex);
        }
        cursor.close();

        return realPath;
    }

    private boolean isExternalStorageAccessible() {
        final String READ_EXT_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (ContextCompat.checkSelfPermission(this, READ_EXT_STORAGE) != PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXT_STORAGE)) {
                // Explain to the user why we need to read the contacts
                new AlertDialog.Builder(this)
                    .setTitle("title")
                    .setMessage("text")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                UserChatActivity.this,
                                new String[]{READ_EXT_STORAGE},
                                1
                            );
                        }
                    })
                    .create()
                    .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    new String[]{READ_EXT_STORAGE},
                    Constants.PERMISSIONS_REQUEST_LOCATION
                );
            }
            return false;
        }
        return true;
    }
}
