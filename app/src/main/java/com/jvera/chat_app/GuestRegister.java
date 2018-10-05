package com.jvera.chat_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class GuestRegister extends AppCompatActivity {

    private static final String TAG = "Debug" ;
    @BindView(R.id.pseudo_guest) EditText pseudo_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);
        Log.i(TAG, "onCreate GuestRegister");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.login_guest_btn})
    public void setOnClickGuestRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_guest_btn:
                if (register_click_action()) {
                    startActivity(new Intent(GuestRegister.this, Login.class)); //not Login
                    //TODO: create the real class to redirect to (group chat)
                }
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private boolean register_click_action() {
        boolean ret = false;
        String pseudo_user = pseudo_guest.getText().toString();
        String invalid_username_reason = Helper.check_username_validity(pseudo_user);

        if (!"".equals(invalid_username_reason)) {
            pseudo_guest.setError(invalid_username_reason);
        } else {
            StringRequest request = Helper.db_add_credentials(
                GuestRegister.this,
                Constants.api_url_guests_usernames,
                pseudo_user,
                null //no password for guests
            );
            RequestQueue rQueue = Volley.newRequestQueue(GuestRegister.this);
            rQueue.add(request);
            ret = true;
        }
        return ret;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart GuestRegister");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume GuestRegister");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause GuestRegister");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop GuestRegister");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy GuestRegister");
    }
}
