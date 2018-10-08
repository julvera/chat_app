package com.jvera.chat_app;


@SuppressWarnings("WeakerAccess")
public class Constants {
    /*
    * Text strings for toasts pop-ups mainly
    */
    protected static String TXT_REGISTRATION_SUCCESSFUL = "Registration successful";
    protected static String TXT_ERROR_USER_EXISTS = "username already exists";
    protected static String TXT_ERROR_FIELD_REQUIRED = "This field is required";
    protected static String TXT_ERROR_ALPHA_OR_NUMBER_ONLY = "only alphabet or number allowed";
    protected static String TXT_ERROR_SHORT_PASSWORD = "This password is too short";
    protected static String TXT_ERROR_SHORT_USERNAME = "This username is too short";
    public static String TXT_ERROR_USER_NOT_FOUND = "user not found";
    public static String TXT_ERROR_INCORRECT_PASSWORD = "This password is incorrect";

    /*
     * api urls string constants
     */
    protected static String API_URL_USERS_USERNAMES = "https://chat-app-f7685.firebaseio.com/users/usernames";
    public static String API_URL_USERS_USERNAMES_JSON = "https://chat-app-f7685.firebaseio.com/users/usernames.json";
    protected static String API_URL_GUESTS_USERNAMES = "https://chat-app-f7685.firebaseio.com/guests/usernames";
    protected static String API_URL_GUESTS_MESSAGES = "https://chat-app-f7685.firebaseio.com/guests/messages";

    /*
    * Message type Constants
    */
    protected static int MESSAGE_TYPE_SELF = 1;
    protected static int MESSAGE_TYPE_OTHER = 2;
}
