package com.jvera.chat_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ImageHelper {
    /** Creates a new intent `get content` on type `image/` */
    public static Intent generateGalleryIntent () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return Intent.createChooser(intent, "Select Picture");
    }

    /** Generate base64 encoded image */
    public static String getEncodedImage(Context c, Uri imageUri) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver(), imageUri);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteFormat = stream.toByteArray();

        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    /** Create temp image file */
    public static File createImage(Context context) throws IOException {
        Log.i("SOMETHING", "create Image File");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFilename = "JPEG_" + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("SOMETHING", storageDir.toString());
        File image = File.createTempFile(
            imageFilename,
            ".jpg",
            storageDir
        );

        Log.i("SOMETHING", "ENDOF createImage() currentPhotoPath = " + image.getAbsolutePath());
        return image;
    }
}
