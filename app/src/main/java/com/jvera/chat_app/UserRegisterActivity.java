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
                if (register_click_action()) {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private boolean register_click_action() {
        boolean ret = false;
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString();
        String invalid_user_reason = Helper.check_username_validity(user);
        String invalid_pass_reason = Helper.check_password_validity(pass);

        if (!"".equals(invalid_user_reason)) {
            login.setError(invalid_user_reason);
        } else if (!"".equals(invalid_pass_reason)) {
            password.setError(invalid_pass_reason);
        } else {
            StringRequest request = Helper.db_add_credentials(
                UserRegisterActivity.this,
                Constants.api_url_users_usernames,
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
