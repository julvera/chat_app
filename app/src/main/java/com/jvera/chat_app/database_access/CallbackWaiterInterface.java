package com.jvera.chat_app.database_access;

/*
* Hack around the response listeners so the main thread waits on responses before going to the
* next step (action according to the response)
*/
public interface CallbackWaiterInterface {
    void onDataReceived(boolean something);
}
