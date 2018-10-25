package com.jvera.chat_app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.ImageHelper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.database_access.Database;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UploadImageActivity  extends AppCompatActivity {

    @BindView(R.id.imageView) ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_REQUEST = 2;
    private boolean imageSelected = false;
    Uri currentPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    /** Send messages!*/
    @OnClick(R.id.sendButton)
    public void onClickSendActions(View v) {
        if (imageSelected) {
            boolean isImageSent = Database.sendImage(this, currentPhotoUri);
            if (isImageSent) {
                Helper.toastAnnounce(this, "Image sent!");
            } else { Helper.toastAnnounce(this, "Something went wrong :/"); }
            finish(); //End of activity, go back to messaging
        } else {
            Helper.toastAnnounce(this, "Nothing selected");
        }
    }

    /** Use camera*/
    @OnClick(R.id.cameraButton)
    public void onClickCameraActions(View v) {
        if(isAccessGranted(android.Manifest.permission.CAMERA)) {
            takePicture();
        }
    }

    /** Select image from Gallery*/
    @OnClick(R.id.galleryButton)
    public void onClickGalleryActions(View v) {
        if (isAccessGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            chooseImage();
        }
    }

    /** verify access authorisations*/
    private boolean isAccessGranted (String permission) {
        return ImageHelper.isAccessPermissionGranted(this, (Activity) this, permission);
    }

    /** click on Gallery logic*/
    private void chooseImage() {
        Intent intent = ImageHelper.generateGalleryIntent();
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = ImageHelper.createImage(this);
            } catch (IOException ex) {ex.printStackTrace(); /* cry */}

            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(
                    this, "com.example.android.fileprovider", photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);

                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
            }
        }
    }

    /** Action when*/
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) {return;}

        if(requestCode == PICK_IMAGE_REQUEST)
        {
            currentPhotoUri = data.getData();
        }
        imageView.setImageURI(currentPhotoUri);
        imageSelected = true;
    }
}
