package com.jvera.chat_app;


@SuppressWarnings("WeakerAccess")
public class constants {
    /*
    * Text strings for toasts pop-ups mainly
    */
    protected static String txt_registration_successful = "Registration successful";
    protected static String txt_error_user_exists = "username already exists";
    protected static String txt_error_field_required = "This field is required";
    protected static String txt_error_alpha_or_number_only = "only alphabet or number allowed";
    protected static String txt_error_short_password = "This password is too short";
    protected static String txt_error_short_username = "This username is too short";
    protected static String txt_error_user_not_found = "user not found";
    protected static String txt_error_incorrect_password = "This password is incorrect";

    /*
     * String constants
     */
    protected static String api_url_users_json = "https://chat-app-f7685.firebaseio.com//users.json";
    protected static String api_url_users = "https://chat-app-f7685.firebaseio.com//users";

    protected static String api_url_guests = "https://chat-app-f7685.firebaseio.com//guests";
    protected static String api_url_guests_json = "https://chat-app-f7685.firebaseio.com//guests.json";

    protected static String api_url_messages ="https://chat-app-f7685.firebaseio.com/messages/";

}
