package com.jvera.chat_app;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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


public class UserRegisterActivity extends AppCompatActivity {

    final static private String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Log.i(TAG, "onCreate UserRegisterActivity");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.register_btn, R.id.login_btn})
    public void setOnClickUserRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.register_btn:
                if (registerClickAction()) {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private boolean registerClickAction() {
        boolean ret = false;
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString();
        String invalidUserReason = Helper.checkUsernameValidity(user);
        String invalidPassReason = Helper.checkPasswordValidity(pass);

        if (!"".equals(invalidUserReason)) {
            login.setError(invalidUserReason);
        } else if (!"".equals(invalidPassReason)) {
            password.setError(invalidPassReason);
        } else {
            StringRequest request = Helper.dbAddCredentials(
                UserRegisterActivity.this,
                Constants.API_URL_USERS_USERNAMES,
                user,
                pass
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
        Log.i(TAG,"onStart UserRegisterActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume UserRegisterActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause UserRegisterActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop UserRegisterActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy UserRegisterActivity");
    }
}
