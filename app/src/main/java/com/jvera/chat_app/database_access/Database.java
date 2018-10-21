package com.jvera.chat_app.database_access;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.GuestDetails;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.UserDetails;

import java.util.HashMap;
import java.util.Map;


public class Database {

    /**
     * Checks if the given credentials references an existing user
     */
    public static void verifyUserCredentials(final Context context, final CallbackWaiterInterface callback,
                                             final String user, final String pass) {
        Firebase.setAndroidContext(context);
        Response.Listener<String> responseListener = DbHelper.generateResponseListener(
            context, callback, user, pass, Constants.API_BASE_URL, false //not adding, just checking
        );

        DbHelper.addRequestQueue(
            context,
            Constants.API_URL_USERS_USERNAMES_JSON,
            responseListener
        );
    }

    /**
     * Add user OR guest into DB
     */
    public static void addCredentials(final Context context, final CallbackWaiterInterface callback,
                                      final String base_url, final String user, final String pass){
        Firebase.setAndroidContext(context);
        Response.Listener<String> responseListener = DbHelper.generateResponseListener(
            context, callback, user, pass, base_url, true //adding credentials. User or guest
        );

        DbHelper.addRequestQueue(
            context,
            base_url + ".json",
            responseListener
        );
    }

    /**
     * Generates a Firebase reference with an associated listener watching for new messages (both
     * incoming and outgoing)
     *
     * @param isPrivateConversation: boolean determining if we are in guest or user chat
     * @return a Firebase reference
     */
    public static Firebase referenceMessages(final Context context, final String url,
                                             final LinearLayout layout, final ScrollView scrollView,
                                             final boolean isPrivateConversation) {
        Firebase.setAndroidContext(context);
        Firebase refGuestsMessages = new Firebase(url);

        refGuestsMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String usernameMessaging = map.get("user").toString();

                //TODO : Add timestamp for each message

                String currentUsername = GuestDetails.username; // Assume guest
                if (isPrivateConversation){                     // We are a user
                    currentUsername = UserDetails.username;     // Take username
                }

                int type = Constants.MESSAGE_TYPE_SELF;         // Message from us
                if(!usernameMessaging.equals(currentUsername)){ // Message from someone else
                    type = Constants.MESSAGE_TYPE_OTHER;
                    if (!isPrivateConversation) {               // Message on guests chat
                        message = usernameMessaging + ": " + message;
                    }
                }
                Helper.addMessageBox(context, layout, scrollView, message, type);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(FirebaseError firebaseError) {}
        });
        return refGuestsMessages;
    }

    public static void sendMessages(final EditText messageArea, final Firebase ref1,
                                    final Firebase ref2, String encodedImage) {
        String messageText = messageArea.getText().toString();
        boolean isGuestChat = ref2 == null; //ref2 null => Guest
        Log.i(Database.class.getSimpleName(), "fait chier : !!!!!!!!!!!!: " + encodedImage);
        if(!messageText.equals("")){
            Map<String, String> map = new HashMap<>();
            map.put("message", messageText);
            if(isGuestChat) {
                map.put("user", GuestDetails.username);
            } else {
                map.put("user", UserDetails.username);
                ref2.push().setValue(map);
            }
            ref1.push().setValue(map);
            messageArea.setText("");
        }
    }
}
