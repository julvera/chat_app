package com.jvera.chat_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

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
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.FRANCE
        ).format(new Date());
        String imageFilename = "JPEG_" + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
            imageFilename,
            ".jpg",
            storageDir
        );
    }

    /** Validate permissions are given to the app to access something on the phone*/
    public static boolean isAccessPermissionGranted(final Context context, final Activity act,
                                                    final String requiredPermission) {
        final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (ContextCompat.checkSelfPermission(context, requiredPermission) != PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(act, requiredPermission)) {
                // Explain to the user why we need to read the contacts
                new AlertDialog.Builder(context)
                    .setTitle("title")
                    .setMessage("text")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                    act,
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
                        act,
                        new String[]{requiredPermission},
                        Constants.PERMISSIONS_REQUEST_LOCATION
                );
            }
            return false;
        }
        return true;
    }
}
