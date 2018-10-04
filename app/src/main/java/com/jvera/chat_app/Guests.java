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

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Guests extends AppCompatActivity {

    private static final String TAG = "Debug" ;
    private static final int guest_password_nbr = new Random().nextInt(100); //random between 0 and 100
    @BindView(R.id.pseudo_guest) EditText pseudo_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        Log.i(TAG, "onCreate Guests");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.login_guest_btn})
    public void setOnClickRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_guest_btn:
                if (register_click_action()) {
                    startActivity(new Intent(Guests.this, Login.class)); //not Login
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
        String invalid_username_reason = helper.check_username_validity(pseudo_user);

        if (!"".equals(invalid_username_reason)) {
            pseudo_guest.setError(invalid_username_reason);
        } else {
            StringRequest request = helper.db_add_credentials(
                Guests.this,
                constants.api_url_guests,
                pseudo_user,
                null //no password for guests
            );
            RequestQueue rQueue = Volley.newRequestQueue(Guests.this);
            rQueue.add(request);
            ret = true;
        }
        return ret;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart Guests");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume Guests");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause Guests");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop Guests");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy Guests");
    }
}
