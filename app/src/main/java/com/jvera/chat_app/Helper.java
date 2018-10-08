package com.jvera.chat_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


@SuppressWarnings("WeakerAccess")
public class Helper {
    private static final Random RAND = new Random();
    private static final int GUEST_PASSWORD_NBR = RAND.nextInt(100); //random between 0 and 100

    /*
    * api url generators
    */
    protected static String api_url_user_messages_friend () {
        return urlGeneratorMessagesSenderReceiver(UserDetails.username, UserDetails.chat_with);
    }
    protected static String api_url_friend_messages_user () {
        return urlGeneratorMessagesSenderReceiver(UserDetails.chat_with, UserDetails.username);
    }
    private static String urlGeneratorMessagesSenderReceiver(String sender, String receiver) {
        return Constants.API_URL_USERS_USERNAMES + "/" + sender + "/messages/" + receiver;
    }

    /*
    * Start activities without calling `new Intent` everywhere
    */
    public static void activityStarter(Context context, Class newActivityClass) {
        context.startActivity(new Intent(context, newActivityClass));
    }

    /*
    * Toast helper
    */
    public static void toastError(Context context, final String error_msg){
        Toast.makeText(context, error_msg, Toast.LENGTH_LONG).show();
    }

    /*
    * Add user or guest into DB
    */
    protected static StringRequest dbAddCredentials(final Context context, final String base_url,
                                                    final String user, final String pass){
        final ProgressDialog progDial = new ProgressDialog(context);
        progDial.setMessage("Loading...");
        progDial.show();

        Response.Listener<String> response_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase(base_url);

                if (s.equals("null")) {
                    setUserGuestPassword(reference, context, user, pass);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            setUserGuestPassword(reference, context, user, pass);
                        } else {
                            Helper.toastError(context, Constants.TXT_ERROR_USER_EXISTS);
                        }
                    } catch (JSONException e) {e.printStackTrace();}
                }
                progDial.dismiss();
            }
        };

        Response.ErrorListener error_listener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                progDial.dismiss();
            }
        };

        return new StringRequest(
            Request.Method.GET,
            base_url + ".json",
            response_listener,
            error_listener
        );
    }

    /*
    * add user password, generates fake password for users. pushes to DB
    */
    private static void setUserGuestPassword(Firebase reference, Context context,
                                             final String user, final String pass) {
        String password;
        if (pass == null) {
            password = "G_" + user + "_" + GUEST_PASSWORD_NBR;
        } else {password = pass;}
        reference.child(user).child("profile").child("password").setValue(password);
        Helper.toastError(context, Constants.TXT_REGISTRATION_SUCCESSFUL);
    }

    /*
    * check username constraints
    */
    protected static String checkUsernameValidity(final String username) {
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

    /*
     * check password constraints
     */
    protected static String checkPasswordValidity(final String password) {
        String errorMessage = "";
        if (password.equals("")) {
            errorMessage = Constants.TXT_ERROR_FIELD_REQUIRED;
        } else if (password.length() < 5) {
            errorMessage = Constants.TXT_ERROR_SHORT_PASSWORD;
        }
        return errorMessage;
    }

    /*
    * Update latest received messages on screen
    */
    protected static void addMessageBox(Context context, LinearLayout layout,
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
