package com.jvera.chat_app.database_access;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;


public class Database {
    private static ProgressDialog progDial;

    public static void isValidCredentials(final Context context, final String user,
                                          final String pass, final CallbackWaiterInterface callback) {
        progDial = new ProgressDialog(context);
        progDial.setMessage("Loading...");
        progDial.show();

        Response.Listener<String> response_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                boolean grantAccess = false;
                if (s.equals("null")) {
                    Helper.toastError(context, Constants.TXT_ERROR_USER_NOT_FOUND);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (!obj.has(user)) {
                            Helper.toastError(context, Constants.TXT_ERROR_USER_NOT_FOUND);
                        } else if (obj.getJSONObject(user).getJSONObject("profile").getString("password").equals(pass)) {
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            grantAccess = true;
                        } else {
                            Helper.toastError(context, Constants.TXT_ERROR_INCORRECT_PASSWORD);
                        }
                        callback.onDataReceived(grantAccess); //Damned trick
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

        StringRequest request = new StringRequest(
            Request.Method.GET,
            Constants.API_URL_USERS_USERNAMES_JSON,
            response_listener,
            error_listener
        );
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
