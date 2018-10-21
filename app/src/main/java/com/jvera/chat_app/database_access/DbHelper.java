package com.jvera.chat_app.database_access;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.models.GuestDetails;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.models.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class DbHelper {
    private static final Random RAND = new Random();
    private static final int GUEST_PASSWORD_NBR = RAND.nextInt(100); //random between 0 and 100
    private static ProgressDialog progDial;

    public static Firebase generateFirebaseReference(String url) {
        return new Firebase(url);
    }

    /**
    * Returns an instance of CallbackWaiterInterface with implementation of onDataReceived using
    * the given actionOnValidCredentials through the interface of CredsValidationInterface
    */
    public static CallbackWaiterInterface generateCallback(final CredsValidationInterface action) {
        return new CallbackWaiterInterface() {
            @Override public void onDataReceived(boolean accessGranted) {
                if (accessGranted) {
                    action.actionOnValidCredentials();
                }
            }
        };
    }

    /**
    * add user password, generates fake password for users. pushes to DB
    */
    private static void setUserGuestPassword(Firebase reference, final String user,
                                             final String pass) {
        String password;
        if (pass == null) {
            //////Guest
            password = "G_" + user + "_" + GUEST_PASSWORD_NBR;
            reference.child(user).child("password").setValue(password); // Guests don't have profile
            GuestDetails.username = user;
        } else {
            //////User
            password = pass;
            reference.child(user).child("profile").child("password").setValue(password);
            UserDetails.username = user;
            UserDetails.password = password;
        }
    }

    /**
     * Generic response listener generator. Valid for user and guests' registration and login.
     */
    static Response.Listener<String> generateResponseListener(final Context context,
                                                              final CallbackWaiterInterface callback,
                                                              final String user, final String pass,
                                                              final String url, final boolean isAdding) {
        progDial = new ProgressDialog(context);
        progDial.setMessage("Loading...");
        progDial.show();

        return new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                boolean grantAccess = false;
                Firebase reference = new Firebase(url);

                if (s.equals("null")) { //if database is empty
                    grantAccess = actionOnEmptyDatabase(context, reference, user, pass, isAdding);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (!obj.has(user)) {
                            grantAccess = actionOnUserNotFound(context, reference, user, pass, isAdding);
                        } else {
                            grantAccess = actionOnExistingUser(context, obj, user, pass, isAdding);
                        }
                    } catch (JSONException e) {e.printStackTrace();}
                }
                callback.onDataReceived(grantAccess); //Damned trick
                progDial.dismiss();
            }
        };
    }

    /** To do things when the database is empty whether we are logging or registering */
    private static boolean actionOnEmptyDatabase(Context context, Firebase reference, String user,
                                                 String pass, boolean isAdding) {
        boolean isAddedCredentials = false;
        if(isAdding){
            setUserGuestPassword(reference, user, pass);
            isAddedCredentials = true;
        } else {
            Helper.toastAnnounce(context, Constants.TXT_ERROR_USER_NOT_FOUND);
        }
        return isAddedCredentials;
    }

    /** If user not found, same protocol than for empty database */
    private static boolean actionOnUserNotFound(Context context, Firebase reference, String user,
                                                String pass, boolean isAdding) {
        return actionOnEmptyDatabase(context, reference, user, pass, isAdding);
    }

    /** To do things if the user exists whether we are logging or registering */
    private static boolean actionOnExistingUser(Context context, JSONObject obj, String user,
                                                String pass, boolean isAdding) {
        boolean grantAccess = false;
        if (isAdding) {
            Helper.toastAnnounce(context, Constants.TXT_ERROR_USER_EXISTS);
        } else {
            try {
                JSONObject userProfile = obj.getJSONObject(user).getJSONObject("profile");
                if (userProfile.getString("password").equals(pass)) {
                    grantAccess = true;
                    UserDetails.username = user;
                    UserDetails.password = pass;
                    Helper.toastAnnounce(context, Constants.TXT_WELCOME_USER(user));
                } else {
                    Helper.toastAnnounce(context, Constants.TXT_ERROR_INCORRECT_PASSWORD);
                }
            } catch (JSONException e) {e.printStackTrace();}
        }
        return grantAccess;
    }

    /**
     * Add request to the queue as by protocol
     */
    static void addRequestQueue(Context context, String url,
                                Response.Listener<String> responseListener) {
        Response.ErrorListener errorListener = generateErrorListener();
        StringRequest request = new StringRequest(
            Request.Method.GET,
            url,
            responseListener,
            errorListener
        );
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    /** Generic error listener generator*/
    private static Response.ErrorListener generateErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                progDial.dismiss();
            }
        };
    }
}
