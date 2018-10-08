package com.jvera.chat_app.database_access;

/*
* Needed so actionOnValidCredentials can be implemented in subclasses to handle the
* different actions if credentials are valid (mostly redirection) in every specific case
*/
public interface CredsValidationInterface {
    void actionOnValidCredentials(); //To be implemented
}
