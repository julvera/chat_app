package com.jvera.chat_app;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class Helper {

    /** Api url generators*/
    static String api_url_user_messages_friend () {
        return urlGeneratorMessagesSenderReceiver(UserDetails.username, UserDetails.chat_with);
    }
    static String api_url_friend_messages_user () {
        return urlGeneratorMessagesSenderReceiver(UserDetails.chat_with, UserDetails.username);
    }
    private static String urlGeneratorMessagesSenderReceiver(String sender, String receiver) {
        return Constants.API_URL_USERS_USERNAMES + "/" + sender + "/messages/" + receiver;
    }

    /**
    * Start activities without calling `new Intent` everywhere
    */
    static void activityStarter(Context context, Class newActivityClass) {
        context.startActivity(new Intent(context, newActivityClass));
    }

    /** Toast helper*/
    public static void toastAnnounce(Context context, final String error_msg){
        Toast.makeText(context, error_msg, Toast.LENGTH_LONG).show();
    }

    /** Check username constraints*/
    static String checkUsernameValidity(final String username) {
        String errorMessage = "";
        if (username.equals("")) {
            errorMessage = Constants.TXT_ERROR_FIELD_REQUIRED;
        } else if (!username.matches("[A-Za-z0-9]+")) {
            errorMessage = Constants.TXT_ERROR_ALPHA_OR_NUMBER_ONLY;
        } else if (username.length() < 5) {
            errorMessage = Constants.TXT_ERROR_SHORT_USERNAME;
        }
        return errorMessage;
    }

    /** Check password constraints*/
    static String checkPasswordValidity(final String password) {
        String errorMessage = "";
        if (password.equals("")) {
            errorMessage = Constants.TXT_ERROR_FIELD_REQUIRED;
        } else if (password.length() < 5) {
            errorMessage = Constants.TXT_ERROR_SHORT_PASSWORD;
        }
        return errorMessage;
    }

    /**
    * Generates the message box for screen prompting of messages
    */
    public static void addMessageBox(Context context, LinearLayout layout,
                                     final ScrollView scrollView, String message, int type){
        TextView textView = new TextView(context);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;

        if(type == Constants.MESSAGE_TYPE_SELF) {
            layoutParams.gravity = Gravity.END;
            textView.setTextColor(context.getResources().getColor(R.color.colorBackgroundChat));
            textView.setBackgroundResource(R.drawable.bubble_right);
        }
        else{
            layoutParams.gravity = Gravity.START;
            textView.setTextColor(context.getResources().getColor(R.color.black));
            textView.setBackgroundResource(R.drawable.bubble_left);
        }

        textView.setLayoutParams(layoutParams);
        layout.addView(textView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
