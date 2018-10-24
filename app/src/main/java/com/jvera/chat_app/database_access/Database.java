package com.jvera.chat_app.database_access;

import android.content.Context;
import android.net.Uri;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.ImageHelper;
import com.jvera.chat_app.MessagesHelper;
import com.jvera.chat_app.models.GuestDetails;
import com.jvera.chat_app.models.UserDetails;

import java.util.HashMap;
import java.util.Map;


public class Database {

    /**
     * Checks if the given credentials references an existing user
     */
    public static void verifyUserCredentials(final Context context, final CallbackWaiterInterface callback,
                                             final String user, final String pass) {
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
        Firebase refGuestsMessages = DbHelper.generateFirebaseReference(url);
        final MessagesHelper messagesHelper = new MessagesHelper();

        refGuestsMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get(Constants.MESSAGES_CATEGORY_MESSAGE).toString();
                String usernameMessaging = map.get(Constants.MESSAGES_CATEGORY_USER).toString();
                String messageType = map.get(Constants.MESSAGES_CATEGORY_TYPE).toString();

                //TODO : Add timestamp for each message

                String currentUsername = GuestDetails.username; // Assume guest
                if (isPrivateConversation){                     // We are a user
                    currentUsername = UserDetails.username;     // Take username
                }

                int messageFrom = Constants.MESSAGE_FROM_SELF;          // Message from us
                if(!usernameMessaging.equals(currentUsername)){         // Message from someone else
                    messageFrom = Constants.MESSAGE_FROM_OTHER;
                    if (!isPrivateConversation) {                       // Message on guests chat
                        message = usernameMessaging + ": " + message;
                    }
                }

                messagesHelper.addMessageBox(context, layout, scrollView, message, messageFrom, messageType);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(FirebaseError firebaseError) {}
        });
        return refGuestsMessages;
    }

    public static void sendMessage(final EditText messageArea, final Firebase ref1,
                                   final Firebase ref2) {
        String messageText = messageArea.getText().toString();
        pushItemToDatabase(messageText, ref1, ref2, null);
        messageArea.setText("");
    }

    public static boolean sendImage(Context c, Uri photoPath){
        Firebase dbRef1 = DbHelper.generateFirebaseReference(Helper.api_url_friend_messages_user());
        Firebase dbRef2 = DbHelper.generateFirebaseReference(Helper.api_url_user_messages_friend());

        try{
            String encodedImage = ImageHelper.getEncodedImage(c, photoPath);
            pushItemToDatabase("Take this pic!", dbRef1, dbRef2, encodedImage);
        }catch (Exception e){return false;}

        return true;
    }

    private static void pushItemToDatabase(String message, Firebase ref1,
                                           Firebase ref2, String encodedImage) {
        boolean isGuestChat = ref2 == null; //ref2 null => Guest
        boolean isImage = encodedImage != null; //encodedImage null => not an image

        if(!message.equals("")){
            String type = Constants.MESSAGE_TYPE_TEXT;
            if (isImage) {
                type = Constants.MESSAGE_TYPE_IMAGE;
                message = encodedImage;
            }

            Map<String, String> map = new HashMap<>();
            map.put(Constants.MESSAGES_CATEGORY_MESSAGE, message);
            map.put(Constants.MESSAGES_CATEGORY_TYPE, type);

            if(isGuestChat) {
                map.put(Constants.MESSAGES_CATEGORY_USER, GuestDetails.username);
            } else {
                map.put(Constants.MESSAGES_CATEGORY_USER, UserDetails.username);
                ref2.push().setValue(map);
            }
            ref1.push().setValue(map);
        }
    }
}
