package com.jvera.chat_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MessagesHelper {
    private TextView textView;
    private ImageView imageView;
    private LinearLayout.LayoutParams layoutParams;

    /**
     * Generates the message box for screen prompting of messages
     */
    public void addMessageBox(Context context, LinearLayout layout, final ScrollView scrollView,
                                     String message, int messageFrom, String messageType){
        textView = new TextView(context);
        imageView = new ImageView(context);
        setLayoutStarterParams(messageFrom);
        boolean isImage = messageType.equals(Constants.MESSAGE_TYPE_IMAGE);

        if (isImage) {
            setImageViewParams(message);
            layout.addView(imageView);
        } else {
            setTextViewParams(context, message, messageFrom);
            layout.addView(textView);
        }

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void setLayoutStarterParams (int messageFrom) {
        layoutParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.END;
        if (messageFrom == Constants.MESSAGE_FROM_OTHER) {
            layoutParams.gravity = Gravity.START;
        }
    }

    private void setImageViewParams (String message) {
        layoutParams.width = 250;
        layoutParams.height = 250;
        Bitmap decodedImage = DecodeImage(message);
        imageView.setImageBitmap(decodedImage);
        imageView.setLayoutParams(layoutParams);
    }

    private void setTextViewParams (Context context, String message, int messageFrom) {
        textView.setTextColor(context.getResources().getColor(R.color.colorBackgroundChat));
        textView.setBackgroundResource(R.drawable.bubble_right);

        if (messageFrom == Constants.MESSAGE_FROM_OTHER) {
            textView.setTextColor(context.getResources().getColor(R.color.black));
            textView.setBackgroundResource(R.drawable.bubble_left);
        }
        textView.setText(message);
        textView.setLayoutParams(layoutParams);
    }

    private Bitmap DecodeImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}

