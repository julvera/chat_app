package com.jvera.chat_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
public class helper {
    private static final Random r = new Random();
    private static final int guest_password_nbr = r.nextInt(100); //random between 0 and 100

    protected static void toast_error(Context context, final String error_msg){
        Toast.makeText(
            context,
            error_msg,
            Toast.LENGTH_LONG
        ).show();
    }

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
                    set_user_guest_password(reference, user, pass);
                    helper.toast_error(context, constants.txt_registration_successful);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            set_user_guest_password(reference, user, pass);
                            helper.toast_error(context, constants.txt_registration_successful);
                            // if registration successful, go back to Login Page
                            context.startActivity(new Intent(context, Login.class));

                        } else {
                            helper.toast_error(context, constants.txt_error_user_exists);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private static void set_user_guest_password(Firebase reference, final String user,
                                                  final String pass) {
        String password;
        if (pass == null) {
            password = "G_" + user + "_" + guest_password_nbr;
        } else {password = pass;}
        reference.child(user).child("password").setValue(password);
    }

    protected static String check_username_validity(final String username) {
        String error_message = "";
        if (username.equals("")) {
            error_message = constants.txt_error_field_required;
        } else if (!username.matches("[A-Za-z0-9]+")) {
            error_message = constants.txt_error_alpha_or_number_only;
        } else if (username.length() < 5) {
            error_message = constants.txt_error_short_username;
        }
        return error_message;
    }

    protected static String check_password_validity(final String password) {
        String error_message = "";
        if (password.equals("")) {
            error_message = constants.txt_error_field_required;
        } else if (password.length() < 5) {
            error_message = constants.txt_error_short_password;
        }
        return error_message;
    }
}
