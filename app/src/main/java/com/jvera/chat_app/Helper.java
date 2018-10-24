package com.jvera.chat_app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.jvera.chat_app.fragments.ProfilFragment;
import com.jvera.chat_app.fragments.UserListFragment;
import com.jvera.chat_app.models.UserDetails;


public class Helper {

    /** Api url generators*/
    public static String api_url_user_messages_friend () {
        return urlGeneratorMessagesSenderReceiver(UserDetails.username, UserDetails.chat_with);
    }
    public static String api_url_friend_messages_user () {
        return urlGeneratorMessagesSenderReceiver(UserDetails.chat_with, UserDetails.username);
    }
    private static String urlGeneratorMessagesSenderReceiver(String sender, String receiver) {
        return Constants.API_URL_USERS_USERNAMES + "/" + sender + "/messages/" + receiver;
    }

    /**
    * Start activities without calling `new Intent` everywhere
    */
    public static void activityStarter(Context context, Class newActivityClass) {
        context.startActivity(new Intent(context, newActivityClass));
    }

    /** Toast helper*/
    public static void toastAnnounce(Context context, final String error_msg){
        Toast.makeText(context, error_msg, Toast.LENGTH_LONG).show();
    }

    /** Check username constraints*/
    public static String checkUsernameValidity(final String username) {
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
    public static String checkPasswordValidity(final String password) {
        String errorMessage = "";
        if (password.equals("")) {
            errorMessage = Constants.TXT_ERROR_FIELD_REQUIRED;
        } else if (password.length() < 5) {
            errorMessage = Constants.TXT_ERROR_SHORT_PASSWORD;
        }
        return errorMessage;
    }

    /**
     * return new user list fragment
     */
    public static Fragment createUserListFragment() {
        return new UserListFragment();
    }

    /**
     * return new user list fragment
     */
    public static Fragment createProfileFragment() {
        return new ProfilFragment();
    }
}
