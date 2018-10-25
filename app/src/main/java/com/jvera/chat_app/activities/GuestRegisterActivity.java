package com.jvera.chat_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.database_access.CredsValidationInterface;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuestRegisterActivity extends AppCompatActivity implements CredsValidationInterface {

    private static final String TAG = "Debug" ;
    @BindView(R.id.pseudo_guest) EditText pseudoGuest;
    String guestUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick(R.id.login_guest_btn)
    public void setOnClickGuestRegisterEvents(View v) {
        registerClickAction();
    }

    /** Checks basic username validity then calls DB to add */
    private void registerClickAction() {
        guestUsername = pseudoGuest.getText().toString();
        String invalidUsernameReason = Helper.checkUsernameValidity(guestUsername);

        if (!"".equals(invalidUsernameReason)) {
            pseudoGuest.setError(invalidUsernameReason);
        } else {
            Database.addCredentials(
                this,
                DbHelper.generateCallback(this), //damned trick
                Constants.API_URL_GUESTS_USERNAMES,
                guestUsername,
                null //no password for guests
            );
        }
    }

    /** To do list if credentials are validated*/
    @Override public void actionOnValidCredentials() {
        Helper.toastAnnounce(this, Constants.TXT_REGISTRATION_SUCCESSFUL_WELCOME + guestUsername + "!");
        Helper.activityStarter(this, GuestChatActivity.class);
    }
}
