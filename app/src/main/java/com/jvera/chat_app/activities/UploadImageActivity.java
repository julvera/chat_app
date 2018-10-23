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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadImageActivity  extends AppCompatActivity {
    @BindView(R.id.imageView) ImageView imageView;
    private static final String TAG = "Debug" ;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_REQUEST = 2;
    private boolean imageSelected = false;
    String currentPhotoPath;
    String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        Log.i(TAG, "onCreate UploadImageActivity");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    /** Send messages!*/
    @OnClick(R.id.sendButton)
    public void onClickSendActions(View v) {
//        Database.sendMessages(messageArea, null, null);
    }

    /** Use camera*/
    @OnClick(R.id.cameraButton)
    public void onClickCameraActions(View v) {
        if(isAccessPermissionGranted(android.Manifest.permission.CAMERA)) {
            takePicturePreparator();
        }
    }

    @OnClick(R.id.galleryButton)
    public void onClickGalleryActions(View v) {
        if (isAccessPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            chooseImage();
        }
    }

    /** click on Gallery logic*/
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void takePicturePreparator() {
        Log.i("SOMETHING", "Take picture intent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                Log.i("SOMETHING", "Take picture intent VALIDATED");
                File photoFile = createImage();
                Uri photoURI = FileProvider.getUriForFile(
                    this,
                    "com.example.android.fileprovider",
                    photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                addToGallery();
                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
            } catch (IOException ex) {/* cry */}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) {return;}

        if(requestCode == PICK_IMAGE_REQUEST)
        {
            Uri filePath = data.getData();
            try {
                String[] filePathColumn = { MediaStore.Images.ImageColumns.DATA };
                Cursor cursor = getContentResolver().query(filePath, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Log.i("SOMETHING", "Image decodable string: " + imgDecodableString);
                Log.i("SOMETHING", "file path: " + filePath);


                imageSelected = true;
//////////////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//////////////                currentPhotoPath = getRealPathFromURI(filePath);
//////////////                //TODO: actually something
////////////////                imageView.setImageURI(uri);
////////////////                imageView.setImageBitmap(bitmap);
//////////////                imageSelected = true;
////////////////                OutputStream imageFile = new FileOutputStream(filePath.getPath());
////////////////                boolean worked = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageFile);
            } catch (Exception e) {e.printStackTrace();}
        } else if (requestCode == TAKE_PICTURE_REQUEST) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageSelected = true;
        }
    }

//    private String getRealPathFromURI(Uri uri) {
//        String realPath = "";
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//
//        if(cursor.moveToFirst()){
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            realPath = cursor.getString(columnIndex);
//        }
//        cursor.close();
//
//        return realPath;
//    }

    private File createImage() throws IOException {
        Log.i("SOMETHING", "create Image File");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFilename = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("SOMETHING", storageDir.toString());
        File image = File.createTempFile(
            imageFilename,
            ".jpg",
            storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        Log.i("SOMETHING", "ENDOF createImage() currentPhotoPath = " + currentPhotoPath);
        return image;
    }

    private void addToGallery() {
        Log.i("SOMETHING", "add to gallery photo path: " + currentPhotoPath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(currentPhotoPath));
        Log.i("SOMETHING", "add to gallery URI: " + contentUri);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /** Validate permissions are given to the app to access something on the phone*/
    private boolean isAccessPermissionGranted(final String requiredPermission) {
        final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (ContextCompat.checkSelfPermission(this, requiredPermission) != PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermission)) {
                // Explain to the user why we need to read the contacts
                new AlertDialog.Builder(this)
                    .setTitle("title")
                    .setMessage("text")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                    UploadImageActivity.this,
                                    new String[]{requiredPermission},
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
                    new String[]{requiredPermission},
                    Constants.PERMISSIONS_REQUEST_LOCATION
                );
            }
            return false;
        }
        return true;
    }
}
