package com.jvera.chat_app;

import android.app.ProgressDialog;
import android.content.Context;
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
    private static final Random r = new Random();
    private static final int guest_password_nbr = r.nextInt(100); //random between 0 and 100

    /*
    * api url generators
    */
    protected static String api_url_user_messages_friend () {
        return url_generator_messages_sender_receiver(UserDetails.username, UserDetails.chat_with);
    }
    protected static String api_url_friend_messages_user () {
        return url_generator_messages_sender_receiver(UserDetails.chat_with, UserDetails.username);
    }
    private static String url_generator_messages_sender_receiver (String sender, String receiver) {
        return Constants.api_url_users_usernames + "/" + sender + "/messages/" + receiver;
    }

    /*
    * Toast helper
    */
    protected static void toast_error(Context context, final String error_msg){
        Toast.makeText(context, error_msg, Toast.LENGTH_LONG).show();
    }

    /*
    * Add user or guest into DB
    */
    protected static StringRequest db_add_credentials(final Context context, final String base_url,
                                                      final String user, final String pass){
        final ProgressDialog prog_dial = new ProgressDialog(context);
        prog_dial.setMessage("Loading...");
        prog_dial.show();

        Response.Listener<String> response_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase(base_url);

                if (s.equals("null")) {
                    set_user_guest_password(reference, context, user, pass);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            set_user_guest_password(reference, context, user, pass);
                        } else {
                            Helper.toast_error(context, Constants.txt_error_user_exists);
                        }
                    } catch (JSONException e) {e.printStackTrace();}
                }
                prog_dial.dismiss();
            }
        };

        Response.ErrorListener error_listener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                prog_dial.dismiss();
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
    private static void set_user_guest_password(Firebase reference, Context context,
                                                final String user, final String pass) {
        String password;
        if (pass == null) {
            password = "G_" + user + "_" + guest_password_nbr;
        } else {password = pass;}
        reference.child(user).child("profile").child("password").setValue(password);
        Helper.toast_error(context, Constants.txt_registration_successful);
    }

    /*
    * check username constraints
    */
    protected static String check_username_validity(final String username) {
        String error_message = "";
        if (username.equals("")) {
            error_message = Constants.txt_error_field_required;
        } else if (!username.matches("[A-Za-z0-9]+")) {
            error_message = Constants.txt_error_alpha_or_number_only;
        } else if (username.length() < 5) {
            error_message = Constants.txt_error_short_username;
        }
        return error_message;
    }

    /*
     * check password constraints
     */
    protected static String check_password_validity(final String password) {
        String error_message = "";
        if (password.equals("")) {
            error_message = Constants.txt_error_field_required;
        } else if (password.length() < 5) {
            error_message = Constants.txt_error_short_password;
        }
        return error_message;
    }

    /*
    * Update latest received messages on screen
    */
    protected static void add_message_box(Context context, LinearLayout layout,
                                          final ScrollView scroll_view, String message, int type){
        TextView textView = new TextView(context);
        textView.setText(message);

        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_params.weight = 1.0f;

        if(type == Constants.message_type_self) {
            layout_params.gravity = Gravity.END;
            textView.setTextColor(context.getResources().getColor(R.color.colorBackgroundChat));
            textView.setBackgroundResource(R.drawable.bubble_right);
        }
        else{
            layout_params.gravity = Gravity.START;
            textView.setTextColor(context.getResources().getColor(R.color.black));
            textView.setBackgroundResource(R.drawable.bubble_left);
        }

        textView.setLayoutParams(layout_params);
        layout.addView(textView);

        scroll_view.post(new Runnable() {
            @Override
            public void run() {
                scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
