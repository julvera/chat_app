package com.jvera.chat_app.database_access;

public class DbHelper {
    /*
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
}
