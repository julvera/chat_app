package com.jvera.chat_app;

import android.content.Context;
import android.widget.Toast;

@SuppressWarnings("WeakerAccess")
public class helper {
    protected static void toast_error(Context context, final String error_msg){
        Toast.makeText(
            context,
            error_msg,
            Toast.LENGTH_LONG
        ).show();
    }
}
