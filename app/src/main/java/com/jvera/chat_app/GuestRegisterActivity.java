package com.jvera.chat_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuestRegisterActivity extends AppCompatActivity {

    private static final String TAG = "Debug" ;
    @BindView(R.id.pseudo_guest) EditText pseudoGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);
        Log.i(TAG, "onCreate GuestRegisterActivity");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.login_guest_btn})
    public void setOnClickGuestRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_guest_btn:
                if (registerClickAction()) {
                    Helper.activityStarter(this, GuestChatActivity.class);
                }
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private boolean registerClickAction() {
        boolean ret = false;
        String guestUsername = pseudoGuest.getText().toString();
        String invalidUsernameReason = Helper.checkUsernameValidity(guestUsername);

        if (!"".equals(invalidUsernameReason)) {
            pseudoGuest.setError(invalidUsernameReason);
        } else {
            GuestDetails.username = guestUsername;
            StringRequest request = Helper.dbAddCredentials(
                GuestRegisterActivity.this,
                Constants.API_URL_GUESTS_USERNAMES,
                guestUsername,
                null //no password for guests
            );
            RequestQueue rQueue = Volley.newRequestQueue(this);
            rQueue.add(request);
            ret = true;
        }
        return ret;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart GuestRegisterActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume GuestRegisterActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause GuestRegisterActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop GuestRegisterActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy GuestRegisterActivity");
    }
}
